import { Component, HostListener, OnInit } from '@angular/core';
import { UserRequest } from '../../services/models';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { debounceTime } from 'rxjs';
import { CommonModule } from '@angular/common';
import { KeycloakService } from '../../services/keycloak/keycloak.service';
import { RegistrationService } from '../../services/services';
import imageCompression from 'browser-image-compression';

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
    selectedIdCardImage:any;
    selectedIdCardImageString:string | undefined;
    showIdCardImgErr:boolean = false;
  
    constructor(
      private toastrService:ToastrService,
      private keycloakService:KeycloakService,
      private registrationService:RegistrationService
    ){}
  
    ngOnInit(): void {
      // password form control
      this.passwordFormControl();
      this.confirmPasswordControl();
    }
  
    // create user
    register(){
      this.registrationService.register({
        body: {
          request: this.userRequest,
          idCardImg: this.selectedIdCardImage
        }
      }).subscribe({
        next:()=>{
          this.toastrService.success('Successfull Registration ', 'Done!')
          setTimeout(()=>{
            this.keycloakService.login();
          },500);
          
        },
        error:(err:HttpErrorResponse)=>{
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
        debounceTime(3000)
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
        debounceTime(3000)
      ).subscribe((value:any)=>{
        const password = value as string;
        if(password.length >= 4){
          if(password !== this.userRequest.password){
            this.showPassConfError = true;
          } else{
            this.showPassConfError = false;
          }
        } else {
          this.showPassConfError = true;
        }
      })
    }
  
    // on save
    onSave(){
      if(!this.userRequest.password ||  this.userRequest.password.length < 4){
        this.showPassMandatoryError = true;
      } else if(!this.selectedIdCardImage) {
        this.showIdCardImgErr = true;
      }
      else if(!this.showPassConfError) {
        this.register();
      }
    }
  
    // on cancel
    onCancel(){
      this.keycloakService.login();
    }
  
    //file methods
    //onfile selected
    onFileSelected(event:any){
        const file = event.target.files[0];
        if(file){
          const reader = new FileReader();
          reader.onload = () => {
            this.selectedIdCardImageString = reader.result as string;
            const options = { 
              maxSizeMB: 0.5, 
              maxWidthOrHeight: 1024, 
              useWebWorker: true, 
              maxIteration: 3 
            };

            imageCompression(file, options).then((compressedFile) => {
              this.selectedIdCardImage = compressedFile; 
            });
          }
          reader.readAsDataURL(file);
          this.showIdCardImgErr = false;

        }
      
    }




}
