import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, retry, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  public static DEFAULT_URL: string = "http://localhost:8080/"
  public static DEFAULT_HEADERS: HttpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  constructor(private http: HttpClient) {}

  httpError(error: HttpErrorResponse) {
    let msg: string = '';
    if (error.error instanceof ErrorEvent) {
      //Client slide  error
      msg = error.error.message;
    } else {
      //Server side error
      msg = `Status: ${error.status}\nMessage: ${error.message}\nDetials: ${error.error}`;
    }
    console.log(msg);
    return throwError(() => new Error(msg));
  }
 
  public sendGetRequest<T>(path: string, headers: HttpHeaders | null): Observable<T> {
    if (headers == null) {
      headers = ApiService.DEFAULT_HEADERS;
    }
    let httpOptions: Object = {
      observe: 'body',
      responseType: 'json',
      headers: headers,
    };
    return this.http
      .get<T>(path, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }
}
