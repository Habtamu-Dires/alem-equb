import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { KeycloakService } from '../keycloak/keycloak.service';

export const authGuard: CanActivateFn = (route, state) => {
  
  const keycloakService = inject(KeycloakService);
  
  if(!keycloakService.isTokenValid){
    keycloakService.logout();
    console.log("expired")
    return false;
  }

  return true;

};
