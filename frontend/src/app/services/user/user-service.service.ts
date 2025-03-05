import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  public static USER_URL = ApiService.DEFAULT_URL + 'users'

  constructor(
    private api: ApiService,
  ) {}

  getAllUsers(): Observable<any>{
    console.log(UserServiceService.USER_URL)
    return this.api.sendGetRequest(UserServiceService.USER_URL, null);
  }
}
