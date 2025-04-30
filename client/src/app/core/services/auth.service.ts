import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, AuthResponse } from '../models/auth.model';
import { TokenStorage } from '../auth/token.storage';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorage
  ) { }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          this.tokenStorage.saveToken(response.token);
          this.tokenStorage.saveUser({
            id: response.id,
            username: response.username,
            email: response.email
          });
        })
      );
  }

  logout(): void {
    this.tokenStorage.clear();
  }

  isAuthenticated(): boolean {
    return !!this.tokenStorage.getToken();
  }

  getCurrentUser() {
    return this.tokenStorage.getUser();
  }
}