import { Component, Input, OnInit } from '@angular/core';
import { EkubsService, EkubUsersService, PaymentsService, RoundsService } from '../../../../services/services';
import { EkubResponse, PageResponseUserResponse, PageResponseUserRoundPaymentResponse, RoundResponse, UserResponse, UserRoundPaymentResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { PaginationComponent } from "../../components/pagination/pagination.component";

@Component({
  selector: 'app-view-ekub-detail',
  imports: [CommonModule, ReactiveFormsModule, PaginationComponent],
  providers: [DatePipe],
  templateUrl: './view-ekub-detail.component.html',
  styleUrl: './view-ekub-detail.component.scss'
})
export class ViewEkubDetailComponent implements OnInit {

  @Input() ekubId: string | undefined;
  ekub: EkubResponse | undefined;
  rounds: RoundResponse[] = [];
  currentRound: number = 0;
  lastRoundWinner: UserResponse | undefined;
  ekubUsersList: UserResponse[] = [];
  ekubWinnersList: UserResponse[] = [];
  drawParticipantsList: UserResponse[] = [];
  userRoundPayments:UserRoundPaymentResponse[] = [
    // { "row": { "name": "Alice", "round1": 100, "round2": 150 } },
    // { "row": { "name": "Bob", "round1": 120, "round2": 130 } }
  ];
  userRoundPaymentHeader:string[] = [];
  // pagination for user round payments
  page:number = 0;
  size:number = 10;
  isEmptyPage: boolean = true;
  isFirstPage: boolean |undefined; 
  isLastPage: boolean |undefined;
  totalPages: number | undefined;
  totalElements: number | undefined;
  numberOfElements: number | undefined;
  showDetail:boolean = false;

  constructor(
    private ekubsService: EkubsService,
    private ekubUsersService: EkubUsersService,
    private roundService: RoundsService,
    private datePipe: DatePipe,
    private paymentsService:PaymentsService
  ) { }

  ngOnInit(): void {
    if (this.ekubId) {
      this.fetchEkubById(this.ekubId);
      this.fetchEkubUsers(this.ekubId);
      this.fetchEkubWinners(this.ekubId);
      this.fetchEkubDrawParticipants(this.ekubId);
      this.fetchUserRoundPayments(this.ekubId);
    }

  }

  //get ekbu by id
  fetchEkubById(ekubId: string) {
    this.ekubsService.getEkubById({
      'ekub-id': ekubId
    }).subscribe({
      next: (res: EkubResponse) => {
        this.ekub = res;
        this.currentRound = res.roundNumber as number;
        //fetch last round winner
        this.fetchLastRoundWinner(ekubId);
      }, error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  //fetch ekub users list
  fetchEkubUsers(ekubId: string) {
    this.ekubUsersService.getEkubUsers({
      'ekub-id': ekubId
    }).subscribe({
      next: (res: UserResponse[]) => {
        console.log(res);
        this.ekubUsersList = res;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  //fetch ekub winners list 
  fetchEkubWinners(ekubId: string) {
    this.ekubUsersService.getEkubWinners({
      'ekub-id': ekubId
    }).subscribe({
      next: (res: UserResponse[]) => {
        this.ekubWinnersList = res;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }
    })
  }

  //fetch ekub draw participants list
  fetchEkubDrawParticipants(ekubId: string) {
    this.ekubUsersService.getDrawParticipants({
      'ekub-id': ekubId
    }).subscribe({
      next: (res: UserResponse[]) => {
        this.drawParticipantsList = res;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  //fetch a last round winner
  fetchLastRoundWinner(ekubId: string) {
    const lastRound = Number(this.currentRound - 1);
    
    if(lastRound > 1){
      this.ekubUsersService.getRoundWinner({
        'ekub-id': ekubId,
        'round-no': lastRound
      }).subscribe({
        next:(res:UserResponse)=>{
          this.lastRoundWinner = res;
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
  }

  //get rounds of ekub
  fetchRoundsOFEkub(ekubId: string) {
    this.roundService.getEkubRounds({
      'ekub-id': ekubId
    }).subscribe({
      next: (res: RoundResponse[]) => {
        this.rounds = res;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      }

    })
  }

  // get user round payments
  fetchUserRoundPayments(ekubId:string){
    this.paymentsService.getUserRoundPayments({
      'ekub-id': ekubId,
      'page': this.page,
      'size': this.size
    }).subscribe({
      next:(res:PageResponseUserRoundPaymentResponse)=>{
        this.userRoundPayments = res.content as UserRoundPaymentResponse[];
        const firstRow = this.userRoundPayments[0].row;
        if(firstRow){
          this.userRoundPaymentHeader = Object.keys(firstRow).filter(
            key => key.startsWith('round')
          )
        }
        
        // pagination
        this.isEmptyPage = res.empty as boolean;
        this.isFirstPage = res.first;
        this.isLastPage = res.last;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.numberOfElements = res.numberOfElements;
        this.page = res.number as number;
        this.size = res.size as number;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }



  // is a user a winer
  hasWon(userId: any) {
    return this.ekubWinnersList
      .filter(user => user.id === userId as string)
      .length > 0;
  }

  // transform date time
  transformDateTime(dateString: any) {
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , yy, hh:mm a')
    return formattedDate;
  }
  

   //pagination methods
   onPageChanged(page:number){
    this.page = page;
    if(this.ekubId){
      this.fetchUserRoundPayments(this.ekubId);
    }
  }

  onSizeChanged(size:number){
    this.size = size;
    if(this.ekubId){
      this.fetchUserRoundPayments(this.ekubId);
    }
  }


}
