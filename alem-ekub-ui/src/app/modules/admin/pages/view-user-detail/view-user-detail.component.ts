import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../../../../services/services';
import { UserResponse } from '../../../../services/models';

@Component({
  selector: 'app-view-user-detail',
  imports: [],
  templateUrl: './view-user-detail.component.html',
  styleUrl: './view-user-detail.component.scss'
})
export class ViewUserDetailComponent implements OnInit{

  userId:string | undefined;
  user:UserResponse | undefined;

  constructor(
    private activatedRoute:ActivatedRoute,
    private usersService:UsersService
  ) { }

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['userId'];
    this.fetchUserById();
  }

  fetchUserById(){
    if(this.userId){
      this.usersService.getUserById({
        'user-id': this.userId
      })
      .subscribe({
        next:(res:UserResponse) =>{
          this.user = res;
        }
      })
    }
    
  }

  onCancel(){
    
  }
}
