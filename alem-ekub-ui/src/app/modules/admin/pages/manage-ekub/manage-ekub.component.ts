import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EkubRequest, EkubResponse } from '../../../../services/models';
import { EkubsService } from '../../../../services/services';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService,provideToastr } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-manage-ekub',
  imports: [CommonModule, FormsModule],
  providers:[DatePipe],
  templateUrl: './manage-ekub.component.html',
  styleUrl: './manage-ekub.component.scss'
})
export class ManageEkubComponent implements OnInit{

  ekubResponse:EkubResponse = {};
  ekubRequest:EkubRequest ={
    name: '',
    description:'',
    amount: Number(undefined) ,
    frequencyInDays: Number(undefined),
    type: '',
    isActive: false,
    penaltyPercentPerDay:Number(undefined)
  };
  errMsgs:Array<string> = [];

  constructor(
    private ekubService:EkubsService,
    private router:Router,
    private activatedRoute: ActivatedRoute,
    private toastrService:ToastrService
  ){}

  ngOnInit(): void {
    const ekubId = this.activatedRoute.snapshot.params['ekubId'];
    if(ekubId){
      this.fetchEkubById(ekubId);
    }
  }
  
  // create equb
  createEkub(){
    console.log("The request " + this.ekubRequest);
    this.ekubService.createEkub({
      body: this.ekubRequest
    }).subscribe({
      next:()=>{
        this.toastrService.success('Ekub Created Successfully', 'Done');
        this.router.navigate(['admin', 'ekubs']);
      }, error:(err:HttpErrorResponse) =>{
        const errMsg = JSON.parse(err.error);
        if(errMsg.validationErrors){
          this.errMsgs = errMsg.validationErrors;
        } else{
          console.log(err);
          this.toastrService.error(err.error.error, 'Ooops');
        }
      }
    })
  }

  // get ekub by id
  fetchEkubById(ekubId:string){
    this.ekubService.getEkubById({
      "ekub-id": ekubId
    }).subscribe({
      next:(res:EkubResponse) =>{
        this.ekubResponse = res;
        this.ekubRequest = {
          id: res.id,
          name: res.name as string,
          description: res.description as string,
          amount: res.amount as number,
          type: res.type as string,
          frequencyInDays:res.frequencyInDays as number,
          startDateTime: res.startDateTime ,
          nextDrawDateTime: res.nextDrawDateTime,
          isActive: res.isActive,
          isExclusive:res.isExclusive,
          penaltyPercentPerDay: res.penaltyPercentPerDay as number
        }
      },
      error:(err) => {
        console.log(err);
      }
    })
  }

  // update ekbu 
  updateEkub(){
    this.ekubService.updateEkub({
      body: this.ekubRequest
    }).subscribe({
      next:()=>{
        this.toastrService.success('Sucessfully Updated', 'Done');
        this.router.navigate(['admin','ekubs']);
      },
      error:(err:HttpErrorResponse)=>{
        const errMsg = JSON.parse(err.error);
        if(errMsg.validationErrors){
          this.errMsgs = errMsg.validationErrors;
        } else{
          console.log(err);
          this.toastrService.error(err.error.error, 'Ooops');
        }
      }
    })
  }

  // on save btn clicked
  onSave() {
    console.log(this.ekubRequest);
    if(this.ekubRequest.id){
      this.updateEkub();
    } else {
      this.createEkub();
    }
  }

  // on cancle btn clicked
  onCancel() {
    this.router.navigate(['admin','ekubs']);
  }
  
}
