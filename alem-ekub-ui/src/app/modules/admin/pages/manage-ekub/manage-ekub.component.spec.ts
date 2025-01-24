import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageEkubComponent } from './manage-ekub.component';

describe('ManageEkubComponent', () => {
  let component: ManageEkubComponent;
  let fixture: ComponentFixture<ManageEkubComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageEkubComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageEkubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
