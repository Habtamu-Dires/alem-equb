import { Component, HostListener } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { EkubResponse, UserResponse } from '../../../../services/models';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UsersService } from '../../../../services/services';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { DrawerComponent } from "../../components/drawer/drawer.component";
import { Router } from '@angular/router';
import { EkubsComponent } from "../ekubs/ekubs.component";
import { PaymentHistoryComponent } from '../payment-history/payment-history.component';
import { UxService } from '../../../../services/ux-service/ux.service';
import { PaymentComponent } from '../payment/payment.component';

@Component({
  selector: 'app-home',
  imports: [HeaderComponent, CommonModule, DrawerComponent, EkubsComponent, 
      PaymentHistoryComponent, PaymentComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  loggedUser: UserProfile | undefined;
  user:UserResponse | undefined;
  ekubs:EkubResponse[] = [];
  onMobileView:boolean = false;
  showDrawer:boolean = false;
  selectedPage:string = 'ekubs';

  constructor(
    private keycloakService: KeycloakService,
    private usersService: UsersService,
    private toastrService: ToastrService,
    private router: Router,
    private uxService:UxService,
  ){

  }
  ngOnInit(){
    this.checkScreenSzie(window.innerWidth);
    this.loggedUser = this.keycloakService.profile;
    if(this.loggedUser?.id){
      this.fetchUserById(this.loggedUser.id);
    }

    //drawer status
    this.getDrawerStatus();
    //selected page
    this.getSelectedPage();
  }

  //fetch users by id
  fetchUserById(userId:string){
    this.usersService.getUserById({
      'user-id': userId
    }).subscribe({
      next:(res:UserResponse) => {
        this.user = res;
      },
      error:(err) => {
        console.log(err);
        this.toastrService.error('Failed to fetch user info', 'Ooops');
      }
    })
  }


  //show drawer service
  getDrawerStatus(){
    this.uxService.showDrawer$.subscribe((value:boolean)=>{
      this.showDrawer = value;
    })
  }

  //selected Page during page size change
  getSelectedPage(){
    this.uxService.selectedPage$.subscribe((value:string)=>{
      this.selectedPage = value;
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
      this.showDrawer = false;
      this.uxService.updateShowDrawerStatus(false);
    }
  }

  //openDrawer
  openDrawer(){
    this.showDrawer = true;
    this.uxService.updateShowDrawerStatus(true);
  }
  //close drawer
  closeDrawer(){
    this.showDrawer = false;
    this.uxService.updateShowDrawerStatus(false);
  }

  // navigate to
  selectPage(page:string){
    this.uxService.updateSelectedPage(page);
    if(this.onMobileView){
      this.router.navigate(['member', page]);
    } else {
      this.selectedPage = page;
    }
  }

}
