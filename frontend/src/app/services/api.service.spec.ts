import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiService } from './api.service';
import { HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import du module de test HTTP
      providers: [ApiService] // Ajout du service dans les providers
    });

    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas d'appels HTTP en attente
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  /**  Test : Vérifier un appel GET */
  it('devrait envoyer une requête GET et retourner des données', () => {
    const mockData = { message: 'Succès' };
    const url = ApiService.DEFAULT_URL + 'test';

    service.sendGetRequest(url, null).subscribe((response) => {
      expect(response).toEqual(mockData);
    });

    const req = httpMock.expectOne(url);
    expect(req.request.method).toBe('GET');
    req.flush(mockData);
  });

  /** Test : Vérifier un appel POST */
  it('devrait envoyer une requête POST et retourner une réponse', () => {
    const mockResponse = { success: true };
    const url = ApiService.DEFAULT_URL + 'post-test';
    const params = new HttpParams().set('param1', 'value1');

    service.sendPostRequest(url, params, null).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(url);
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });
});
