import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';

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
    return this.api.sendGetRequest(UserServiceService.USER_URL, null);
  }

}

