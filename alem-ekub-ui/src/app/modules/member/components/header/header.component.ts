import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { KeycloakService } from '../../../../services/keycloak/keycloak.service';
import { UserProfile } from '../../../../services/keycloak/user-profile';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DrawerItemComponent } from "../drawer-item/drawer-item.component";
import { UxService } from '../../../../services/ux-service/ux.service';

@Component({
  selector: 'app-header',
  imports: [CommonModule, DrawerItemComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{

  @Input() onMobileView:boolean= false;
  @Input() onSubPage:boolean = false;
  @Input() pageName:string | undefined;
  @Input() onHomePage:boolean = false;
  loggedUser: UserProfile | undefined;
  showActions: boolean = false;
  @Output() onOpenDrawer:EventEmitter<boolean> = new EventEmitter<boolean>();
  selectedItem: string = 'Home';
  

  constructor(
    private keycloakService: KeycloakService,
    private router: Router,
    private uxService:UxService
  ) { }

  //fetch logged user
  getLoggedUser() {
    this.loggedUser = this.keycloakService.profile;
  }

  ngOnInit(): void {
    this.getLoggedUser();
    this.getHeadersSelectedItem();
  }

  profile(){
    this.router.navigate(['member','profile']);
    // this.keycloakService.accountManagement();
  }

  // toggle show actions
  toggelShowActions(){
    this.showActions = true;
  }

  // logout
  logout() {
    this.keycloakService.logout();
  }

  home(){
    this.router.navigate(['member','home']);
  }

  // hide delete & edit btn onclick outside the btn
  @HostListener('document:click', ['$event'])
  hideDeleteBtn(event: MouseEvent){
    const target = event.target as HTMLElement;
    if(!target.classList.contains('donthide')){
      this.showActions = false;
    }
  }

  // open drawer
  openDrawer(){
    this.onOpenDrawer.emit(true);
  }

  //back
  back(){
    window.history.back();
  }

  // big screen menu
  selectItem(item:string){
    this.selectedItem=item;
    this.uxService.updateHeadersSelectedItem(item);
    this.router.navigate(['member', item.toLocaleLowerCase()]);
  }

  //get selected item
  getHeadersSelectedItem(){
    this.uxService.headersSelectedItem$.subscribe((item:string)=>{
      this.selectedItem = item;
    })
  }

  

}
