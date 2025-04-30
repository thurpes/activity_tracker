import { createFeatureSelector, createSelector } from '@ngrx/store';
import { AuthState } from '../reducers/auth.reducer';

export const selectAuthState = createFeatureSelector<AuthState>('auth');

export const selectUser = createSelector(
  selectAuthState,
  state => state.user
);

export const selectToken = createSelector(
  selectAuthState,
  state => state.token
);

export const selectIsAuthenticated = createSelector(
  selectAuthState,
  state => state.isAuthenticated
);

export const selectAuthLoading = createSelector(
  selectAuthState,
  state => state.loading
);

export const selectAuthError = createSelector(
  selectAuthState,
  state => state.error
);