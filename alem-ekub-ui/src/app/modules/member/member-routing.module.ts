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
import { PaymentHistoryComponent } from './pages/payment-history/payment-history.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { NewEkubsComponent } from './pages/new-ekubs/new-ekubs.component';

const routes: Routes = [
  {path:'',component:MainComponent,
  children:[
    {path:'',redirectTo:'home',pathMatch:'full'},
    {path:'home',component:HomeComponent},
    {path:'personal-info',component:PersonalInfoComponent},
    {path:'change-pin',component:ChangePasswordComponent},
    {path:'about',component:AboutComponent},
    {path:'ekubs',component:EkubsComponent},
    {path:'ekub-detail/:type',component:EkubDetailComponent},
    {path:'payment', component:PaymentComponent},
    {path:'payment-history', component:PaymentHistoryComponent},
    {path:'new-ekubs', component:NewEkubsComponent}

  ]
 }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MemberRoutingModule { }
