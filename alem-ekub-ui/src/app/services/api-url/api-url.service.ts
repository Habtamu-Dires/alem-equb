import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';

declare var window: any;

@Injectable({
  providedIn: 'root'
})
export class ApiUrlService {

  // _rootUrl: string = '';

  constructor() { }

  async initialize(){
    console.log('API_URL:', window.API_URL);
    if(window.API_URL && window.API_URL !== '__API_URL_PLACEHOLDER__'){
      environment.apiUrl = window.API_URL || 'http://localhost:8088/api/v1';
    } else {
      environment.apiUrl = 'http://localhost:8088/api/v1';
    }

    if(window.KEYCLOAK_URL && window.KEYCLOAK_URL !== '__KEYCLOAK_URL_PLACEHOLDER__'){
      environment.keycloakUrl = window.KEYCLOAK_URL || 'http://host.docker.internal:9090';
      console.log("hello ----", environment.keycloakUrl);
    } else {  
      environment.keycloakUrl = 'http://host.docker.internal:9090';
    }

    if(window.REALM && window.REALM !== '__REALM_PLACEHOLDER__'){
      environment.realm = window.REALM || 'alem-ekub';
    } else {
      environment.realm = 'alem-ekub';
    }

    if(window.CLIENT_ID && window.CLIENT_ID !== '__CLIENT_ID_PLACEHOLDER__'){
      environment.clientId = window.CLIENT_ID || 'ekub-user-login';
    } else {
      environment.clientId = 'ekub-user-login';
    }

    if(window.REDIRECT_URL && window.REDIRECT_URL !== '__REDIRECT_URL_PLACEHOLDER__'){
      environment.redirectUrl = window.REDIRECT_URL || 'http://localhost:4200';
    } else {
      environment.redirectUrl = 'http://localhost:4200';
    }

    console.log('API rootUrl set to:', window.API_URL);
  }

}
