import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ActivityState } from '../reducers/activity.reducer';

export const selectActivityState = createFeatureSelector<ActivityState>('activities');

export const selectActivities = createSelector(
  selectActivityState,
  state => state.activities
);

export const selectLoading = createSelector(
  selectActivityState,
  state => state.loading
);

export const selectError = createSelector(
  selectActivityState,
  state => state.error
);

export const selectTotalElements = createSelector(
  selectActivityState,
  state => state.totalElements
);

export const selectTotalPages = createSelector(
  selectActivityState,
  state => state.totalPages
);

export const selectCurrentPage = createSelector(
  selectActivityState,
  state => state.currentPage
);

export const selectPageSize = createSelector(
  selectActivityState,
  state => state.pageSize
);