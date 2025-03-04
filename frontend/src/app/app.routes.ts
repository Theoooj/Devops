import { Routes } from '@angular/router';
import { LoginFormComponent } from './login/login-form/login-form.component';
import { ProduitsListComponent } from './produits/produits-list/produits-list.component';

export const routes: Routes = [
    { path: '', component: LoginFormComponent },
    { path: 'produits', component: ProduitsListComponent}
];
