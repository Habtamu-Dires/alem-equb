import { Component, HostListener, OnInit } from '@angular/core';
import { UsersService } from '../../../../services/services';
import { UserRequest, UserResponse } from '../../../../services/models';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ImageViewerComponent } from '../../../admin/components/image-viewer/image-viewer.component';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { HeaderComponent } from "../../components/header/header.component";

@Component({
  selector: 'app-personal-info',
  imports: [CommonModule, FormsModule, HeaderComponent],
  templateUrl: './personal-info.component.html',
  styleUrl: './personal-info.component.scss'
})
export class PersonalInfoComponent implements OnInit {

  userRequest:UserRequest ={
    username:'',
    password:'',
    firstName:'',
    lastName:'',
    phoneNumber:'',
    profession:'',
    ekubIds:[]
  };
  loggedUser: UserProfile | undefined;
  errMsgs:Array<string> = [];
  selectedProfilePic:any;
  selectedPictureString:string | undefined;
  selectedIdCardImage:any;
  selectedIdCardImageString:string | undefined;
  onMobileView:boolean = true;

  constructor(
    private usersService: UsersService,
    private keycloakService: KeycloakService,
    private matDialog:MatDialog,
    private toastrService:ToastrService,
  ){}

  ngOnInit(){
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserById(this.loggedUser.id);
    }
    //check screen size
    this.checkScreenSzie(window.innerWidth);
  }

  //fetch users by id
  fetchUserById(userId:string){
    this.usersService.getUserById({
      'user-id': userId
    }).subscribe({
      next:(res:UserResponse) => {
        this.userRequest={
          id:res.id,
          username: res.username as string,
          firstName: res.firstname as string,
          lastName: res.lastname as string,
          phoneNumber: res.phoneNumber as string,
          email: res.email as string,
          profession: res.profession as string,
          ekubIds: res.ekubIdList,
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
      },
      error:(err)=>{
        console.log(err);
        this.toastrService.error(err.error.error, 'Ooops');
      }
    })
  }

  // update user 
  updateProfile(){
      this.usersService.updateProfile({
        body:this.userRequest
      }).subscribe({
        next:(res)=>{
          this.toastrService.success('User updated successfully ', 'Done!')
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

  onSave(){
    this.updateProfile();
  }

  openImageViewer(){
      const dialog = this.matDialog.open(ImageViewerComponent,{
        data:{imageString: this.selectedIdCardImageString},
        hasBackdrop: true,
        backdropClass: 'backdrop'
      });
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
