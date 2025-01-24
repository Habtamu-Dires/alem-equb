import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-drawer-item',
  imports: [CommonModule],
  templateUrl: './drawer-item.component.html',
  styleUrl: './drawer-item.component.scss'
})
export class DrawerItemComponent {
  @Input() component:string | undefined;
  @Input() isActiveComponent: boolean | undefined;
}
