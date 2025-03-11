import { Component, HostListener, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { Router } from '@angular/router';
import { EkubsService } from '../../../../services/services';
import { EkubResponse, PageResponseEkubResponse } from '../../../../services/models';
import { CommonModule, DatePipe } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import {ToastrService} from 'ngx-toastr';
import { PaginationComponent } from "../../components/pagination/pagination.component";
import { ViewEkubDetailComponent } from "../view-ekub-detail/view-ekub-detail.component";
import { InvitationDialogComponent } from '../../components/invitation-dialog/invitation-dialog.component';
import { ConfirmationDialogComponent } from '../../../../components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-ekub',
  imports: [HeaderComponent, CommonModule, PaginationComponent, ViewEkubDetailComponent],
  providers:[DatePipe],
  templateUrl: './ekub.component.html',
  styleUrl: './ekub.component.scss'
})
export class EkubComponent implements OnInit {

  ekubList:EkubResponse[] =[];
  selectedEkub:EkubResponse | undefined;
  showActions:boolean = false;
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
    private ekubService:EkubsService,
    private router:Router,
    private datePipe:DatePipe,
    private matDialog:MatDialog,
    private toastrService:ToastrService
  ){}

  ngOnInit(): void {
    this.fetchPageOfEkubs();
  }

  // get List of ekubs
  fetchPageOfEkubs(){
    this.ekubService.getPageOfEkubs({
      'page': this.page,
      'size': this.size
    }).subscribe({
      next:(res:PageResponseEkubResponse) =>{
        this.ekubList = res.content as EkubResponse[];
        // pagination
        this.isEmptyPage = res.empty as boolean;
        this.isFirstPage = res.first;
        this.isLastPage = res.last;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.numberOfElements = res.numberOfElements;
        this.page = res.number as number;
        this.size = res.size as number;
        // console.log(this.ekubList);
      },
      error:(err) => {
        this.toastrService.error('Something went wrong', 'oops');
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


  onCreateNew() {
    this.router.navigate(['admin', 'ekubs', 'manage']);
  }

  onEdit(ekubId:any){
      this.router.navigate(['admin', 'ekubs', 'manage', ekubId as string]);
  }

  // toggle show actions
  onShowActions(ekub:any){
    this.selectedEkub =  ekub;
    this.showActions = true;
  }

  // onView detail
  onViewDetail(ekub:any){
    this.selectedEkub = ekub;
    this.showDetail = true;
  }

  toggleShowDetail(){
    this.showDetail = !this.showDetail;
  }

  onDelete(ekubId:any){
    const dialog = this.matDialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message:'you wants to delete this ekub',
        buttonName: 'delete',
        isWarning: true
      }
    });

    dialog.afterClosed().subscribe(result =>{
      if(result){
        this.ekubService.deleteEkub({
          "ekub-id": ekubId
        }).subscribe({
          next:()=>{
            this.fetchPageOfEkubs();
            this.toastrService.success('EQub deleted successfully','Done');
          },
          error:(err)=>{
            console.log(err);
            this.toastrService.error('Something went wrong', 'oops')
          }
        })
      }
    })
  }

  // on invite user
  onInviteUser(ekubId:any){
    const dialogRef = this.matDialog.open(InvitationDialogComponent,{
      width:'400px',
      data:{
        ekubId: ekubId as string
      }
    });
  }

  // search ekub
  onSearch(text:string){
    if(text.length >= 3){
      this.searchEkub(text);
    } else if(text.length == 0) {
      this.fetchPageOfEkubs();
    }
  }

  // search ekbu by name
  searchEkub(text:string){
    this.ekubService.searchEkubByName({
      'ekub-name': text
    }).subscribe({
      next:(res:EkubResponse[])=>{
        
        this.ekubList = res;
        this.isEmptyPage = true;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //pagination methods
  onPageChanged(page:number){
    this.page = page;
    this.fetchPageOfEkubs();
  }

  onSizeChanged(size:number){
    this.size = size;
    this.fetchPageOfEkubs();
  }

  // hide delete btn onclick outside the btn
  @HostListener('document:click', ['$event'])
  hideDeleteBtn(event: MouseEvent){
    const target = event.target as HTMLElement;
    if(!target.classList.contains('donthide') && this.showActions === true){
      console.log("hiding")
      this.showActions = false;
    }
  }

}
