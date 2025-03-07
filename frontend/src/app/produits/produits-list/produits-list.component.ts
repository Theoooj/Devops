import { Component, OnInit } from '@angular/core';
import { ProduitService } from '../../services/produit/produit.service';
import { Produit } from '../../interfaces/product';
import { of } from 'rxjs';
import { CartService } from '../../services/panier/cart.service';
import { CommonModule } from '@angular/common';
import { Panier } from '../../interfaces/basket';

@Component({
  selector: 'app-produits-list',
  imports: [],
  templateUrl: './produits-list.component.html',
  styleUrl: './produits-list.component.css'
})
export class ProduitsListComponent implements OnInit{
  produits: Produit[]= [];
  paniers: Panier[]=[];
  produitsPanier: Produit[]=[];

  constructor(private produitService: ProduitService, private basketService: CartService, private cartService : CartService){
    this.removeProductFromList();
  }

  ngOnInit(): void {
    this.produitService.getAllProducts().subscribe((data: Produit[]) => {
      this.produits = data;
    });
  }

  addProductToBasket(id : number){
    this.basketService.addProductBasket(id).subscribe();
  }

  removeProductFromList(){
    this.cartService.getBasket().subscribe((data: Panier)=>{
    })
  }
  
}
