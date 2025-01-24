import { Component, Input, OnInit } from '@angular/core';
import { EkubsService, EkubUsersService } from '../../../../services/services';
import {  EkubResponse, UserResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-view-ekub-detail',
  imports: [CommonModule,ReactiveFormsModule],
  providers:[DatePipe],
  templateUrl: './view-ekub-detail.component.html',
  styleUrl: './view-ekub-detail.component.scss'
})
export class ViewEkubDetailComponent implements OnInit{
  
  @Input() ekubId: string | undefined;
  // ekubPaymentStatusList: EkubPaymentStatusResponse[] = [];
  ekub:EkubResponse | undefined;
  currentRound:number = 0;
  lastRoundWinner:UserResponse | undefined;
  ekubUsersList:UserResponse[] = [];
  ekubWinnersList:UserResponse[] = [];
  drawParticipantsList:UserResponse[]=[];

    searchControl= new FormControl();

  constructor(
    private ekubsService:EkubsService,
    private ekubUsersService:EkubUsersService,
    private datePipe:DatePipe
  ) {}

  ngOnInit(): void {
    if(this.ekubId){
      this.fetchEkubById(this.ekubId);
      this.fetchEkubUsers(this.ekubId);
      this.fetchEkubWinners(this.ekubId);
      this.fetchEkubDrawParticipants(this.ekubId);
    }
    
  }

  //get ekbu by id
    fetchEkubById(ekubId:string){
      this.ekubsService.getEkubById({
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
  


}
