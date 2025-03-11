import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { EkubsService, EkubUsersService, UsersService } from '../../../../services/services';
import { EkubResponse } from '../../../../services/models';
import { EkubComponent } from "../../components/ekub/ekub.component";
import { MatDialog } from '@angular/material/dialog';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { HeaderComponent } from '../../components/header/header.component';
import { Router } from '@angular/router';
import { ConfirmationDialogComponent } from '../../../../components/confirmation-dialog/confirmation-dialog.component';
import { UxService } from '../../../../services/ux-service/ux.service';


@Component({
  selector: 'app-new-ekubs',
  imports: [CommonModule, EkubComponent,HeaderComponent],
  templateUrl: './new-ekubs.component.html',
  styleUrl: './new-ekubs.component.scss'
})
export class NewEkubsComponent implements OnInit{

  onMobileView:boolean= true;
  ekubs:EkubResponse[] = [];
  selectedCategory:string = 'public';
  loggedUser:UserProfile | undefined;

  constructor(
    private ekubsService: EkubsService,
    private ekubUsersService:EkubUsersService,
    private dialog: MatDialog,
    private router:Router,
    private uxService:UxService
  ){}

  ngOnInit(): void {
   this.checkScreenSzie(window.innerWidth);
   //get selected category
   this.uxService.selectedNewEkubCategory$.subscribe((value:string)=>{
      this.selectedCategory = value;
      if(value === 'public'){
        this.fetchPublicEkubs();
      } else if(value === 'invited'){
        this.fetchInvitedEkubs();
      }
    })
  }

  //fetch public ekubs
  fetchPublicEkubs(){
    this.ekubsService.getPublicEkubs()
    .subscribe({
      next:(res:EkubResponse[])=>{
        this.ekubs = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  //fetch invited ekbus
  fetchInvitedEkubs(){
    this.ekubsService.getInvitedEkubsYetToJoin()
    .subscribe({
      next:(res:EkubResponse[])=>{
        this.ekubs = res;
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // join ekub
  joinEkub(ekubId:string){
    this.ekubUsersService.joinEkub({
      'ekub-id': ekubId
    }).subscribe({
      next:()=>{
        if(this.selectedCategory === 'public'){
          this.fetchPublicEkubs();
        } else if(this.selectedCategory === 'invited'){
          this.fetchInvitedEkubs();
        }
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // selected category
  selectCategory(category:string){
    this.selectedCategory = category;
    this.uxService.updateSelectedNewEkubCategory(category);
    if(category === 'public'){
      this.fetchPublicEkubs();
    } else if(category === 'invited'){
      this.fetchInvitedEkubs();
    }
  }

  // on join ekbu
  onJoinEkub(ekubId:string){
    //open dialog
    const dialogRef = this.dialog.open(ConfirmationDialogComponent,{
      width: '400px',
      data:{
        message: 'You want to join this ekub',
        buttonName: 'Join',
        isWarning: false
      }
    });
    // dialog result
    dialogRef.afterClosed().subscribe(result =>{
      if(result){
        this.joinEkub(ekubId);
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
        this.router.navigate(['member','home']);
      }
    }

}
