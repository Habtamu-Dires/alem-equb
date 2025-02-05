import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { EkubResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  selectedEkubSubject = new BehaviorSubject<EkubResponse>({});
  selectedEkub$ = this.selectedEkubSubject.asObservable();
  
  constructor() { }

  updateSelectedEkub(ekub:EkubResponse){
    this.selectedEkubSubject.next(ekub);
  }
}
