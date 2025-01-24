import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EkubDetailComponent } from './ekub-detail.component';

describe('EkubDetailComponent', () => {
  let component: EkubDetailComponent;
  let fixture: ComponentFixture<EkubDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EkubDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EkubDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
