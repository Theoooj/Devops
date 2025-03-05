import { Component, OnInit } from '@angular/core';
import { ProduitService } from '../../services/produit/produit.service';
import { Produit } from '../../interfaces/product';
import { of } from 'rxjs';

@Component({
  selector: 'app-produits-list',
  imports: [],
  templateUrl: './produits-list.component.html',
  styleUrl: './produits-list.component.css'
})
export class ProduitsListComponent implements OnInit{
  produits: Produit[]= [];

  constructor(private produitService: ProduitService){

  }

  ngOnInit(): void {
    this.produitService.getAllProducts().subscribe((data: Produit[]) => {
      this.produits = data;
      for(let produit of this.produits){
        console.log(produit.srcUrl)
      }
    });
  }
  
}
