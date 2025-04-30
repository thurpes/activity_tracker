import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/reducers';
import * as ActivityActions from '../../store/actions/activity.actions';
import * as ActivitySelectors from '../../store/selectors/activity.selectors';
import { Activity } from '../../core/models/activity.model';

@Component({
  selector: 'app-activity-list',
  templateUrl: './activity-list.component.html',
  styleUrls: ['./activity-list.component.scss']
})
export class ActivityListComponent implements OnChanges {
  @Input() activities: Activity[] | null = [];
  @Input() loading: boolean | null = false;

  currentPage = 0;
  pageSize = 10;

  constructor(private store: Store<AppState>) {
    this.store.select(ActivitySelectors.selectCurrentPage).subscribe(page => {
      this.currentPage = page;
    });

    this.store.select(ActivitySelectors.selectPageSize).subscribe(size => {
      this.pageSize = size;
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    // You can react to input changes if needed
  }

  formatDateTime(dateTime: string | undefined): string {
    if (!dateTime) return '';
    return new Date(dateTime).toLocaleString();
  }

  getBadgeClass(action: string): string {
    switch (action) {
      case 'LOGIN':
        return 'bg-success';
      case 'LOGOUT':
        return 'bg-warning';
      case 'ERROR':
        return 'bg-danger';
      default:
        return 'bg-primary';
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.store.dispatch(ActivityActions.fetchActivities({
      page,
      size: this.pageSize
    }));
  }
}