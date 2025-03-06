import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserServiceService } from './user-service.service';
import { ApiService } from '../api.service';
import {User} from '../../interfaces/user'
import { of } from 'rxjs';
import {dateTimestampProvider} from "rxjs/internal/scheduler/dateTimestampProvider";

describe('UserServiceService', () => {
  let service: UserServiceService;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;

  beforeEach(() => {
    // Création d'un mock pour ApiService
    const apiSpy = jasmine.createSpyObj('ApiService', ['sendGetRequest']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import pour éviter l'erreur HttpClient
      providers: [
        UserServiceService,
        { provide: ApiService, useValue: apiSpy } // Utilisation du mock d'ApiService
      ]
    });

    service = TestBed.inject(UserServiceService);
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /** Test : Vérifier que `getAllUsers()` appelle bien l'API */
  it('devrait appeler l\'API et récupérer les utilisateurs', () => {
    const mockUsers: User[] = [
      { id: 1, created_at: new Date(), email: "test.fn", password: "password" },
      { id: 2, created_at: new Date(), email: "test2.fn", password: "password2" }
    ];

    apiServiceSpy.sendGetRequest.and.returnValue(of(mockUsers));

    service.getAllUsers().subscribe((users) => {
      expect(users).toEqual(mockUsers);
    });

    expect(apiServiceSpy.sendGetRequest).toHaveBeenCalledWith(UserServiceService.USER_URL, null);
  });
});
 