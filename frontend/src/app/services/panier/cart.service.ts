import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  public static CART_URL = ApiService.DEFAULT_URL + 'baskets'

  constructor(
    private api: ApiService,
  ) {}

  getBasket(): Observable<any>{
    console.log(CartService.CART_URL)
    return this.api.sendGetRequest(CartService.CART_URL, null);
  }
}
