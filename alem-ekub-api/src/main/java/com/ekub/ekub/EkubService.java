package com.ekub.ekub;

import com.ekub.common.PageResponse;
import com.ekub.round.RoundMapper;
import com.ekub.round.RoundService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EkubService {

    private final EkubRepository repository;
    private final EkubMapper mapper;
    private final RoundService roundService;
    private final RoundMapper roundMapper;

    // create ekub
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createEkub(@Valid EkubRequest request) {
        if(repository.findByName(request.name()).isPresent()){
            throw new EntityExistsException("Ekub with name " + request.name() + " already exists");
        }
        repository.save(
            Ekub.builder()
                .name(request.name())
                .version(0)
                .description(request.description())
                .exclusive(request.isExclusive())
                .amount(request.amount())
                .penaltyPercentPerDay(request.penaltyPercentPerDay())
                .frequencyInDays(request.frequencyInDays())
                .type(request.type())
                .nextDrawDateTime(request.startDateTime())
                .roundNumber(0)
                .startDateTime(request.startDateTime())
                .archived(request.isArchived())
                .build()
        );

    }

    // update ekub
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateEkubInfo(EkubRequest request){
        Ekub ekub = findEkubByExternalId(request.id());
        ekub.setName(request.name());
        ekub.setAmount(request.amount());
        ekub.setPenaltyPercentPerDay(request.penaltyPercentPerDay());
        ekub.setType(request.type());
        ekub.setFrequencyInDays(request.frequencyInDays());
        ekub.setNextDrawDateTime(request.nextDrawDateTime());
        ekub.setStartDateTime(request.startDateTime());
        ekub.setExclusive(request.isExclusive());
        ekub.setArchived(request.isArchived());

        Ekub savedEkub = repository.save(ekub);
        // if ekub is updated to be active
        if( request.isActive()
            && !savedEkub.isActive()
            && savedEkub.getNextDrawDateTime() != null)
        {
            System.out.println("Ekub activated so reset");
            resetEkub(savedEkub);
        } else if(!request.isActive() && savedEkub.isActive()) { // de-activate
            savedEkub.setActive(false);
            savedEkub.setNextDrawDateTime(null);
            repository.save(savedEkub);
        }
    }

    //reset ekub
    public void resetEkub(Ekub ekub){
        ekub.setActive(true); //activate ekub
        //start new version of ekub
        ekub.setVersion(ekub.getVersion() + 1);
        ekub.setRoundNumber(0);
        ekub.setTotalAmount(BigDecimal.ZERO);
        //create new round
        roundService.createNewRound(ekub);
        ekub.setRoundNumber(1);
        repository.save(ekub);
    }

    // update total amount of ekub
    public void updateTotalAmountOfEkub(int ekubId, BigDecimal amount) {
        Ekub ekub = repository.findById(ekubId)
                .orElseThrow(()-> new EntityNotFoundException("Ekub not found"));

        BigDecimal prvAmount = ekub.getTotalAmount();
        BigDecimal updated = prvAmount.add(amount);
        ekub.setTotalAmount(updated);
        repository.save(ekub);
    }

    // get page of ekubs
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResponse<EkubResponse> getPageOfEkubs(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Ekub> res = repository.findAll(pageable);
        List<EkubResponse> ekubResponsesList = res.map(mapper::toEkubResponse).toList();

        return PageResponse.<EkubResponse>builder()
                .content(ekubResponsesList)
                .totalElements(res.getTotalElements())
                .numberOfElements(res.getNumberOfElements())
                .totalPages(res.getTotalPages())
                .size(res.getSize())
                .number(res.getNumber())
                .first(res.isFirst())
                .last(res.isLast())
                .empty(res.isEmpty())
                .build();
    }

    //find ekub by id
    public Ekub findEkubByExternalId(String ekubId){
        return repository.findByExternalId(UUID.fromString(ekubId))
                .orElseThrow(() -> new EntityNotFoundException("Equb not found"));
    }

    // get list of ekubs for a user
    public List<EkubResponse> getEkubsOfUser(String userId){
        return repository.findEkubsByUserId(userId)
                .stream()
                .map(mapper::toEkubResponse)
                .toList();
    }

    // delete ekub
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteEkub(String ekubId){
        Ekub ekub = this.findEkubByExternalId(ekubId);
        repository.delete(ekub);
    }

    // save ekub
    public void save(Ekub ekub) {
        repository.save(ekub);
    }

    // get ekub by id
    public EkubResponse getEkubById(String ekubId) {
        return mapper.toEkubResponse(this.findEkubByExternalId(ekubId));
    }

    // searching
    public List<EkubResponse> searchEkubByName(String ekubName) {
        Specification<Ekub> searchSpec = EkubSpecification.searchEkubByName(ekubName);

        return repository.findAll(searchSpec)
                .stream()
                .map(mapper::toEkubResponse)
                .toList();
    }

    // get public ekbus
    public List<EkubResponse> getPublicEkubsToJoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();
        return   repository.findPublicEkubsToJoin(loggedUserId)
                 .stream()
                 .map(mapper::toEkubResponse)
                 .toList();
    }


    // find InvitedEkubs not joined yet
    public List<EkubResponse> getInvitedEkubsYetToJoin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = authentication.getName();

        return repository.findInvitedEkubsYetToJoin(loggedUserId)
                .stream()
                .map(mapper::toEkubResponse)
                .toList();
    }

    //get ekub status
    public EkubStatusResponse getEkubStatus(String ekubId, int version){
        return repository.findEkubStatus(UUID.fromString(ekubId),version);
    }

}
