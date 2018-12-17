import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';

import { AuthorisedLayoutComponent } from './authorised-layout.component';

@Component({selector: 'app-authorised-top-nav', template: ''})
class AuthorisedTopNavComponent {}

@Component({selector: 'app-authorised-side-nav', template: ''})
class AuthorisedSideNavComponent {}

@Component({selector: 'app-page-content', template: ''})
class PageContentComponent { }

describe('AuthorisedLayoutComponent', () => {
  let component: AuthorisedLayoutComponent;
  let fixture: ComponentFixture<AuthorisedLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AuthorisedLayoutComponent,
        AuthorisedTopNavComponent,
        AuthorisedSideNavComponent,
        PageContentComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorisedLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
