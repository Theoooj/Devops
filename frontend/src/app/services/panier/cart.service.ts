import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';

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

  addProductBasket(idProduct: number): Observable<any> {
    const body = {
        products: [
            { id: idProduct }
        ]
    };
    return this.api.sendPostRequest(CartService.CART_URL + '/baskets', body, null);
  }

}
