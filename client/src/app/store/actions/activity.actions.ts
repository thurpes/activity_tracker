import { createAction, props } from '@ngrx/store';
import { Activity, ActivitySearch, PagedResponse } from '../../core/models/activity.model';

export const fetchActivities = createAction(
  '[Activity] Fetch Activities',
  props<{ page: number; size: number; sort?: string }>()
);

export const fetchActivitiesSuccess = createAction(
  '[Activity] Fetch Activities Success',
  props<{ response: PagedResponse<Activity> }>()
);

export const fetchActivitiesFailure = createAction(
  '[Activity] Fetch Activities Failure',
  props<{ error: any }>()
);

export const fetchUserActivities = createAction(
  '[Activity] Fetch User Activities',
  props<{ userId: number; page: number; size: number }>()
);

export const fetchUserActivitiesSuccess = createAction(
  '[Activity] Fetch User Activities Success',
  props<{ response: PagedResponse<Activity> }>()
);

export const fetchUserActivitiesFailure = createAction(
  '[Activity] Fetch User Activities Failure',
  props<{ error: any }>()
);

export const searchActivities = createAction(
  '[Activity] Search Activities',
  props<{ searchParams: ActivitySearch }>()
);

export const searchActivitiesSuccess = createAction(
  '[Activity] Search Activities Success',
  props<{ response: PagedResponse<Activity> }>()
);

export const searchActivitiesFailure = createAction(
  '[Activity] Search Activities Failure',
  props<{ error: any }>()
);

export const logActivity = createAction(
  '[Activity] Log Activity',
  props<{ activity: Activity }>()
);

export const logActivitySuccess = createAction(
  '[Activity] Log Activity Success',
  props<{ activity: Activity }>()
);

export const logActivityFailure = createAction(
  '[Activity] Log Activity Failure',
  props<{ error: any }>()
);

export const addWebSocketActivity = createAction(
  '[Activity] Add WebSocket Activity',
  props<{ activity: Activity }>()
);

export const clearActivities = createAction(
  '[Activity] Clear Activities'
);