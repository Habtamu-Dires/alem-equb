import { Component, Input } from '@angular/core';
import { EkubResponse } from '../../../../services/models';
import { DatePipe, DecimalPipe } from '@angular/common';
import { formatDistanceToNow } from 'date-fns';
import { Router } from '@angular/router';


@Component({
  selector: 'app-ekub',
  imports: [DecimalPipe],
  providers: [DatePipe],
  templateUrl: './ekub.component.html',
  styleUrl: './ekub.component.scss'
})
export class EkubComponent {

  @Input() ekub:EkubResponse | undefined;

  constructor(
    private datePipe:DatePipe,
    private router:Router
  ) { }

  showDetails(ekubId:any){
    this.router.navigate(['member','ekub-detail',ekubId]);
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
    const duration = formatDistanceToNow(date)
    return duration;
  }

  
}
