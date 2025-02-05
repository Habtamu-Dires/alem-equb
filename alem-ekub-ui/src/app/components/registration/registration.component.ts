import { Component, HostListener, OnInit } from '@angular/core';
import { IdResponse, UserRequest } from '../../services/models';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { debounceTime } from 'rxjs';
import { CommonModule } from '@angular/common';
import { KeycloakService } from '../../services/keycloak/keycloak.service';
import { RegistrationService } from '../../services/services';

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule,CommonModule,FormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.scss'
})
export class RegistrationComponent implements OnInit{
  
   userRequest:UserRequest={
      username:'',
      password:'',
      firstName:'',
      lastName:'',
      phoneNumber:'',
      profession:'',
      enabled:false
    };
    errMsgs:Array<string> = [];
    showPassConfError:boolean = false;
    showPassMandatoryError:boolean = false;
    showPassLenghtError:boolean = false;
    confirmPassword = new FormControl('');
    passwordControl = new FormControl('');
    ekubIdNameMap = new Map<string,string>();
    selectedProfilePic:any;
    selectedPictureString:string | undefined;
    selectedIdCardImage:any;
    selectedIdCardImageString:string | undefined;
    showProfilePicErr:boolean = false;
    showIdCardImgErr:boolean = false;
  
    constructor(
      private toastrService:ToastrService,
      private keycloakService:KeycloakService,
      private registrationService:RegistrationService
    ){}
  
    ngOnInit(): void {
      // password form control
      this.confirmPasswordControl();
      this.passwordFormControl();
    }
  
    // create user
    register(){
      
      this.registrationService.register({
        body: {
          request: this.userRequest,
          profilePic: this.selectedProfilePic,
          idCardImg: this.selectedIdCardImage
        }
      }).subscribe({
        next:()=>{
          // //upload  profile picture
          // if(this.selectedProfilePic) {
          //   this.uploadProfilePic(res.id as string);
          // } 
          // // upload id card image
          // if(this.selectedIdCardImage) {
          //   this.uploadIdCardImg(res.id as string);
          // }
  
          this.toastrService.success('Successfull Registration ', 'Done!')
          setTimeout(()=>{
            this.keycloakService.login();
          },500);
          
        },
        error:(err:HttpErrorResponse)=>{
          this.toastrService.error('Something Went wrong', 'Ooops');
          if(err.error.validationErrors){
            this.errMsgs = err.error.validationErrors;
          } else{
            console.log(err);
            this.toastrService.error(err.error.error, 'Ooops');
          }
          
        }
      })
  
    }

    // password form control
    passwordFormControl(){
      this.passwordControl.valueChanges
      .pipe(
        debounceTime(1000)
      ).subscribe((value:any)=>{
        const password = value as string;
        if(password.length >= 4) {
          this.showPassMandatoryError = false;
          this.showPassLenghtError = false;
          this.userRequest.password = password;
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
          this.showPassMandatoryError =false;
        }
      })
    }
    
    
    //confirm password form control
    confirmPasswordControl(){
      this.confirmPassword.valueChanges
      .pipe(
        debounceTime(1200)
      ).subscribe((value:any)=>{
        const password = value as string;
        if(password.length >= 4){
          if(password !== this.userRequest.password){
            this.showPassConfError = true;
          } else{
            this.showPassConfError = false;
          }
        }
      })
    }
  
    // on save
    onSave(){
      if(!this.userRequest.password ||  this.userRequest.password.length < 4){
        this.showPassMandatoryError = true;
      } else if(!this.selectedProfilePic){
        this.showProfilePicErr = true;
      } else if(!this.selectedIdCardImage) {
        this.showIdCardImgErr = true;
      }
      else if(!this.showPassConfError) {
        // this.registration();
        this.register();
      }
    }
  
    // on cancel
    onCancel(){
      this.keycloakService.login();
    }
  
    //file methods
    //onfile selected
    onFileSelected(event:any,type:string){
      console.log("hello ");
      if(type ==='profilePic'){
        this.selectedProfilePic = event.target.files[0];
        if(this.selectedProfilePic != null){
          const reader = new FileReader();
          reader.onload = () => {
            this.selectedPictureString = reader.result as string;
          }
          reader.readAsDataURL(this.selectedProfilePic)
          this.showProfilePicErr = false;
        }
      } else if(type==='idCardImg') {
        this.selectedIdCardImage = event.target.files[0];
        if(this.selectedIdCardImage != null){
          const reader = new FileReader();
          reader.onload = () => {
            this.selectedIdCardImageString = reader.result as string;
          }
          reader.readAsDataURL(this.selectedIdCardImage);
          this.showIdCardImgErr = false;
        }
      }
      
    }
  
  

}
