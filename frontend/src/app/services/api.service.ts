import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
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
  public static FORM_HEADERS: HttpHeaders = new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded',
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

  public sendPostRequest<T>(path: string, body: any, headers: HttpHeaders | null): Observable<T> {
    if (headers == null) {
      headers = ApiService.DEFAULT_HEADERS;
    }
  
    const httpOptions = {
      observe: 'body' as const,
      responseType: 'json' as const,
      headers: headers,
    };
  
    return this.http
      .post<T>(path, body, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }
  

  public sendPutRequest<T>(path: string, params: HttpParams, headers: HttpHeaders | null): Observable<T> {
    if (headers == null) {
      headers = ApiService.DEFAULT_HEADERS;
    }
    let httpOptions: Object = {
      observe: 'body',
      responseType: 'json',
      headers: headers,
    };
    return this.http
      .put<T>(path, params, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  public sendDeleteRequest<T>(
    path: string,
    headers: HttpHeaders | null
  ): Observable<T> {
    if (headers == null) {
      headers = ApiService.FORM_HEADERS
    }
    let httpOptions: Object = {
      observe: 'response',
      responseType: 'json',
      headers: headers,
    };
    return this.http
      .delete<T>(path, httpOptions)
      .pipe(retry(1), catchError(this.httpError));
  }

  public uploadfile(file: File): Observable<string> {
    let formParams = new FormData();
    formParams.append('file', file);
    let httpOptions: Object = {
        observe: 'body',
        responseType: 'text',
    };
    let url: string = '/image/upload';
    return this.http
        .post<string>(url, formParams, httpOptions)
        .pipe(
            retry(1)
        );
  }
}
