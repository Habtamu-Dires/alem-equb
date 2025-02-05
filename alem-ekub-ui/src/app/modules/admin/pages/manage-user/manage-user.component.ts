import { Component, HostListener, OnInit } from '@angular/core';
import { EkubResponse, IdResponse, UserRequest, UserResponse } from '../../../../services/models';
import { EkubsService, EkubUsersService, RegistrationService, UsersService } from '../../../../services/services';
import { CommonModule } from '@angular/common';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, throttleTime } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../components/confirm-dialog/confirm-dialog.component';
import { ImageViewerComponent } from "../../components/image-viewer/image-viewer.component";
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-manage-user',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './manage-user.component.html',
  styleUrl: './manage-user.component.scss'
})
export class ManageUserComponent implements OnInit{
    
  userRequest:UserRequest={
    username:'',
    password:'',
    firstName:'',
    lastName:'',
    phoneNumber:'',
    profession:'',
    ekubIds:[]
  };
  errMsgs:Array<string> = [];
  showPassConfError:boolean = false;
  showPassMandatoryError:boolean = false;
  showPassLenghtError:boolean = false;
  confirmPassword = new FormControl('');
  passwordControl = new FormControl('');
  ekubControl= new FormControl('');
  showEkubs:boolean = true;
  ekubList:EkubResponse[] = [];
  ekubIdNameMap = new Map<string,string>();
  selectedProfilePic:any;
  selectedPictureString:string | undefined;
  selectedIdCardImage:any;
  selectedIdCardImageString:string | undefined;

  constructor(
    private usersService:UsersService,
    private ekubsService:EkubsService,
    private ekubUsersService:EkubUsersService,
    private activatedRoute:ActivatedRoute,
    private router:Router,
    private toastrService:ToastrService,
    private matDialog:MatDialog,
    private registrationService:RegistrationService
  ){}

  ngOnInit(): void {
    const userId = this.activatedRoute.snapshot.params['userId'];
    if(userId){
      this.fetchUserById(userId);
    }
    // password form control
    this.confirmPasswordControl();
    this.passwordFormControl();
    //ekub seach control
    this.ekubSearchControl();
  }

  // create user
  createUser(){
    this.usersService.createUser({
      body: {
        request: this.userRequest,
        profilePic: this.selectedProfilePic,
        idCardImg: this.selectedIdCardImage
      }
    }).subscribe({
      next:(res:IdResponse)=>{
      
        this.router.navigate(['admin','users']);
        this.toastrService.success('User saved successfully ', 'Done!')
        
      },
      error:(err:HttpErrorResponse)=>{
        this.toastrService.error(err.error.error, 'Ooops');
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

  // update user
  updateUser(){
    this.usersService.updateUser({
      body:this.userRequest
    }).subscribe({
      next:(res:IdResponse)=>{
        //upload  profile picture
        if(this.selectedProfilePic) {
          this.uploadProfilePic(res.id as string);
        } 
        // upload id card image
        if(this.selectedIdCardImage) {
          this.uploadIdCardImg(res.id as string);
        }
        this.router.navigate(['admin','users']);
        this.toastrService.success('User updated successfully ', 'Done!')
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

  // upload profile picture
  uploadProfilePic(id:string){
    this.usersService.uploadProfilePicture({
      'user-id' : id,
      body : {
        file: this.selectedProfilePic
      }
    }).subscribe({
      next:() => {
        
      },
      error:(err) => {
        console.log(err);
        this.toastrService.error("Couldn't upload profile picture", 'Ooops');            
      }
    })
  }

  // upload id card image
  uploadIdCardImg(id:string){
    this.usersService.uploadIdCardImage({
      'user-id' : id,
      body : {
        file: this.selectedIdCardImage
      }
    }).subscribe({
      next:() => {
        
      },
      error:(err) => {
        console.log(err);
        this.toastrService.error("Couldnt upload id card image", 'Ooops');            
      }
    })
  }

  // fetch user by id
  fetchUserById(userId:string){
    this.usersService.getUserById({
      "user-id": userId
    }).subscribe({
      next:(res:UserResponse)=>{
        this.userRequest={
          id:res.id,
          username: res.username as string,
          firstName: res.firstname as string,
          lastName: res.lastname as string,
          phoneNumber: res.phoneNumber as string,
          email: res.email as string,
          profession: res.profession as string,
          ekubIds: res.ekubIds,
          enabled: res.enabled,
          remark:res.remark
        }
        //profile pic
        if(res.profilePicUrl != undefined && res.profilePicUrl.length > 0){ 
          this.selectedPictureString = res.profilePicUrl;
        }
        //id card image
        if(res.idCardImageUrl != undefined && res.idCardImageUrl.length > 0){ 
          this.selectedIdCardImageString = res.idCardImageUrl;
        }
        // map ekubId wiht ekubName
        const ekubIdList = this.userRequest.ekubIds;
        const ekubNameList = res.ekubs;
        if(ekubIdList && ekubNameList){
          for (let i = 0; i < ekubIdList.length; i++) {
            this.ekubIdNameMap.set(ekubIdList[i],ekubNameList[i]);
          }
        }
        
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //ekub search control
  ekubSearchControl(){
    this.ekubControl.valueChanges
    .pipe(
      debounceTime(300),
      throttleTime(500)
    ).subscribe((value:any)=>{
      const text = value as string;
      if(text.length >= 3){
        this.searchEkubByName(text);
        this.showEkubs = true;
      } else {
        this.showEkubs = false;
      }
    })
  }

  //serach ekub by name 
  searchEkubByName(ekubName:string){
    this.ekubsService.searchEkubByName({
      'ekub-name':  ekubName
    }).subscribe({
      next:(res:EkubResponse[])=>{
        this.ekubList = res;
      },
      error:(err) => {
        console.log(err);
      }
    })
  }

  // on ekbu selected
  onEkubSelected(ekub:EkubResponse){
    // if on update
    if(this.userRequest.id){
      this.addUserToEkub(ekub);
    } else { // on create
      const ekubId = ekub.id as string;
      if(this.userRequest.ekubIds && !this.userRequest.ekubIds?.includes(ekubId)){
        this.userRequest.ekubIds.push(ekubId);
        this.ekubIdNameMap.set(ekubId,ekub.name as string)
      } 
    }
  }

  // add use to a ekub
  addUserToEkub(ekub:EkubResponse){
    const userId = this.userRequest.id;
    const ekubId = ekub.id;
    if(userId && ekubId){
      this.ekubUsersService.addEkubUser({
        'ekub_id': ekubId,
        'user_id': userId
      }).subscribe({
        next:()=>{
          if(this.userRequest.ekubIds && !this.userRequest.ekubIds?.includes(ekubId)){
            this.userRequest.ekubIds.push(ekubId);
            this.ekubIdNameMap.set(ekubId,ekub.name as string)
          } 
          this.toastrService.success('Successfully add use to ekub ', ekub.name);
        }, 
        error:(err)=>{
          console.log(err);
          this.toastrService.error('Something went wrong', 'Ooops');
        }
      })
    }
    
  }


  // remove ekub
  removeEkub(ekbuId:any){
     // if it is on update
     if(this.userRequest.id){
       const dialog = this.matDialog.open(ConfirmDialogComponent);
           
           dialog.afterClosed().subscribe(result =>{
             if(result){
               // call remove ekub from user api
               this.removeUserFromEkub(ekbuId);
             }
           })
     } else {
      // on create new
      const ekubIds = this.userRequest.ekubIds;
      this.userRequest.ekubIds = ekubIds?.filter(ids => ids !== ekbuId as string)
      this.ekubIdNameMap.delete(ekbuId);
     }
  }

  //remove user form ekub
  removeUserFromEkub(ekubId:string){
    const userId = this.userRequest.id;
    if(userId){
      this.ekubUsersService.removeUser({
        'ekub_id': ekubId,
        'user_id': userId
      }).subscribe({
        next:()=>{
          // remove from display
          const ekubIds = this.userRequest.ekubIds;
          this.userRequest.ekubIds = ekubIds?.filter(ids => ids !== ekubId as string)
          this.ekubIdNameMap.delete(ekubId);
          
          this.toastrService.success("Successfully Remove Ekub","Done");
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
    
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
      debounceTime(1000)
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
    if(this.userRequest.id){
      this.updateUser();
    }else if(!this.userRequest.password ||  this.userRequest.password.length < 4){
      this.showPassMandatoryError = true;
    } else if(!this.showPassConfError) {
      this.createUser();
    }
  }

  // on cancel
  onCancel(){
    this.router.navigate(['admin','users']);
  }

  //file methods
  //onfile selected
  onFileSelected(event:any,type:string){
    if(type ==='profilePic'){
      this.selectedProfilePic = event.target.files[0];
      if(this.selectedProfilePic != null){
        const reader = new FileReader();
        reader.onload = () => {
          this.selectedPictureString = reader.result as string;
        }
        reader.readAsDataURL(this.selectedProfilePic)
      }
    } else if(type==='idCardImg') {
      this.selectedIdCardImage = event.target.files[0];
      if(this.selectedIdCardImage != null){
        const reader = new FileReader();
        reader.onload = () => {
          this.selectedIdCardImageString = reader.result as string;
        }
        reader.readAsDataURL(this.selectedIdCardImage)
      }
    }
    
  }


  openImageViewer(){
    const dialog = this.matDialog.open(ImageViewerComponent,{
      data:{imageString: this.selectedIdCardImageString},
      hasBackdrop: true,
      backdropClass: 'backdrop'
    });
  }

  // Track clicks to hide the dropdown 
  @HostListener('document:click', ['$event'])
  onClickOutSideDropdown(event: MouseEvent){
    const target = event.target as HTMLElement;
    // only hide if the click is outside the component
    if(!target.classList.contains('donthide')){
      this.showEkubs = false;
      this.ekubControl.setValue('');
    }
  }  
}

