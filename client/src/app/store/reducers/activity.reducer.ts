import { createReducer, on } from '@ngrx/store';
import { Activity } from '../../core/models/activity.model';
import * as ActivityActions from '../actions/activity.actions';

export interface ActivityState {
  activities: Activity[];
  loading: boolean;
  error: any;
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export const initialState: ActivityState = {
  activities: [],
  loading: false,
  error: null,
  totalElements: 0,
  totalPages: 0,
  currentPage: 0,
  pageSize: 10
};

export const activityReducer = createReducer(
  initialState,

  // Fetch Activities
  on(ActivityActions.fetchActivities, (state, { page, size }) => ({
    ...state,
    loading: true,
    error: null,
    currentPage: page,
    pageSize: size
  })),

  on(ActivityActions.fetchActivitiesSuccess, (state, { response }) => ({
    ...state,
    activities: response.content,
    totalElements: response.totalElements,
    totalPages: response.totalPages,
    loading: false
  })),

  on(ActivityActions.fetchActivitiesFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Fetch User Activities
  on(ActivityActions.fetchUserActivities, (state, { page, size }) => ({
    ...state,
    loading: true,
    error: null,
    currentPage: page,
    pageSize: size
  })),

  on(ActivityActions.fetchUserActivitiesSuccess, (state, { response }) => ({
    ...state,
    activities: response.content,
    totalElements: response.totalElements,
    totalPages: response.totalPages,
    loading: false
  })),

  on(ActivityActions.fetchUserActivitiesFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Search Activities
  on(ActivityActions.searchActivities, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(ActivityActions.searchActivitiesSuccess, (state, { response }) => ({
    ...state,
    activities: response.content,
    totalElements: response.totalElements,
    totalPages: response.totalPages,
    loading: false
  })),

  on(ActivityActions.searchActivitiesFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Log Activity
  on(ActivityActions.logActivity, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(ActivityActions.logActivitySuccess, (state, { activity }) => ({
    ...state,
    activities: [activity, ...state.activities.slice(0, state.pageSize - 1)],
    totalElements: state.totalElements + 1,
    loading: false
  })),

  on(ActivityActions.logActivityFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // WebSocket Activity
  on(ActivityActions.addWebSocketActivity, (state, { activity }) => ({
    ...state,
    activities: [activity, ...state.activities.slice(0, state.pageSize - 1)],
    totalElements: state.totalElements + 1
  })),

  // Clear Activities
  on(ActivityActions.clearActivities, state => ({
    ...state,
    activities: [],
    totalElements: 0,
    totalPages: 0
  }))
);