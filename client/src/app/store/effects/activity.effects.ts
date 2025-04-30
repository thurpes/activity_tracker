import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, switchMap, catchError } from 'rxjs/operators';
import { ActivityService } from '../../core/services/activity.service';
import * as ActivityActions from '../actions/activity.actions';

@Injectable()
export class ActivityEffects {

  constructor(
    private actions$: Actions,
    private activityService: ActivityService
  ) {}

  fetchActivities$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.fetchActivities),
      switchMap(({ page, size, sort }) =>
        this.activityService.getActivities(page, size, sort).pipe(
          map(response => ActivityActions.fetchActivitiesSuccess({ response })),
          catchError(error => of(ActivityActions.fetchActivitiesFailure({ error })))
        )
      )
    )
  );

  fetchUserActivities$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.fetchUserActivities),
      switchMap(({ userId, page, size }) =>
        this.activityService.getUserActivities(userId, page, size).pipe(
          map(response => ActivityActions.fetchUserActivitiesSuccess({ response })),
          catchError(error => of(ActivityActions.fetchUserActivitiesFailure({ error })))
        )
      )
    )
  );

  searchActivities$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.searchActivities),
      switchMap(({ searchParams }) =>
        this.activityService.searchActivities(searchParams).pipe(
          map(response => ActivityActions.searchActivitiesSuccess({ response })),
          catchError(error => of(ActivityActions.searchActivitiesFailure({ error })))
        )
      )
    )
  );

  logActivity$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ActivityActions.logActivity),
      switchMap(({ activity }) =>
        this.activityService.logActivity(activity).pipe(
          map(response => ActivityActions.logActivitySuccess({ activity: response })),
          catchError(error => of(ActivityActions.logActivityFailure({ error })))
        )
      )
    )
  );
}