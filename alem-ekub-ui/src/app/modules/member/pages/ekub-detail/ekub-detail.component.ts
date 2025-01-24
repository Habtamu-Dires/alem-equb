import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EkubsService, EkubUsersService } from '../../../../services/services';
import { EkubResponse, UserResponse } from '../../../../services/models';
import { HttpErrorResponse } from '@angular/common/http';
import { HeaderComponent } from "../../components/header/header.component";
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-ekub-detail',
  imports: [HeaderComponent,CommonModule],
  providers:[DatePipe],
  templateUrl: './ekub-detail.component.html',
  styleUrl: './ekub-detail.component.scss'
})
export class EkubDetailComponent implements OnInit {
  
  onMobileView:boolean = true;
  ekub:EkubResponse | undefined;
  currentRound:number = 0;
  lastRoundWinner:UserResponse | undefined;
  ekubUsersList:UserResponse[] = [];
  ekubWinnersList:UserResponse[] = [];
  drawParticipantsList:UserResponse[]=[];

  constructor(
    private activatedRoute: ActivatedRoute,
    private ekubService:EkubsService,
    private ekubUsersService:EkubUsersService,
    private datePipe:DatePipe
  ){}

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    const ekubId = this.activatedRoute.snapshot.params['ekubId'];
    if(ekubId){
      this.fetchEkubById(ekubId);
      this.fetchEkubUsers(ekubId);
      this.fetchEkubWinners(ekubId);
      this.fetchEkubDrawParticipants(ekubId);
    }
  }

  //get ekbu by id
  fetchEkubById(ekubId:string){
    this.ekubService.getEkubById({
      'ekub-id': ekubId
    }).subscribe({
      next:(res:EkubResponse)=>{
        this.ekub = res;
        this.currentRound = res.roundNumber as number;
        //fetch last round winner
        this.fetchLastRoundWinner(ekubId);
      }, error:(err:HttpErrorResponse)=>{
        console.log(err);
      }
    })
  }

  //fetch ekub users list
  fetchEkubUsers(ekubId:string){
    this.ekubUsersService.getEkubUsers({
      'ekub-id': ekubId
    }).subscribe({
      next:(res:UserResponse[])=>{
        console.log(res);
        this.ekubUsersList = res;
      },
      error:(err:HttpErrorResponse)=>{
        console.log(err);
      }
    })
  }

  //fetch ekub winners list 
  fetchEkubWinners(ekubId:string){
    this.ekubUsersService.getEkubWinners({
      'ekub-id': ekubId
    }).subscribe({
      next:(res:UserResponse[])=>{
        this.ekubWinnersList = res;
      },
      error:(err:HttpErrorResponse)=>{
        console.log(err);
      }
    })
  }

  //fetch ekub draw participants list
  fetchEkubDrawParticipants(ekubId:string){
    this.ekubUsersService.getDrawParticipants({
      'ekub-id': ekubId
    }).subscribe({
      next:(res:UserResponse[]) =>{
        this.drawParticipantsList = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //fetch a last round winner
  fetchLastRoundWinner(ekubId:string){
    const lastRound = this.currentRound - 1;
    if(lastRound < 1){
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

  // is a user a winer
  hasWon(userId:any){
    return this.ekubWinnersList
                .filter(user => user.id === userId as string)
                .length > 0;
  }


  // transform date time
  transformDateTime(dateString:any){
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , yy, hh:mm a')
    return formattedDate;
  }

  // Listen for window resize events
  @HostListener('window:resize',['$event'])
  onResize(event:any){
    this.checkScreenSzie(event.target.innerWidth);
  }

  // check screen size and set hideSideBar value
  private checkScreenSzie(width:number){
    if (width < 1008) { // Small screen threshold (can be adjusted)
        this.onMobileView = true;
    } else {
      this.onMobileView = false;
    }
  }


}
