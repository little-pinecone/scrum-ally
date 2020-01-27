import { TestBed, getTestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { Credentials } from 'src/app/auth/credentials';
import { TokenService } from 'src/app/auth/services/token.service';
import { Observable } from 'rxjs';
import {HttpResponse} from "@angular/common/http";

describe('AuthService', () => {
  let injector: TestBed;
  let service: AuthService;
  let tokenService: TokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService
       ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ]
    });
    injector = getTestBed();
    service = injector.get(AuthService);
    tokenService = injector.get(TokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should have loggedIn option set to false by default', () => {
    expect(service.isLoggedIn()).toEqual(false);
  });

  it('should have redirectToUrl option set to dashboard by default', () => {
    expect(service.redirectToUrl).toEqual('/dashboard');
  });

  it('should return false when user is logged out', () => {
    spyOn(tokenService, 'logout').and.returnValue(new Observable<string>());
    service.logout();
    expect(service.isLoggedIn()).toEqual(false);
  });

  it('should return true when user is logged in', () => {
    spyOn(tokenService, 'getResponseHeaders').and.returnValue(new Observable<HttpResponse<Object>>());
    service.login(new Credentials('',''));
    expect(tokenService.getResponseHeaders).toHaveBeenCalled();
  });
});
