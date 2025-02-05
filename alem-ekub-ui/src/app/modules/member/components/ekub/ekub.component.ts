import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EkubResponse } from '../../../../services/models';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { formatDistanceToNow } from 'date-fns';
import { Router } from '@angular/router';
import { MemberService } from '../../../../services/member-services/member.service';


@Component({
  selector: 'app-ekub',
  imports: [DecimalPipe, CommonModule],
  providers: [DatePipe],
  templateUrl: './ekub.component.html',
  styleUrl: './ekub.component.scss'
})
export class EkubComponent {

  @Input() ekub:EkubResponse | undefined;
  @Input() isNewEkub:boolean = false;
  @Output() onJoinEkub = new EventEmitter<string>();
  @Output() onLeaveEkub = new EventEmitter<string>();


  constructor(
    private datePipe:DatePipe,
    private router:Router,
    private memberService:MemberService
  ) {}

  // show detail
  showDetails(ekub:EkubResponse){
    this.memberService.updateSelectedEkub(ekub);
    let type = 'joined'
    if(this.isNewEkub){
      type = 'new'
    }
    this.router.navigate(['member','ekub-detail',type]);
  }

  // join ekubs
  joinEkub(ekubId:any){
    this.onJoinEkub.emit(ekubId);
  }

  // unJoin ekub
  leaveEkub(ekubId:any){
    this.onLeaveEkub.emit(ekubId);
  }

  //transfrom date
  transformDate(dateString:any){
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , yy')
    return formattedDate;
  }

  transformDateTime(dateString:any){
    const date = new Date(dateString);
    const formattedDate = this.datePipe.transform(date, 'EEE, dd , yy, hh:mm a')
    return formattedDate;
  }

  //transfrom time
  transfromTime(dateString:any){
    const date = new Date(dateString);
    const fromattedTime = this.datePipe.transform(date, 'hh:mm a');
    return fromattedTime;
  }

  //transfrom  duaration
  transfromDuration(dateString:any){
    const date = new Date(dateString);
    const duration = formatDistanceToNow(date, {addSuffix: true})
    return duration;
  }

  
}
