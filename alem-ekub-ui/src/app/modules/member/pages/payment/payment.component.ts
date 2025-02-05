import { Component, HostListener } from '@angular/core';
import { PaymentsService, RoundsService, UsersService } from '../../../../services/services';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { Router } from '@angular/router';
import { HeaderComponent } from "../../components/header/header.component";
import { CommonModule, DatePipe } from '@angular/common';
import { UserPendingPaymentResponse, UserResponse } from '../../../../services/models';
import { HttpErrorResponse } from '@angular/common/http';
import { formatDistanceToNow } from 'date-fns';
import { PaymentRequest } from '../../../../services/models';
import { MatDialog } from '@angular/material/dialog';
import { PaymentMethodDialogComponent } from '../../components/payment-method-dialog/payment-method-dialog.component';
import { ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-payment',
  imports: [HeaderComponent,CommonModule],
  providers:[DatePipe],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss'
})
export class PaymentComponent {
  onMobileView:boolean= true;
  loggedUser:UserProfile | undefined;
  pendingPayments:UserPendingPaymentResponse[] = [];
  user:UserResponse | undefined;

  constructor(
    private keycloakService:KeycloakService,
    private roundsService:RoundsService,
    private paymentsService:PaymentsService,
    private router:Router,
    private datePipe:DatePipe,
    private dialog:MatDialog,
    private usersService:UsersService,
    private toastrService:ToastrService
  ) {}

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserPendingPayments(this.loggedUser.id);
      this.fetchUserById(this.loggedUser.id);
    }
  }

  //fetch user pending payments
  fetchUserPendingPayments(userId:string){
    this.roundsService.getUserPendingPayments({
      'user-id':userId
    }).subscribe({
      next:(res:UserPendingPaymentResponse[]) =>{
        this.pendingPayments = res as UserPendingPaymentResponse[];
      },
      error:(err:HttpErrorResponse)=>{
        console.log(err);
      }
    })
  }

  // fetch user  ---> this could be optimized / removed
  fetchUserById(userId:string){
    this.usersService.getUserById({
      'user-id':userId
    }).subscribe({
      next:(res)=>{
        this.user = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //create payment 
  createPayment(paymentRequest:PaymentRequest){
    const userId = this.loggedUser?.id as string;
    this.paymentsService.createPayment({
      body: paymentRequest
    }).subscribe({
      next:()=>{
        this.fetchUserPendingPayments(userId);
        this.toastrService.success('Payment Successfully Made', 'Done');
      },
      error:(err)=>{
        console.log(err);
        this.toastrService.error('Something went wrong', 'Ooops');
      }
    })
  }

  // on payment
  onPayment(pendingPayment:UserPendingPaymentResponse){
    const userId = this.loggedUser?.id as string;
    const username = this.loggedUser?.username as string;
    //open dialog
    const dialogRef = this.dialog.open(PaymentMethodDialogComponent,{
      width: '400px',
      data: {
        phoneNumber:this.user?.phoneNumber,
        username: username,
        totalAmount: pendingPayment.totalAmount
      }
    });
    // data from dialog
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        console.log("The phone number ", result.phoneNumber);
        // build payment request
        const paymentRequest:PaymentRequest = {
          'userId': userId,
          'amount':  pendingPayment.totalAmount as number,
          'roundId': pendingPayment.roundId as string,
          'paymentMethod': result.paymentMethod,
          'type': 'MEMBER_PAYMENT',
          'remark': result.remark
        }
        
        //create payemnt
        this.createPayment(paymentRequest);
      }
    })    
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
      this.router.navigate(['member','home']);
    }
  }

  transformDateTime(dateString:any){
      const date = new Date(dateString);
      const formattedDate = this.datePipe.transform(date, 'EEE, dd , MM, hh:mm a')
      return formattedDate;
  }
  
  //transfrom  duaration
  transfromDuration(dateString:any){
    const date = new Date(dateString);
    const duration = formatDistanceToNow(date, {addSuffix: true})
    return duration;
  }
}
