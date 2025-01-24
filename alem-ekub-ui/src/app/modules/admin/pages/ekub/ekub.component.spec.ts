import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EkubComponent } from './ekub.component';

describe('EkubComponent', () => {
  let component: EkubComponent;
  let fixture: ComponentFixture<EkubComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EkubComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EkubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
