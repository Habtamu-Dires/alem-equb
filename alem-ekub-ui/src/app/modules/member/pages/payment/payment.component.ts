import { Component, HostListener } from '@angular/core';
import { PaymentsService, RoundsService } from '../../../../services/services';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { Router } from '@angular/router';
import { HeaderComponent } from "../../components/header/header.component";
import { CommonModule, DatePipe } from '@angular/common';
import { UserPendingPaymentResponse } from '../../../../services/models';
import { HttpErrorResponse } from '@angular/common/http';
import { formatDistanceToNow } from 'date-fns';
import { PaymentRequest } from '../../../../services/models';


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

  constructor(
    private keycloakService:KeycloakService,
    private roundsService:RoundsService,
    private paymentsService:PaymentsService,
    private router:Router,
    private datePipe:DatePipe
  ) {}

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserPendingPayments(this.loggedUser.id);
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

  onPayment(pendingPayment:UserPendingPaymentResponse){
    const userId = this.loggedUser?.id as string;
    const paymentRequest:PaymentRequest = {
        'userId': userId,
        'amount':  pendingPayment.amount as number,
        'roundId': pendingPayment.roundId as string,
    }
    this.paymentsService.createPayment({
      body: paymentRequest
    }).subscribe({
      next:()=>{
        this.fetchUserPendingPayments(userId);
      },
      error:(err)=>{
        console.log(err);
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
    const duration = formatDistanceToNow(date)
    return duration;
  }
}
