import { Component, OnInit } from '@angular/core';
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

}
