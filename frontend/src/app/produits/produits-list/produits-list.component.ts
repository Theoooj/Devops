import { Component, OnInit } from '@angular/core';
import { ProduitService } from '../../services/produit/produit.service';
import { Produit } from '../../interfaces/product';
import { of } from 'rxjs';
import { CartService } from '../../services/panier/cart.service';

@Component({
  selector: 'app-produits-list',
  imports: [],
  templateUrl: './produits-list.component.html',
  styleUrl: './produits-list.component.css'
})
export class ProduitsListComponent implements OnInit{
  produits: Produit[]= [];

  constructor(private produitService: ProduitService, private basketService: CartService){

  }

  ngOnInit(): void {
    this.produitService.getAllProducts().subscribe((data: Produit[]) => {
      this.produits = data;
      for(let produit of this.produits){
        console.log(produit.srcUrl)
      }
    });
  }

  addProductToBasket(id : number){
    this.basketService.addProductBasket(id);
  }
  
}
