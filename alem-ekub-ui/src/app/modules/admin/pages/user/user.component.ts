import { Component, HostListener, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { Router } from '@angular/router';
import { PageResponseUserResponse, UserResponse } from '../../../../services/models';
import { CommonModule } from '@angular/common';
import { UsersService } from '../../../../services/services';
import { MatDialog } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';
import { PaginationComponent } from "../../components/pagination/pagination.component";
import { ViewUserDetailComponent } from "../view-user-detail/view-user-detail.component";

@Component({
  selector: 'app-user',
  imports: [HeaderComponent, CommonModule, PaginationComponent, ViewUserDetailComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit{

  userList:UserResponse[] = [];
  selectedUserId:string | undefined;
  showActions:boolean = false;
  showDetail:boolean = false;
  // pagination
  page:number = 0;
  size:number = 5;
  isEmptyPage: boolean = true;
  isFirstPage: boolean |undefined; 
  isLastPage: boolean |undefined;
  totalPages: number | undefined;
  totalElements: number | undefined;
  numberOfElements: number | undefined;

  constructor(
    private usersService:UsersService,
    private router:Router,
    private matDialog:MatDialog,
    private toastrService:ToastrService
  ){}

  ngOnInit(): void {
    this.fetchPageOfUsers();
  }
  //fetch page of users
  fetchPageOfUsers(){
    this.usersService.getPageOfUsers({
      'page': this.page,
      'size': this.size
    }).subscribe({
      next:(res:PageResponseUserResponse)=>{
        this.userList = res.content as UserResponse[];
        // hello
        console.log("Profile Url: " , this.userList[0].profilePicUrl);
        // pagination
        this.isEmptyPage = res.empty as boolean;
        this.isFirstPage = res.first;
        this.isLastPage = res.last;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.numberOfElements = res.numberOfElements;
        this.page = res.number as number;
        this.size = res.size as number;
      },
      error:(err)=>{
        this.toastrService.error('Something went wrong', 'oops')
      }
    })
  }

  onCreateNew() {
    this.router.navigate(['admin','users','manage'])
  }


  onEdit(userId:any){
    this.router.navigate(['admin','users','manage', userId as string]);
  }

  // togel show actions [ edit and delete]
  toggelShowActions(userId:any){
    this.selectedUserId =  userId as string;
    this.showActions = !this.showActions;
  }
  // on view detail
  onViewDetail(userId:any){
    // this.router.navigate(['admin','users', userId as string]);
    this.selectedUserId = userId as string;
    this.showDetail = true;
  }

  toggleShowDetail(){
    this.showDetail = !this.showDetail;
  }



  onDelete(userId:any){
    const dialog = this.matDialog.open(ConfirmDialogComponent);
    
    dialog.afterClosed().subscribe(result =>{
      if(result){
        this.usersService.deleteUser({
          "user-id": userId
        }).subscribe({
          next:()=>{
            this.fetchPageOfUsers();
            this.toastrService.success('User deleted successfully','Done');
          },
          error:(err)=>{
            console.log(err);
            this.toastrService.error('Something went wrong', 'oops')
          }
        })
      }
    })
  }

  // hide delete & edit btn onclick outside the btn
  @HostListener('document:click', ['$event'])
  hideDeleteBtn(event: MouseEvent){
    const target = event.target as HTMLElement;
    if(!target.classList.contains('donthide')){
      this.showActions = false;
    }
  }

  // pagination
  onSizeChanged(size:number){
    this.size=size;
    this.fetchPageOfUsers();
  }

  onPageChanged(page:number){
    this.page=page;
    this.fetchPageOfUsers();
  }



}
