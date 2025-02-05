import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { PasswordUpdateRequest } from '../../../../services/models';
import { debounceTime } from 'rxjs';
import { UsersService } from '../../../../services/services';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { HeaderComponent } from "../../components/header/header.component";

@Component({
  selector: 'app-change-password',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HeaderComponent],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss'
})
export class ChangePasswordComponent implements OnInit {

    passwordUpdateRequest:PasswordUpdateRequest = {
      username: '',
      oldPassword:'',
      newPassword:''
    };
    loggedUser: UserProfile | undefined;
    errMsgs:Array<string> = []; 
    showPassConfError:boolean = false;
    showPassLenghtError:boolean = false;
    confirmPassword = new FormControl('');
    passwordControl = new FormControl('');
    onMobileView:boolean = true;

  constructor(
    private keycloakService: KeycloakService,
    private userService: UsersService,
    private toastrService: ToastrService
  ){}

  ngOnInit(){
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    this.passwordUpdateRequest.username = this.loggedUser?.username as string;
    this.passwordFormControl();
    this.confirmPasswordControl();
  }

  //update self passowrd
  updateSelfPassword(){
    this.userService.updatePassword({
      'user-id': this.loggedUser?.id as string,
      body: this.passwordUpdateRequest
    }).subscribe({
      next:() =>{
        this.toastrService.success('Password updated successfully','Done');
        //clear form
        this.keycloakService.logout();
      },
      error:(err:HttpErrorResponse)=>{
        const errMsg = JSON.parse(err.error);
        if(errMsg.validationErrors){
          this.errMsgs = errMsg.validationErrors;
        } else{
          console.log(err);
          this.toastrService.error(err.error.error, 'Ooops');
        }
      }
    })
  }
  // on save
  onSave(){
    if(!this.showPassConfError && !this.showPassLenghtError){
      this.updateSelfPassword();
    }
  }

  // password form control
    passwordFormControl(){
      this.passwordControl.valueChanges
      .pipe(
        debounceTime(500)
      ).subscribe((value:any)=>{
        const password = value as string;
        if(password.length >= 4) {
          this.showPassLenghtError = false;
          this.passwordUpdateRequest.newPassword = password;
          if(this.confirmPassword.value?.length !== 0 && 
            password !== this.confirmPassword.value )
          {
            this.showPassConfError = true;
          } else {
            this.showPassConfError = false;
          }
        } else {
          this.showPassLenghtError = true;
          this.showPassConfError =false;
        }
      })
    }
  
  
    //confirm password form control
    confirmPasswordControl(){
      this.confirmPassword.valueChanges
      .pipe(
        debounceTime(500)
      ).subscribe((value:any)=>{
        const password = value as string;
        if(password.length >= 4){
          if(password !== this.passwordUpdateRequest.newPassword){
            this.showPassConfError = true;
          } else{
            this.showPassConfError = false;
          }
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
      }
    }  
}
