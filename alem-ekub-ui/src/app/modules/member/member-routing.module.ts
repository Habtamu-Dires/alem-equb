import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { MainComponent } from './main/main.component';
import { PersonalInfoComponent } from './pages/personal-info/personal-info.component';
import { ChangePasswordComponent } from './pages/change-password/change-password.component';
import { AboutComponent } from './pages/about/about.component';
import { EkubsComponent } from './pages/ekubs/ekubs.component';
import { EkubComponent } from './components/ekub/ekub.component';
import { EkubDetailComponent } from './pages/ekub-detail/ekub-detail.component';

const routes: Routes = [
  {path:'',component:MainComponent,
  children:[
    {path:'',redirectTo:'home',pathMatch:'full'},
    {path:'home',component:HomeComponent},
    {path:'personal-info',component:PersonalInfoComponent},
    {path:'change-pin',component:ChangePasswordComponent},
    {path:'about',component:AboutComponent},
    {path:'ekubs',component:EkubsComponent},
    {path:'ekub-detail/:ekubId',component:EkubDetailComponent}
  ]
 }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MemberRoutingModule { }
