import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app.routes';
import { LoginFormComponent } from './login/login-form/login-form.component';
import { ProduitsListComponent } from './produits/produits-list/produits-list.component';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    LoginFormComponent,
    ProduitsListComponent // Ajout pour éviter les erreurs
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],

})
export class AppModule { }
