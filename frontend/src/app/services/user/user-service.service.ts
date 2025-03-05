import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';
import { HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  public static USER_URL = ApiService.DEFAULT_URL + 'users'
  jwtHelper: any;

  constructor(
    private api: ApiService,
  ) {}

  getAllUsers(): Observable<User[]>{
    console.log(UserServiceService.USER_URL)
    return this.api.sendGetRequest(UserServiceService.USER_URL, null);
  }

  public login(email: string, password: string): Observable<void> {
    let params: HttpParams = new HttpParams({
      fromObject: {
        email: email,
        password: password
      }
    });
    let obs: Observable<HttpResponse<string>> = this.api.sendPostRequest(ApiService.DEFAULT_URL + '/login', params, null);
    return new Observable<void>((observer) => {
      obs.subscribe({
        next: (data: HttpResponse<string>) => {
          let headers: HttpHeaders = data.headers;
          let jwt: string | null = headers.get("Authorization");
  
          if (jwt) {
            localStorage.setItem("token", jwt);
          }
          if (jwt) {
            console.log("Connexion réussie");
          }  
          observer.next();
          observer.complete();
        },
        error: (err) => {
          observer.error(err);
        }
      });
    });
  }
}

