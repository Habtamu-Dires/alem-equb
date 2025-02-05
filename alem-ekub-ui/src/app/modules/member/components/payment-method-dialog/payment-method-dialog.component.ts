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

  remark:string | undefined;
  selectedPaymentMethod: string |undefined;
  username:string;
  phoneNumber:string;
  totalAmount:number;

  showRemarkrErr: boolean = false;
  showMethodErr: boolean = false;
  showPNErr: boolean = false;
   
  constructor(
    public dialogRef:MatDialogRef<PaymentMethodDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      phoneNumber: string,
      totalAmount:number,
      username:string,
     }
  ){
    this.phoneNumber = data.phoneNumber;
    this.totalAmount = data.totalAmount;
    this.username = data.username;

    //select payment method based on the phone number
    if(this.phoneNumber.startsWith('09')){
      this.selectedPaymentMethod = 'telebirr';
    } else if(this.phoneNumber.startsWith('07')){
      this.selectedPaymentMethod = 'm-pesa';
    } 

  }

  // select payment method
  selectPaymentMethod(method: string) {
    this.selectedPaymentMethod = method;
    this.showMethodErr = false;
  }
  // submit 
  submit(){
    if(!this.remark){
      this.showRemarkrErr = true;
    } else if(!this.selectedPaymentMethod){
      this.showMethodErr = true;
    } else if(!this.isPhoneValide()) {
      this.showPNErr = true;
    } else {
      this.dialogRef.close({
        phoneNumber: this.phoneNumber,
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
