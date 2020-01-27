import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { environment } from '../../../environments/environment';

import { TokenService } from './token.service';
import { Credentials } from '../credentials';
import { HttpResponse, HttpHeaders } from '@angular/common/http';

describe('TokenService', () => {
  let injector: TestBed;
  let service: TokenService;
  let httpMock: HttpTestingController;
  let apiUrl = environment.apiUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TokenService],
      imports: [ HttpClientTestingModule ]
    });
    injector = getTestBed();
    service = injector.get(TokenService);
    httpMock = injector.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call login on API', () => {
    service.getResponseHeaders(new Credentials('', ''))
    .subscribe((res: HttpResponse<any>) => {
      expect(res.headers.get('authorization')).toEqual('bearer token');
    });

    let HttpResponseMock: HttpResponse<any> = {
      body: null,
      type: null,
      clone:null,
      status: 200,
      statusText: null,
      url: null,
      ok: true,
      headers: new HttpHeaders({ 'authorization': 'bearer token' })
    };

    const req = httpMock.expectOne(apiUrl + '/login');
    expect(req.request.method).toBe("POST");
    req.flush({}, HttpResponseMock);
  });

  it('should call logout on API', () => {
    service.logout().subscribe(() => {});

    const req = httpMock.expectOne(apiUrl + '/logout');
    expect(req.request.method).toBe("GET");
    req.flush("");
  });

});
