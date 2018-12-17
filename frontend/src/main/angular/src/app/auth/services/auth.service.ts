import { Injectable } from '@angular/core';
import { Observable, of, Subscription } from 'rxjs';
import { tap, delay } from 'rxjs/operators';
import { Router } from '@angular/router';

import { TokenService } from './token.service'
import { Credentials } from '../credentials';
import { HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  static readonly TOKEN_STORAGE_KEY = 'token';
  redirectToUrl: string = '/dashboard';

  constructor(private router: Router, private tokenService: TokenService) { }

  public isLoggedIn(): boolean {
    return !!this.getToken();
  }

  public login(credentials: Credentials): void {
    this.tokenService.getResponseHeaders(credentials)
    .subscribe((res: HttpResponse<any>) => {
      this.saveToken(res.headers.get('authorization'));
      this.router.navigate([this.redirectToUrl]);
    });
  }

  private saveToken(token: string){
    localStorage.setItem(AuthService.TOKEN_STORAGE_KEY, token);
  }

  public logout(): void {
    this.tokenService.logout()
    .subscribe(() =>{
      localStorage.removeItem(AuthService.TOKEN_STORAGE_KEY);
    });
  }

  public getToken(): string {
    return localStorage.getItem(AuthService.TOKEN_STORAGE_KEY);
  }
}
