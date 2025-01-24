import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { DrawerItemComponent } from "../drawer-item/drawer-item.component";
import { Router } from '@angular/router';
import { UxService } from '../../../../services/ux-service/ux.service';

@Component({
  selector: 'app-drawer',
  imports: [DrawerItemComponent],
  templateUrl: './drawer.component.html',
  styleUrl: './drawer.component.scss'
})
export class DrawerComponent implements OnInit{

  loggedUser: UserProfile | undefined;
  selectedItem: string = '';

  constructor(
    private keycloakService: KeycloakService,
    private router:Router,
    private uxService:UxService
  ) { }

  ngOnInit(): void {
    this.loggedUser = this.keycloakService.profile;
  }

  selectItem(item:string){
    this.uxService.updateHeadersSelectedItem(item);
    this.router.navigate(['member', item.toLocaleLowerCase()]);
  }

  logout() {
    this.keycloakService.logout();
  }
  
}
