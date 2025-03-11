import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { debounceTime, throttleTime } from 'rxjs';
import { UsersService } from '../../../../services/services';
import { UserResponse } from '../../../../services/models';

@Component({
  selector: 'app-invitation-dialog',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './invitation-dialog.component.html',
  styleUrl: './invitation-dialog.component.scss'
})
export class InvitationDialogComponent implements OnInit{

  ekubId:string | undefined;
  ekubName:string | undefined;
  userControl = new FormControl();
  users:UserResponse[] = [];

  constructor(
    private usersService:UsersService,
    public dialogRef:MatDialogRef<InvitationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      ekubId:string
    }
  ){
    this.ekubId = data.ekubId;
  }

  ngOnInit(): void {
    // user serach control
    this.userSearchControl();
  }

  // search user to invite
  searchUserToInvite(searchTerm:string){
    this.usersService.searchUsersToInvite({
      'ekub-id': this.ekubId as string,
      'search-term': searchTerm
    }).subscribe({
      next:(res:UserResponse[])=>{
       this.users = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // user search form control
  userSearchControl(){
    this.userControl.valueChanges
    .pipe(
      debounceTime(300),
      throttleTime(1000)
    ).subscribe((value:any)=>{
      const searchTerm = value as string;
      if(searchTerm.length >= 3 && this.ekubId){
        this.searchUserToInvite(searchTerm);
      }
    })
  }

  // onInvite
  invite(userId:any){
    this.usersService.inviteUserToEkub({
      'ekub-id': this.ekubId as string,
      'user-id': userId 
    }).subscribe({
      next:()=>{
        this.users = [];
        this.userControl.setValue('');
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // clear ekub search
  clearEkubSearch(){
    this.userControl.setValue('');
    this.users = [];
  }

  // cancel
  cancel(){
    this.dialogRef.close();
  }
}
