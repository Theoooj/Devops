import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    HttpClientModule, // ✅ Ajoute pour éviter l'erreur HttpClient
    ReactiveFormsModule // ✅ Pour gérer les formulaires réactifs
  ],
  providers: [],
})
export class AppModule {}
