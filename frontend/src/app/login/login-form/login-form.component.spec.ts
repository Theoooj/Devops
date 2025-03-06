import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { LoginFormComponent } from './login-form.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    // Mock du service AuthService et Router
    const authSpy = jasmine.createSpyObj('AuthService', ['login']);
    const routerSpyObj = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        LoginFormComponent, // IMPORTANT : On l'importe car il est standalone
        FormsModule // Ajout du module pour activer [(ngModel)]
      ],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpyObj }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    fixture.detectChanges();
  });

  /**  Test 1 : Vérifier l'authentification réussie */
  it('doit rediriger vers /dashboard en cas de connexion réussie', () => {
    const fakeToken = { token: 'fake-jwt-token' };
    authServiceSpy.login.and.returnValue(of(fakeToken));

    component.loginData.email = 'valid@example.com';
    component.loginData.password = 'ValidPassword123';
    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith(component.loginData);
    expect(localStorage.getItem('token')).toBe(fakeToken.token);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/produits']);
  });

  /**  Test 2 : Vérifier le message d'erreur en cas d'identifiants incorrects */
  it('doit afficher un message d\'erreur en cas de connexion échouée', () => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Erreur d\'authentification')));

    component.loginData.email = 'wrong@example.com';
    component.loginData.password = 'wrongpassword';
    component.onSubmit();

    expect(component.errorMessage).toBe('Email ou mot de passe incorrect');
  });

  /** Test 3 : Vérifier la protection contre le brute force (3 échecs max) */
  it('doit bloquer l\'utilisateur après 3 tentatives échouées', fakeAsync(() => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Échec d\'authentification')));
  
    // Remplit les champs et déclenche 3 erreurs
    component.loginData.email = 'test@example.com';
    component.loginData.password = 'wrongpassword';
    
    for (let i = 0; i < 3; i++) {
      component.onSubmit();
    }
  
    tick(1000); // Simulation d’un délai
  
    // 4ème tentative (devrait être bloquée)
    component.onSubmit();
    expect(component.errorMessage).toBe('Trop de tentatives échouées. Veuillez réessayer plus tard.');
  }));

  /** Test 4 : Vérifier la protection contre l'injection SQL */
  it('doit empêcher l\'injection SQL', () => {
    authServiceSpy.login.and.returnValue(throwError(() => new Error('Tentative de requête invalide')));
  
    component.loginData.email = `' OR 1=1; --`; // Email malveillant
    component.loginData.password = 'password';
    component.onSubmit();
  
    // Vérifier que login() N'EST PAS appelé car l'email est invalide
    expect(authServiceSpy.login).not.toHaveBeenCalled();
    expect(component.errorMessage).toBe('Email invalide.');
  });

  /** Test 5 : Vérifier la soumission avec des champs vides */
  it('doit afficher une erreur si les champs sont vides', () => {
    component.loginData.email = '';
    component.loginData.password = '';
    component.onSubmit();

    expect(component.errorMessage).toBe('Veuillez remplir tous les champs.');
  });

  /** Test 6 : Vérifier la validation de l'email */
  it('doit afficher une erreur si l\'email est invalide', () => {
    component.loginData.email = 'invalid-email';
    component.loginData.password = 'ValidPassword123';
    component.onSubmit();

    expect(component.errorMessage).toBe('Email invalide.');
  });
});
