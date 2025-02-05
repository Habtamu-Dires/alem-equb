import { Component, OnInit } from '@angular/core';
import { PaymentsService } from '../../../../services/services';
import { PageResponsePaymentResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { PaymentResponse } from '../../../../services/models';
import { HeaderComponent } from "../../components/header/header.component";
import { PaginationComponent } from "../../components/pagination/pagination.component";

@Component({
  selector: 'app-payment',
  imports: [CommonModule, HeaderComponent, PaginationComponent],
  providers:[DatePipe],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss'
})
export class PaymentComponent implements OnInit{

  paymentList:PaymentResponse[] = [];
  // pagination
  page:number = 0;
  size:number = 5;
  isEmptyPage: boolean = true;
  isFirstPage: boolean |undefined; 
  isLastPage: boolean |undefined;
  totalPages: number | undefined;
  totalElements: number | undefined;
  numberOfElements: number | undefined;
  showDetail:boolean = false;

  constructor(
    private paymentsService:PaymentsService,
    private datePipe:DatePipe
  ){}

  ngOnInit(): void {
    this.fetchPageOfPayments();
  }

  //fetch all payments
  fetchPageOfPayments(){
    this.paymentsService.getPageOfPayments({
      'page': this.page,
      'size': this.size
    }).subscribe({
      next:(res:PageResponsePaymentResponse) => {
        this.paymentList = res.content as PaymentResponse[];
        // pagination
        this.isEmptyPage = res.empty as boolean;
        this.isFirstPage = res.first;
        this.isLastPage = res.last;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.numberOfElements = res.numberOfElements;
        this.page = res.number as number;
        this.size = res.size as number;
      }
    })
  }

  //formatted date time
  formattedDateTime(dateTime:any){
    if(dateTime){
      const date = new Date(dateTime as string);
      return this.datePipe.transform(date, 'MMM dd, yy hh:mm a');
    }
    return '';
  }

  //pagination methods
  onPageChanged(page:number){
    this.page = page;
    this.fetchPageOfPayments();
  }

  onSizeChanged(size:number){
    this.size = size;
    this.fetchPageOfPayments();
  }
}
