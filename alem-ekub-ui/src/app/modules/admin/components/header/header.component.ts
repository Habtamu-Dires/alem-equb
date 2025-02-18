import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, throttleTime } from 'rxjs';
import { EkubResponse } from '../../../../services/models';
import { EkubsService } from '../../../../services/services';

@Component({
  selector: 'app-header',
  imports: [ReactiveFormsModule,CommonModule,FormsModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit{

  @Input() componentName: string = '';
  @Output() onCreateNewCliked = new EventEmitter<{}>();
  @Output() onSearch = new EventEmitter<string>();
  @Output() filter = new EventEmitter<string>();
  @Output() afterDateTime = new EventEmitter<string>();

  showEkubs:boolean = false;
  ekubs:EkubResponse[] = [];
  dateTime:string | undefined;

  searchControl= new FormControl();
  ekubSearchControl = new FormControl();

  constructor(
    private ekubsService:EkubsService
  ){}

  ngOnInit(): void {
    this.searchFormControl();
    this.ekubSearchFormControl();
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

}
