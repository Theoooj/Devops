import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
  imports: [FormsModule, CommonModule]
})
export class LoginFormComponent {
  loginData = { email: '', password: '' };
  errorMessage: string = '';
  private failedAttempts = 0;
  private maxAttempts = 3;
  private lockoutDuration = 30000; // Temps de verrouillage en ms (30s)
  private lastFailedAttemptTime: number | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (this.failedAttempts >= this.maxAttempts) {
      const currentTime = Date.now();
      if (this.lastFailedAttemptTime && currentTime - this.lastFailedAttemptTime < this.lockoutDuration) {
        this.errorMessage = 'Trop de tentatives échouées. Veuillez réessayer plus tard.';
        return;
      } else {
        this.failedAttempts = 0;
        this.lastFailedAttemptTime = null;
      }
    }

    // Vérification des champs vides (mais compter quand même une tentative)
    if (!this.loginData.email.trim() || !this.loginData.password.trim()) {
      this.failedAttempts++; // Compte l'échec
      this.errorMessage = 'Veuillez remplir tous les champs.';
      return;
    }

    // Vérification du format de l'email
    if (!this.isValidEmail(this.loginData.email)) {
      this.failedAttempts++; // Compte l'échec
      this.errorMessage = 'Email invalide.';
      return;
    }

    // Appel API
    this.authService.login(this.loginData).subscribe({
      next: (response: { token: string }) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/produits']);
        this.failedAttempts = 0;
      },
      error: () => {
        this.failedAttempts++;
        this.lastFailedAttemptTime = Date.now();
        this.errorMessage = 'Email ou mot de passe incorrect';
      }
    });
  }
  
  isValidEmail(email: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
  }
}
