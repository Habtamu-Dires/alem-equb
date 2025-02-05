import { Routes } from '@angular/router';
import { authGuard } from './services/guard/auth.guard';
import { RegistrationComponent } from './components/registration/registration.component';

export const routes: Routes = [
    {path: 'admin', 
        loadChildren: () => import('./modules/admin/admin.module')
            .then(m => m.AdminModule),
            canActivate:[authGuard],
            canActivateChild:[authGuard]
    },
    {path: 'member', 
        loadChildren: () => import('./modules/member/member.module')
            .then(m => m.MemberModule),
            canActivate:[authGuard],
            canActivateChild:[authGuard]
    },
    { path: 'registration',component:RegistrationComponent }
];
