import { Component, HostListener, OnInit } from '@angular/core';
import { KeycloakService } from '../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../services/keycloak/user-profile';
import { CommonModule } from '@angular/common';
import { SideBarItemComponent } from "../components/side-bar-item/side-bar-item.component";
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule, SideBarItemComponent,RouterOutlet],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  profile:UserProfile | undefined;
  isSideBarHidden:boolean = false;
  activeComponent:string = 'Ekubs';
  onMobileView:boolean = false;

  constructor(private keycloakService:KeycloakService,
    private router:Router
  ){}


  ngOnInit(): void {
    this.setActiveComponent('Ekubs');
    this.profile = this.keycloakService.profile;
  }

  // logout
  logout(){
    this.keycloakService.logout();
  }

  // toggle side bar
  toggleSideBar() {
    this.isSideBarHidden = !this.isSideBarHidden;
  }

  // set active component
  setActiveComponent(component:string){
    this.activeComponent = component;
    this.router.navigate(['admin', component.toLocaleLowerCase()])
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
        this.isSideBarHidden = true;
      }
    }

}
