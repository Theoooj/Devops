import { Produit } from "./product";

export interface Panier {
    id: number;
    products: Array<Produit>;
}
