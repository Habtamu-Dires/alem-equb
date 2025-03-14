
import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EkubsService, EkubUsersService, RoundsService, UserGuaranteesService } from '../../../../services/services';
import { BooleanResponse, EkubResponse, EkubStatusResponse, LastRoundResponse, MemberDetailResponse, RoundResponse, UserResponse } from '../../../../services/models';
import { HeaderComponent } from "../../components/header/header.component";
import { CommonModule, DatePipe } from '@angular/common';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { MemberService } from '../../../../services/member-services/member.service';
import { ConfirmationDialogComponent } from '../../../../components/confirmation-dialog/confirmation-dialog.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-ekub-detail',
  imports: [HeaderComponent,CommonModule,FormsModule],
  providers:[DatePipe],
  templateUrl: './ekub-detail.component.html',
  styleUrl: './ekub-detail.component.scss'
})
export class EkubDetailComponent implements OnInit {
  
  onMobileView:boolean = true;
  ekub:EkubResponse | undefined;
  lastRoundNo:number = 0;
  lastRound:LastRoundResponse | undefined;
  loggedUserId:string | undefined;
  isAllowedToBeGuarantor:boolean = false;
  ekubStatus:EkubStatusResponse | undefined;
  membersDetailList: MemberDetailResponse[] = [];
  ekubType:string = '';
  currentVersion:number | undefined;

  constructor(
    private memberService:MemberService,
    private activatedRoute: ActivatedRoute,
    private keycloakService:KeycloakService,
    private ekubService:EkubsService,
    private ekubUsersService:EkubUsersService,
    private userGuaranteeService:UserGuaranteesService,
    private roundsService:RoundsService,
    private datePipe:DatePipe,
    private dialog:MatDialog,
    private toastrService:ToastrService,
    private router:Router
  ){}

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    this.ekubType = this.activatedRoute.snapshot.params['type'];
    this.loggedUserId = this.keycloakService.profile.id;
    
    // get ekub 
    this.memberService.selectedEkub$.subscribe((ekub:EkubResponse)=>{
      this.ekub = ekub;

      //upate  lastRound number
      if(this.ekub.isActive && ekub.roundNumber && ekub.roundNumber > 1){
        this.lastRoundNo = ekub.roundNumber as number - 1;
      } else {
        this.lastRoundNo = ekub.roundNumber as number;
      }
      // fetch datas
      if(ekub.id && ekub.version !== undefined){
        this.currentVersion = ekub.version;
        this.fetchEkubStatus(ekub.id,ekub.version);
        this.fetchMembersDetail(ekub.id, ekub.version);
        if(ekub.version){
          this.fetchLastRound(ekub.id,ekub.version);
          this.fetchIsAllowedToGuarantor(ekub.id,ekub.version );
        }
      }
    })
  }

  //fetch ekub status information
  fetchEkubStatus(ekubId:string, version:number){
    this.ekubService.getEkubStatus({
      'ekub-id':ekubId,
      'version':version
    }).subscribe({
      next:(res:EkubStatusResponse)=>{
        this.ekubStatus = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //get members detail
  fetchMembersDetail(ekubId:string,version:number){
    this.ekubUsersService.getMemberDetail({
      'ekub-id':ekubId,
      'version':version
    }).subscribe({
      next:(res:MemberDetailResponse[])=>{
        this.membersDetailList = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //fetch round by ekubId and round no
  fetchLastRound(ekubId:string,version:number){
    this.roundsService.getLastRound({
      'ekub-id': ekubId,
      'version': version,
      'round-no': this.lastRoundNo
    }).subscribe({
      next:(res:RoundResponse)=>{
        this.lastRound = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // isAllowedToGuarant
  fetchIsAllowedToGuarantor(ekubId:string,version:number){
    this.userGuaranteeService.isAllowedToBeGuarantor({
      'ekub-id': ekubId,
      'version': version
    }).subscribe({
      next:(res:BooleanResponse)=>{
        this.isAllowedToBeGuarantor = res.result as boolean;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //guarantee
  guarantWinner(winRoundId:string, winnerId:string){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
          width: '400px',
          data:{
            message:'you wants to guarantee user',
            buttonName: 'guarantee',
            isWarning: false
          }
    });
    dialogRef.afterClosed().subscribe(result =>{
      if(result){
        this.userGuaranteeService.guaranteeUser({
          'round-id': winRoundId,
          'user-id': winnerId
        }).subscribe({
          next:()=>{
            this.isAllowedToBeGuarantor = false;
            // fetch update member detail
            this.fetchMembersDetail(
                this.ekub?.id as string,
                this.currentVersion as number
            );
            this.toastrService.success("Successfully guaranteed user")
          },
          error:(err)=>{
            console.log(err);
            this.toastrService.error("Something went wrong", 'Ooops');
          }
        })
      }
    })
  }

  // leaving ekub
  onLeavingEkub(ekubId:any){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message: 'You want toleave this equb',
        buttonName: 'Leave'
      }
    });
    // dialog confirmation
    dialogRef.afterClosed().subscribe((result)=>{
      if(result){
        console.log("leave the ekub");
        this.leaveEkub(ekubId);
      }
    })
  }

  // leave ekub
  leaveEkub(ekubId:any){
    this.ekubUsersService.leaveEkub({
      'ekub-id': ekubId
    }).subscribe({
      next:()=>{
        this.router.navigate(['member','ekubs']);
      },
      error:(err)=>{
        console.log(err);
        if(err.error){
          const errObj = JSON.parse(err.error);
          this.toastrService.error(errObj.error, 'Ooops');
        }
      }
    })
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
