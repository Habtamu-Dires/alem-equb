import { ApplicationConfig, inject, Injector, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {provideToastr} from 'ngx-toastr';
import { routes } from './app.routes';
import { KeycloakService } from './services/keycloak/keycloak.service';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { httpInterceptor } from './services/interceptor/http.interceptor';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(
      withInterceptors([httpInterceptor])
    ),
    provideRouter(routes),
    provideAppInitializer(() => {
      const injector = inject(Injector);
      const kcService = injector.get(KeycloakService);
      return kcService.init();
    }),
    provideAnimations(),
    provideAnimationsAsync(),
    provideToastr({
      progressBar:true, 
      closeButton: true,
      newestOnTop: true,
      tapToDismiss: true,
      positionClass: 'toast-top-center',
      timeOut: 2000
    }),
    ]
};
