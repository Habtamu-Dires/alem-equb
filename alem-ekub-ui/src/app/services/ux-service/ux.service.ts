import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UxService {

  //shaw drawer status
  showDrawerSubjec = new BehaviorSubject<boolean>(false);
  showDrawer$ = this.showDrawerSubjec.asObservable();

  //header selected item
  headersSelectedItemSubject = new BehaviorSubject<string>('Home');
  headersSelectedItem$ = this.headersSelectedItemSubject.asObservable();

  //page service
  paginationSizeSubject = new BehaviorSubject<number>(5);
  paginationSize$ = this.paginationSizeSubject.asObservable();

  constructor() { }

  // update show drawer
  updateShowDrawerStatus(value:boolean){
    this.showDrawerSubjec.next(value);
  }

  // update header selected item
  updateHeadersSelectedItem(value:string){
    this.headersSelectedItemSubject.next(value);
  }

  // update pagination size
  updatePaginationSize(value:number){
    this.paginationSizeSubject.next(value);
  }


}
