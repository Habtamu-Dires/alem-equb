import { Component, HostListener, OnInit } from '@angular/core';
import { KeycloakService } from '../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../services/keycloak/user-profile';
import { CommonModule } from '@angular/common';
import { SideBarItemComponent } from "../components/side-bar-item/side-bar-item.component";
import { Router, RouterOutlet } from '@angular/router';
import { AdminUxService } from '../services/admin-ux/admin-ux.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, SideBarItemComponent,RouterOutlet],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  profile:UserProfile | undefined;
  onShawDrawer:boolean = false;
  activeComponent:string = 'Ekubs';
  // onMobileView:boolean = false;

  constructor(
    private keycloakService:KeycloakService,
    private router:Router,
    private adminUxService:AdminUxService
  ){}

  ngOnInit(): void {
    this.setActiveComponent('Ekubs');
    this.profile = this.keycloakService.profile;
    // get show drawer 
    this.adminUxService.showDrawer$.subscribe((onShawDrawer:boolean)=>{
      this.onShawDrawer = onShawDrawer;
    });
  }

  // logout
  logout(){
    this.keycloakService.logout();
  }

  // toggle side bar
  toggleSideBar() {
    this.onShawDrawer = !this.onShawDrawer;
    this.updateShowDrawer(this.onShawDrawer);
  }

  // set active component
  setActiveComponent(component:string){
    this.activeComponent = component;
    this.router.navigate(['admin', component.toLocaleLowerCase()])
  }

  // update show drawer
  updateShowDrawer(show:boolean){
    this.adminUxService.updateShowDrawerStatus(show);
  }

}
