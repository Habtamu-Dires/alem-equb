import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewEkubDetailComponent } from './view-ekub-detail.component';

describe('ViewEkubDetailComponent', () => {
  let component: ViewEkubDetailComponent;
  let fixture: ComponentFixture<ViewEkubDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewEkubDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewEkubDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
