import { createReducer, on } from '@ngrx/store';
import { User } from '../../core/models/user.model';
import * as AuthActions from '../actions/auth.actions';

export interface AuthState {
  user: User | null;
  token: string | null;
  loading: boolean;
  error: any;
  isAuthenticated: boolean;
}

// Try to get initial state from local storage
const storedToken = localStorage.getItem('auth-token');
const storedUserStr = localStorage.getItem('auth-user');
const storedUser = storedUserStr ? JSON.parse(storedUserStr) : null;

export const initialState: AuthState = {
  user: storedUser,
  token: storedToken,
  loading: false,
  error: null,
  isAuthenticated: !!storedToken
};

export const authReducer = createReducer(
  initialState,

  // Login
  on(AuthActions.login, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(AuthActions.loginSuccess, (state, { response }) => ({
    ...state,
    user: {
      id: response.id,
      username: response.username,
      email: response.email
    },
    token: response.token,
    isAuthenticated: true,
    loading: false
  })),

  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Logout
  on(AuthActions.logout, state => ({
    ...state,
    user: null,
    token: null,
    isAuthenticated: false
  }))
);