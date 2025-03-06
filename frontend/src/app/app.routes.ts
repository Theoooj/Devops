import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './login/login-form/login-form.component';
import { ProduitsListComponent } from './produits/produits-list/produits-list.component';
import { NgModule } from '@angular/core';

export const routes: Routes = [
    { path: 'login', component: LoginFormComponent },
    { path: 'produits', component: ProduitsListComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full' } // Correction ici
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
