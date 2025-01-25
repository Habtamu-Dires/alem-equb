import { Component, HostListener } from '@angular/core';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { HeaderComponent } from "../../components/header/header.component";
import { PaymentsService } from '../../../../services/services';
import { HttpErrorResponse } from '@angular/common/http';
import { PaymentResponse } from '../../../../services/models/payment-response';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-history',
  imports: [HeaderComponent,CommonModule],
  providers:[DatePipe],
  templateUrl: './payment-history.component.html',
  styleUrl: './payment-history.component.scss'
})
export class PaymentHistoryComponent {
  onMobileView:boolean= true;
  loggedUser:UserProfile | undefined;
  paymentResponses:PaymentResponse[] = [];

  constructor(
    private keycloakService:KeycloakService,
    private paymentsService:PaymentsService,
    private router:Router,
    private datePipe:DatePipe
  ) { }

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserPayments(this.loggedUser.id);
    }
  }

  transformDateTime(dateString:any){
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , MM, yyyy, hh:mm a')
    return formattedDate;
  }

  // fetch user payments 
  fetchUserPayments(userId:string){
    this.paymentsService.getUserPayments({
      'user-id': userId
    }).subscribe({
      next:(res:PaymentResponse[])=>{
        this.paymentResponses = res as PaymentResponse[];
      },
      error:(err:HttpErrorResponse) =>{
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
}
