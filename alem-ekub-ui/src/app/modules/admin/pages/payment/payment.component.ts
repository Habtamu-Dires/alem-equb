import { Component, OnInit } from '@angular/core';
import { PaymentsService } from '../../../../services/services';
import { PageResponsePaymentResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { PaymentResponse } from '../../../../services/models';
import { HeaderComponent } from "../../components/header/header.component";
import { PaginationComponent } from "../../components/pagination/pagination.component";
import { filter } from 'rxjs';

@Component({
  selector: 'app-payment',
  imports: [CommonModule, HeaderComponent, PaginationComponent],
  providers:[DatePipe],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.scss'
})
export class PaymentComponent implements OnInit{

  paymentList:PaymentResponse[] = [];
  ekubFilter:string = '';
  dateTimeFilter:string = '';
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
      'ekubId-filter':this.ekubFilter,
      'dateTime-filter': this.dateTimeFilter,
      'page': this.page,
      'size': this.size
    }).subscribe({
      next:(res:PageResponsePaymentResponse) => {
        console.log("hello");
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

  // on search
  onSearch(name:string){
    if(name.length >= 3){
      this.searchByName(name);
      this.isEmptyPage = true;
    } else if(name.length === 0 && this.isEmptyPage){
      this.fetchPageOfPayments();
    }
  }

  // search by name
  searchByName(username:string){
    this.paymentsService.searchPayment({
      'username':username
    }).subscribe({
      next:(res:PaymentResponse[])=>{
        this.paymentList = res;
      },
      error:(err)=>{
        console.log(err);
      }
    },
    );
  }

  // on filtered ekub
  onFilterByEkub(ekubId:string){
    this.ekubFilter = ekubId;
    this.fetchPageOfPayments();
  }

  // on filtered by date
  onDateTimeFilter(dateTime:string){
    this.dateTimeFilter = dateTime;
    this.fetchPageOfPayments();
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
