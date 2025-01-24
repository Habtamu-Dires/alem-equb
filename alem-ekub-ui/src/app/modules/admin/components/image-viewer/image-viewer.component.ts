import { Component, HostListener, Inject, Input } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-image-viewer',
  imports: [],
  templateUrl: './image-viewer.component.html',
  styleUrl: './image-viewer.component.scss'
})
export class ImageViewerComponent {

  imageString:string = '';

  constructor(
     private imageViewerRef:MatDialogRef<ImageViewerComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { imageString: string }
  ){
    this.imageString = data.imageString;
  }

  closeDialog(){
    this.imageViewerRef.close();
  }

  

}
