import { Routes } from '@angular/router';

export const routes: Routes = [
    {path: 'admin', 
        loadChildren: () => import('./modules/admin/admin.module')
            .then(m => m.AdminModule),
    },
    {path: 'member', 
        loadChildren: () => import('./modules/member/member.module')
            .then(m => m.MemberModule),
    }
];
