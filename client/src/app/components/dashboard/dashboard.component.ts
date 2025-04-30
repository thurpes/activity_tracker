import { Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { take } from 'rxjs/operators';
import { AppState } from '../../store/reducers';
import * as ActivityActions from '../../store/actions/activity.actions';
import * as ActivitySelectors from '../../store/selectors/activity.selectors';
import * as AuthActions from '../../store/actions/auth.actions';
import * as AuthSelectors from '../../store/selectors/auth.selectors';
import { Activity } from '../../core/models/activity.model';
import { User } from '../../core/models/user.model';
import { WebSocketService } from '../../core/services/websocket.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  activities$: Observable<Activity[]>;
  loading$: Observable<boolean>;
  error$: Observable<any>;
  totalElements$: Observable<number>;
  user$: Observable<User | null>;

  private wsSubscription: Subscription | null = null;

  constructor(
    private store: Store<AppState>,
    private webSocketService: WebSocketService
  ) {
    this.activities$ = this.store.select(ActivitySelectors.selectActivities);
    this.loading$ = this.store.select(ActivitySelectors.selectLoading);
    this.error$ = this.store.select(ActivitySelectors.selectError);
    this.totalElements$ = this.store.select(ActivitySelectors.selectTotalElements);
    this.user$ = this.store.select(AuthSelectors.selectUser);
  }

  ngOnInit(): void {
    // Fetch initial activities
    this.store.dispatch(ActivityActions.fetchActivities({ page: 0, size: 10 }));

    // Connect to WebSocket for real-time updates
    this.connectWebSocket();
  }

  ngOnDestroy(): void {
    // Disconnect WebSocket
    this.webSocketService.disconnect();

    if (this.wsSubscription) {
      this.wsSubscription.unsubscribe();
    }
  }

  connectWebSocket(): void {
    this.store.select(AuthSelectors.selectToken).pipe(take(1)).subscribe(token => {
      if (token) {
        this.webSocketService.connect().subscribe(connected => {
          if (connected) {
            console.log('Connected to WebSocket');

            // Subscribe to activity updates
            this.wsSubscription = this.webSocketService.onActivity().subscribe(activity => {
              this.store.dispatch(ActivityActions.addWebSocketActivity({ activity }));
            });
          }
        });
      }
    });
  }

  logout(): void {
    this.store.dispatch(AuthActions.logout());
  }
}