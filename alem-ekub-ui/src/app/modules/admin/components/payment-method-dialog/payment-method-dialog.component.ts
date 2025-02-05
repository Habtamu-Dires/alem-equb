import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-payment-method-dialog',
  imports: [CommonModule,FormsModule],
  templateUrl: './payment-method-dialog.component.html',
  styleUrl: './payment-method-dialog.component.scss'
})
export class PaymentMethodDialogComponent {

  remark:string ='';
  selectedPaymentMethod: string | undefined;
  showRemarkErr:boolean = false;
  showMethodErr:boolean = false;
  showPNErr:boolean = false;

  phoneNumber: string;
  amount:number;
  username:string;
  fullName:string;
  
  constructor(
    public dialogRef: MatDialogRef<PaymentMethodDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: { 
        phoneNumber: string,
        amount:number,
        username: string,
        fullName:string
       }
    ) {
      this.phoneNumber = data.phoneNumber;
      this.username = data.username;
      this.fullName = data.fullName;
      this.amount = data.amount;

      //select payment method based on the phone number
      if(this.phoneNumber.startsWith('09')){
        this.selectedPaymentMethod = 'telebirr';
      } else if(this.phoneNumber.startsWith('07')){
        this.selectedPaymentMethod = 'm-pesa';
      } 
    }

    selectPaymentMethod(method: string) {
      this.selectedPaymentMethod = method;
    }
    // submit 
    submit(){
      if(!this.remark){
        this.showRemarkErr = true;
      } else if(!this.selectedPaymentMethod){
        this.showMethodErr = true;
      } else if(!this.isPhoneValide()){
        this.showPNErr = true;
      }
      else {
        this.dialogRef.close({
          paymentMethod: this.selectedPaymentMethod,
          remark: this.remark
        });
      } 
    }
    // 
    cancel(){
      this.dialogRef.close();
    }

    // phone number validator
  isPhoneValide(){
    if(this.phoneNumber.length !== 10){
      return false;
    } else if(!this.phoneNumber.startsWith('09') && !this.phoneNumber.startsWith('07')){
      return false;
    }

    return true;
  }
}
