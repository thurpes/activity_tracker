import { ActionReducerMap } from '@ngrx/store';
import * as fromActivity from './activity.reducer';
import * as fromAuth from './auth.reducer';

export interface AppState {
  activities: fromActivity.ActivityState;
  auth: fromAuth.AuthState;
}

export const reducers: ActionReducerMap<AppState> = {
  activities: fromActivity.activityReducer,
  auth: fromAuth.authReducer
};