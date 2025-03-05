import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  public static USER_URL = ApiService.DEFAULT_URL + 'users'

  constructor(
    private api: ApiService,
  ) {}

  getAllUsers(): Observable<User[]>{
    console.log(UserServiceService.USER_URL)
    return this.api.sendGetRequest(UserServiceService.USER_URL, null);
  }
}
