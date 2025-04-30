import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitySearchComponent } from './activity-search.component';

describe('ActivitySearchComponent', () => {
  let component: ActivitySearchComponent;
  let fixture: ComponentFixture<ActivitySearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActivitySearchComponent]
    });
    fixture = TestBed.createComponent(ActivitySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
