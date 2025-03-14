import { Component, HostListener, Input, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { CommonModule } from '@angular/common';
import { EkubResponse } from '../../../../services/models';
import { ToastrService } from 'ngx-toastr';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { EkubComponent } from "../../components/ekub/ekub.component";
import { EkubsService, EkubUsersService } from '../../../../services/services';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-ekubs',
  imports: [HeaderComponent, CommonModule, EkubComponent],
  templateUrl: './ekubs.component.html',
  styleUrl: './ekubs.component.scss'
})
export class EkubsComponent implements OnInit{

  onMobileView:boolean= true;
  ekubs:EkubResponse[] = [];
  loggedUser:UserProfile | undefined;

  constructor(
    private ekubsService:EkubsService,
    private toastrService: ToastrService,
    private keycloakService:KeycloakService,
    private router:Router,
    private dialog:MatDialog
  ) { }

  ngOnInit(): void {
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserEkubs(this.loggedUser.id);
    }
  }

  // fetch users ekbu
  fetchUserEkubs(userId:string){
    this.ekubsService.getEkubsOfUser({
      'user-id': userId
    }).subscribe({
      next:(res:EkubResponse[])=>{
        this.ekubs = res;
      },
      error:(err)=>{
        console.log(err);
        this.toastrService.error('Failed to fetch user ekub', 'Ooops');
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
