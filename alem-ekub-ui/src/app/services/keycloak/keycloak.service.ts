import { Injectable } from '@angular/core';
import  Keycloak  from 'keycloak-js';
import { UserProfile } from './user-profile';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private _KeyCloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;


  get keycloak(){
    if(!this._KeyCloak){
      this._KeyCloak = new Keycloak({
        url:'http://localhost:9090',
        realm:'alem-ekub',
        clientId:'ekub-user-login'
      })
    }
    return this._KeyCloak;
  } 

  get profile():any{
    return this._profile;
  }

  constructor(private router:Router) { }


  async init(){
    console.log('Authenticating the user .... ');
    const authenticated = await this.keycloak?.init({
         onLoad:'login-required',
    });

    if(authenticated){
      console.log('user authenticated');
      this._profile = (await this.keycloak?.loadUserProfile()) as UserProfile;
      if(this.isAdminUser){
        this.router.navigate(['admin'])
      } else {
        this.router.navigate(['member']);
      }
    }
  }

  login(){
    return this.keycloak?.login();
  }

  logout(){
    return this.keycloak?.logout({
      redirectUri: 'http://localhost:4200'
    });
  }

  // is token valid
  get isTokenValid(){
    return !this.keycloak.isTokenExpired();
  }

  //account management
  accountManagement(){
    this.keycloak.accountManagement();
  }


  // decode the users role
  get isAdminUser():boolean{
    const parsedToken = this.keycloak.tokenParsed;
    console.log("roles ",parsedToken?.realm_access?.roles);
    const roles:string[] = parsedToken?.realm_access?.roles as string[];
    console.log(roles.includes("ADMIN"));

    if(roles.includes('ADMIN')){
      return true;
    }
      return false;
  }
}
