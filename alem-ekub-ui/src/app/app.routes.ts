import { Routes } from '@angular/router';
import { authGuard } from './services/guard/auth.guard';
import { RegistrationComponent } from './components/registration/registration.component';

export const routes: Routes = [
    {path: '', redirectTo: 'member', pathMatch: 'full'},
    {path: 'member', 
        loadChildren: () => import('./modules/member/member.module')
            .then(m => m.MemberModule),
            canLoad:[authGuard],
            canActivate:[authGuard],
            canActivateChild:[authGuard]
    },
    {path: 'admin', 
        loadChildren: () => import('./modules/admin/admin.module')
            .then(m => m.AdminModule),
            canLoad:[authGuard],
            canActivate:[authGuard],
            canActivateChild:[authGuard]
    },
    { path: 'registration',component:RegistrationComponent }
];
