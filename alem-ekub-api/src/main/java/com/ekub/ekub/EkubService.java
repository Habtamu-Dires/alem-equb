package com.ekub.ekub;

import com.ekub.common.PageResponse;
import com.ekub.round.Round;
import com.ekub.round.RoundMapper;
import com.ekub.round.RoundResponse;
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
import org.springframework.stereotype.Service;

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
        System.out.println("The request " + request);
        Ekub ekub = repository.save(
                Ekub.builder()
                        .id(UUID.randomUUID())
                        .name(request.name())
                        .description(request.description())
                        .amount(request.amount())
                        .frequencyInDays(request.frequencyInDays())
                        .type(request.type())
                        .nextDrawDateTime(request.startDateTime())
                        .roundNumber(0)
                        .startDateTime(request.startDateTime())
                        .build()
        );

        // if ekub is active create first round
        if(ekub.isActive()){
            Round newRound = roundService.createNewRound(ekub);
            ekub.setRoundNumber(newRound.getRoundNumber());
            repository.save(ekub);
        }
    }

    // get ekubs
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
    public Ekub findEkubById(String ekubId){
        return repository.findById(UUID.fromString(ekubId))
                .orElseThrow(() -> new EntityNotFoundException("Ekub not found exception"));
    }

    // find ekub by name
    public Ekub findEkubByName(String name){
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Ekub with name %s not found exception",name)));
    }

    // update ekub
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateEkubInfo(EkubRequest request){
        Ekub ekub = this.findEkubById(request.id());

        ekub.setName(request.name());
        ekub.setAmount(request.amount());
        ekub.setType(request.type());
        ekub.setFrequencyInDays(request.frequencyInDays());
        ekub.setNextDrawDateTime(request.nextDrawDateTime());
        ekub.setStartDateTime(request.startDateTime());
        ekub.setActive(request.isActive());

        Ekub savedEkub = repository.save(ekub);
        // if ekub is updated to be active
        if(savedEkub.isActive() && savedEkub.getRoundNumber() == 0){
            Round newRound = roundService.createNewRound(ekub);
            ekub.setRoundNumber(newRound.getRoundNumber());
            repository.save(ekub);
        }
    }

    // delete ekub
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteEkub(String ekubId){
        Ekub ekub = this.findEkubById(ekubId);
        repository.delete(ekub);
    }

    //get current round of ekub
    public Round getCurrentRound(Ekub ekub){
        return roundService.getRoundByEkubIdAndRoundNo(ekub.getId(),ekub.getRoundNumber());
    }

    // get current RoundResponse of ekbu
    public RoundResponse getCurrentRound (String ekubId){
        Ekub ekub = findEkubById(ekubId);
        return roundMapper.toRoundResponse(getCurrentRound(ekub));
    }

    // save ekub
    public void save(Ekub ekub) {
        repository.save(ekub);
    }

    // get ekub by id
    public EkubResponse getEkubById(String ekubId) {
        return mapper.toEkubResponse(this.findEkubById(ekubId));
    }

    // searching
    public List<EkubResponse> searchEkubByName(String ekubName) {
        Specification<Ekub> searchSpec = EkubSpecification.searchEkubByName(ekubName);

        return repository.findAll(searchSpec)
                .stream()
                .map(mapper::toEkubResponse)
                .toList();
    }
}
