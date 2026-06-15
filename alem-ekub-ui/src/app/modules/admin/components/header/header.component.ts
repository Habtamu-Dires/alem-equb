import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, throttleTime } from 'rxjs';
import { EkubResponse } from '../../../../services/models';
import { EkubsService } from '../../../../services/services';
import { AdminUxService } from '../../services/admin-ux/admin-ux.service';
import { NgxPrintModule } from 'ngx-print';

@Component({
  selector: 'app-header',
  imports: [ReactiveFormsModule,CommonModule,FormsModule,NgxPrintModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{

  @Input() componentName: string = '';
  @Input() printSectionId: string = '';
  @Output() onCreateNewCliked = new EventEmitter<{}>();
  @Output() onSearch = new EventEmitter<string>();
  @Output() filter = new EventEmitter<string>();
  @Output() afterDateTime = new EventEmitter<string>();

  showEkubs:boolean = false;
  ekubs:EkubResponse[] = [];
  dateTime:string | undefined;
  onShowDrawer:boolean = false;

  searchControl= new FormControl();
  ekubSearchControl = new FormControl();

  constructor(
    private ekubsService:EkubsService,
    private adminUxService:AdminUxService
  ){}

  ngOnInit(): void {
    this.searchFormControl();
    this.ekubSearchFormControl();
    // on show drawer status
    this.adminUxService.showDrawer$.subscribe((onShowDrawer:boolean)=>{
      this.onShowDrawer = onShowDrawer;
    });
  }

  // update showDrawer status
  updateShowDrawerStatus(){
    this.adminUxService.updateShowDrawerStatus(this.onShowDrawer);
  }

  // toogle showdrawer
  toggleShowDrawer(){
    this.onShowDrawer = !this.onShowDrawer;
    this.updateShowDrawerStatus();
  }

  onCreateNew() {
    this.onCreateNewCliked.emit();
  }

  // search form control
  searchFormControl(){
    this.searchControl.valueChanges
    .pipe(
      debounceTime(300),
      throttleTime(1000)
    ).subscribe((value:any) =>{
       const text = value as string;
       this.onSearch.emit(text);
    })
  }

  // clear search area
  clearSearchArea() {
    this.searchControl.setValue('');
    this.onSearch.emit('');
  }

  //on filter changed
  onFilterChanged(value:any){
    this.filter.emit(value as string);
  }

  // ekub search control
  ekubSearchFormControl(){
    this.ekubSearchControl.valueChanges
    .pipe(
      debounceTime(300),
      throttleTime(1000)
    )
    .subscribe((value:any)=>{
      const text = value as string;
      if(text.length >= 3 && !this.showEkubs){
        this.searchEkubByName(text); 
      } else if(this.showEkubs){
        this.showEkubs = false;
      }
    })
  }

  // clear ekub search
  clearEkubSearch(){
    this.ekubSearchControl.setValue('');
    this.showEkubs = false;
  }

  // on ekub selected
  onEkubSelected(ekub:EkubResponse){
    this.filter.emit(ekub.id as string);
    this.ekubSearchControl.setValue(ekub.name);
  }

  // search ekub by name
  searchEkubByName(name:string){
    this.ekubsService.searchEkubByName({
      'ekub-name': name
    }).subscribe({
      next:(res:EkubResponse[])=>{
        if(res.length > 0){
          this.ekubs = res;
          this.showEkubs = true;
        }
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  // on date change
  onDateTimeChange(){
    this.afterDateTime.emit(this.dateTime);
  }

  // print page
  printPage(){
    window.print();
  }

}
