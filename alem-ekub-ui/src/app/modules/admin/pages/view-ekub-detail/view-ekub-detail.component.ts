import { Component, Input, OnInit } from '@angular/core';
import { EkubsService, EkubUsersService, PaymentsService, RoundsService, UserGuaranteesService, UsersService } from '../../../../services/services';
import { EkubResponse,  EkubStatusResponse,  MemberDetailResponse,  RoundResponse, UserResponse, UserRoundPaymentResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { PaymentMethodDialogComponent } from '../../components/payment-method-dialog/payment-method-dialog.component';
import { PaymentRequest } from '../../../../services/models';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { ToastrService } from 'ngx-toastr';
import { ConfirmationDialogComponent } from '../../../../components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-view-ekub-detail',
  imports: [CommonModule, ReactiveFormsModule,FormsModule],
  providers: [DatePipe],
  templateUrl: './view-ekub-detail.component.html',
  styleUrl: './view-ekub-detail.component.scss'
})
export class ViewEkubDetailComponent implements OnInit {

  @Input() ekub: EkubResponse | undefined;
  paymentRequest:PaymentRequest | undefined;
  rounds: RoundResponse[] = [];
  lastRound: number = 0;
  userRoundPayments:UserRoundPaymentResponse[] = [];
  userRoundPaymentHeader:string[] = [];
  loggedUser:UserProfile | undefined;
  invitedUsers: UserResponse[] = [];
  ekubStatus:EkubStatusResponse | undefined;
  memberDetails:MemberDetailResponse[] = [];
  currentVersion:number | undefined;
  ekubVesions:number [] = [];
 
  constructor(
    private keycloakService:KeycloakService,
    private ekubsService:EkubsService,
    private ekubUsersService: EkubUsersService,
    private roundService: RoundsService,
    private usersService:UsersService,
    private datePipe: DatePipe,
    private paymentsService:PaymentsService,
    private dialog:MatDialog,
    private toastrService:ToastrService,
    private userGuaranteeService:UserGuaranteesService,
  ) {}

  ngOnInit(): void {
    this.loggedUser = this.keycloakService.profile;
    if (this.ekub?.id && this.ekub.version !== undefined) {
        // this.fetchEkubStatus(this.ekub.id,this.ekub.version);
        // this.fetchMembersDetail(this.ekub.id,this.ekub.version);
        if(this.ekub.version){
          this.fetchRoundsOfEkub(this.ekub.id, this.ekub.version);
          // this.fetchUserRoundPayments(this.ekub.id, this.ekub.version);
        }
        //fill the versions
        this.currentVersion = this.ekub.version;
        this.ekubVesions =  Array.from({ length: this.currentVersion }, (_, i) => i + 1);
    }

    // update last round number
    if(this.ekub?.isActive){ // there is still another rounds
      this.lastRound = this.ekub.roundNumber as number - 1;
    } else {
      // it is the last round 
      this.lastRound = this.ekub?.roundNumber as number;
    }

    //fetch invited users if ekub is exclusive and not active
    if(this.ekub?.isExclusive && !this.ekub.isActive && this.ekub.id){
      this.fetchInvitedUsers(this.ekub.id)
    }

  }

  //fetch ekub status information
  fetchEkubStatus(ekubId:string,version:number){
    this.ekubsService.getEkubStatus({
      'ekub-id': ekubId,
      'version': version
    }).subscribe({
      next:(res:EkubStatusResponse)=>{
        this.ekubStatus = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //fetck member detail
  fetchMembersDetail(ekubId:string,version:number){
    this.ekubUsersService.getMemberDetail({
      'ekub-id': ekubId,
      'version': version
    }).subscribe({
      next:(res:MemberDetailResponse[])=>{
        this.memberDetails = res;
        console.log("the member detail length", res.length);
        console.log("The current version is " + this.currentVersion);
        console.log("the ekub version " + this.ekub?.version);
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }  

  // guarant winner
  guarantWinner(round:RoundResponse){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message:'you wants to guarantee user',
        buttonName: 'guarantee'
      }
    });
    dialogRef.afterClosed().subscribe(result =>{
      if(result){
        this.userGuaranteeService.guaranteeUser({
          'round-id': round.id as string,
          'user-id': round.winner?.id as string
        }).subscribe({
          next:()=>{
            this.fetchRoundsOfEkub(this.ekub?.id as string, round.version as number);
            this.fetchMembersDetail(
              this.ekub?.id  as string,
              this.currentVersion as number
            )
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

  // check if he has a guarantor
  hasGuarantor(round:RoundResponse){
    const numberOfGuarantor = round.userGuarantees?.length ? round.userGuarantees.length : 0;
    return numberOfGuarantor > 0;
  }

  // remove guarantor
  removeGuarantor(round:RoundResponse,guarantorId:any){
    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message:'You want to remove guarant from ' + round.winner?.username,
        buttonName: 'continue',
        isWarning: true
      }
    });
    dialogRef.afterClosed().subscribe(result =>{
      if(result && guarantorId){
        this.userGuaranteeService.cancelGuarantee({
          'round-id': round.id as string,
          'guarantor-id': guarantorId,
          'guaranteed-id': round.winner?.id as string
        }).subscribe({
          next:()=>{
            this.fetchRoundsOfEkub(this.ekub?.id as string, round.version as number);
            this.toastrService.success("Successfully remove guarantee")
          },
          error:(err)=>{
            console.log(err);
            this.toastrService.error("Something went wrong", 'Ooops');
          }
        })
      }
    })
  }

  //get rounds of ekub
  fetchRoundsOfEkub(ekubId: string, version:number) {
    if(this.ekub?.version){
      this.roundService.getEkubRounds({
        'ekub-id': ekubId,
        'version': version
      }).subscribe({
        next: (res: RoundResponse[]) => {
          this.rounds = res;
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        }
  
      })
    }
  }

  // get user round payments
  fetchUserRoundPayments(ekubId:string, version:number){
    this.paymentsService.getUserRoundPayments({
      'ekub-id': ekubId,
      'version':version,
    }).subscribe({
      next:(res: UserRoundPaymentResponse[])=>{
        this.userRoundPayments = res as UserRoundPaymentResponse[];
      
        console.log("ts map:", this.userRoundPayments);
        
      if(this.userRoundPayments.length > 0){
          const firstRow = this.userRoundPayments[0].row;
        if(firstRow){
          this.userRoundPaymentHeader = Object.keys(firstRow).filter(
            key => key.startsWith('round')
          )
        }
      }
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // fetch invited users list 
  fetchInvitedUsers(ekubId:string){
    this.usersService.getInvitedUsersInEkubAndNotJoined({
      'ekub-id': ekubId
    }).subscribe({
      next:(res:UserResponse[])=>{
        this.invitedUsers = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // create payment 
  createPayment(round:RoundResponse){
    if(this.paymentRequest){
      this.paymentsService.createPayment({
        body: this.paymentRequest
      }).subscribe({
        next:()=>{
          //update round 
          this.toastrService.success('Payment Successfully Made', 'Done');
          this.fetchRoundsOfEkub(
            this.ekub?.id as string, 
            round.version as number
          );
        },
        error:(err)=>{
          console.log(err);
          this.toastrService.error('Oops Something Went Wrong', 'Ooops');
        }
      })
    }
  }

  // pay round winner
  payRoundWinner(round:RoundResponse){
    //open dialog
    const dialogRef = this.dialog.open(PaymentMethodDialogComponent,{
      width: '400px',
      data: {
        phoneNumber: round.winner?.phoneNumber,
        fullName:round.winner?.fullName,
        username:round.winner?.username,
        amount:this.ekub?.winAmount
      }
    });
    // data from dialog
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        console.log("Selected Method " + result.paymentMethod);
        console.log("Remakr ", result.remark);
        this.paymentRequest = {
           'amount': this.ekub?.winAmount as number,
           'roundId': round.id as string,
           'userId': this.loggedUser?.id as string,
           'toUserId':round.winner?.id as string,
           'type':'WINNING_PAYOUT',
           'paymentMethod': result.paymentMethod,
           'remark': result.remark
        }
        //create payemnt
        this.createPayment(round);
      }
    })
  }

  //cancel user invitation
  cancelInvitation(user:UserResponse){

    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message:'You want to delete invitation ' + user?.username,
        buttonName: 'yes',
        isWarning: true
      }
    });
    
    dialogRef.afterClosed().subscribe(result =>{
      if(result && user.id){
        this.usersService.cancelInvitation({
          'ekub-id': this.ekub?.id as string,
          'user-id': user.id 
        }).subscribe({
          next:()=>{
            this.fetchInvitedUsers(this.ekub?.id as string);
          },
          error:(err)=>{
            console.log(err);
          }
        })
      }
    })
  }


  // set ekub version
  setVersion(){
    this.fetchRoundsOfEkub(this.ekub?.id as string, this.currentVersion as number);
    this.fetchUserRoundPayments(this.ekub?.id as string, this.currentVersion as number);
    this.fetchEkubStatus(this.ekub?.id as string, this.currentVersion as number);
    this.fetchMembersDetail(this.ekub?.id as string, this.currentVersion as number);
  }

  // transform date time
  transformDateTime(dateString: any) {
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , yy, hh:mm a')
    return formattedDate;
  }
  
}
