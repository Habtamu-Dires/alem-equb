import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EkubsComponent } from './ekubs.component';

describe('EkubsComponent', () => {
  let component: EkubsComponent;
  let fixture: ComponentFixture<EkubsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EkubsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EkubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
