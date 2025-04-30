import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { AppState } from '../../store/reducers';
import * as ActivityActions from '../../store/actions/activity.actions';
import { ActivitySearch } from '../../core/models/activity.model';

@Component({
  selector: 'app-activity-search',
  templateUrl: './activity-search.component.html',
  styleUrls: ['./activity-search.component.scss']
})
export class ActivitySearchComponent implements OnInit, OnDestroy {
  searchForm!: FormGroup;
  private destroy$ = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.setupFormListeners();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  initForm(): void {
    this.searchForm = this.formBuilder.group({
      userId: [null],
      action: [''],
      startDate: [null],
      endDate: [null]
    });
  }

  setupFormListeners(): void {
    // Apply debouncing (300ms) to search
    this.searchForm.valueChanges.pipe(
      takeUntil(this.destroy$),
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(formValue => {
      this.performSearch(formValue);
    });
  }

  performSearch(searchParams: ActivitySearch): void {
    // Only search if at least one field has a value
    if (searchParams.userId || searchParams.action || searchParams.startDate || searchParams.endDate) {
      this.store.dispatch(ActivityActions.searchActivities({
        searchParams: {
          ...searchParams,
          page: 0,
          size: 10
        }
      }));
    } else {
      // If all fields are empty, fetch all activities
      this.store.dispatch(ActivityActions.fetchActivities({ page: 0, size: 10 }));
    }
  }

  resetSearch(): void {
    this.searchForm.reset();
    this.store.dispatch(ActivityActions.fetchActivities({ page: 0, size: 10 }));
  }
}