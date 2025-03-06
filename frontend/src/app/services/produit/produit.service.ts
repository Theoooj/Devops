import { Injectable } from '@angular/core';
import { ApiService } from '../api.service';
import { Observable } from 'rxjs';
import { Produit } from '../../interfaces/product';

@Injectable({
  providedIn: 'root'
})
export class ProduitService {

  public static PRODUCTS_URL = ApiService.DEFAULT_URL + 'products'
  
  constructor(private api: ApiService) { 

  }

  getAllProducts(): Observable<Produit[]>{
      console.log(ProduitService.PRODUCTS_URL)
      return this.api.sendGetRequest(ProduitService.PRODUCTS_URL, null);
    }
}
