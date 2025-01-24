import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-header',
  imports: [ReactiveFormsModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  @Input() componentName:string = '';
  @Output() onCreateNewCliked:EventEmitter<{}> = new EventEmitter<{}>();
  searchControl= new FormControl();

  onCreateNew() {
    this.onCreateNewCliked.emit();
  }

  clearSearchArea() {

  }

}
