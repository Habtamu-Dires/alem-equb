import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEkubsComponent } from './new-ekubs.component';

describe('NewEkubsComponent', () => {
  let component: NewEkubsComponent;
  let fixture: ComponentFixture<NewEkubsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewEkubsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewEkubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
