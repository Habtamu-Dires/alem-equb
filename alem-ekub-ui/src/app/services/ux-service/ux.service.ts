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

  //home selected page in members during change of size
  selectedHomePageSubject = new BehaviorSubject<string>('ekubs');
  selectedHomePage$ = this.selectedHomePageSubject.asObservable();

  //pagination service
  paginationSizeSubject = new BehaviorSubject<number>(5);
  paginationSize$ = this.paginationSizeSubject.asObservable();

  //new ekub selected item [public vs invited]
  selectedNewEkubCategorySubject = new BehaviorSubject<string>('public');
  selectedNewEkubCategory$ = this.selectedNewEkubCategorySubject.asObservable();

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

  // update home selected page size
  updateSelectedHomePage(value:string){
    this.selectedHomePageSubject.next(value);
  }

  // update selected new ekub category
  updateSelectedNewEkubCategory(value:string){
    this.selectedNewEkubCategorySubject.next(value);
  }
}
