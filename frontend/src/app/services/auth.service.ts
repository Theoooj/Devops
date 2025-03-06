import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth/login';

  constructor(private api: ApiService) {}

  login(credentials: { email: string, password: string }): Observable<any> {
    const body = {
      email: credentials.email,
      password: credentials.password
    };
    return this.api.sendPostRequest(this.apiUrl, body, null);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
  }
}
