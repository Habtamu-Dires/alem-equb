import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { EkubComponent } from './pages/ekub/ekub.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { UserComponent } from './pages/user/user.component';
import { ManageEkubComponent } from './pages/manage-ekub/manage-ekub.component';
import { ManageUserComponent } from './pages/manage-user/manage-user.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { ViewEkubDetailComponent } from './pages/view-ekub-detail/view-ekub-detail.component';
import { ViewUserDetailComponent } from './pages/view-user-detail/view-user-detail.component';

const routes: Routes = [
  {
    path:'', 
    component: HomeComponent ,
    children:[
      {path:'', redirectTo:'dashboard', pathMatch:'full'},
      {path:'dashboard', component: DashboardComponent},
      {path:'ekubs', component:EkubComponent},
      {path:'ekubs/manage', component:ManageEkubComponent},
      {path:'ekubs/manage/:ekubId', component:ManageEkubComponent},
      {path:'ekubs/:ekubId', component:ViewEkubDetailComponent},
      {path:'users', component:UserComponent}, 
      {path:'users/manage', component:ManageUserComponent},
      {path:'users/manage/:userId', component:ManageUserComponent},
      {path:'users/:userId', component:ViewUserDetailComponent}, 
      {path:'payments', component:PaymentComponent}, 

    ]},
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
