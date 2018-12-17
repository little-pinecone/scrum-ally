import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';

import { GuestLayoutComponent } from './guest-layout.component';

@Component({selector: 'app-guest-nav', template: ''})
class GuestNavComponent {}

@Component({selector: 'app-page-content', template: ''})
class PageContentComponent { }

@Component({selector: 'app-guest-footer', template: ''})
class GuestFooterComponent {}


describe('GuestLayoutComponent', () => {
  let component: GuestLayoutComponent;
  let fixture: ComponentFixture<GuestLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ 
        GuestLayoutComponent,
        GuestNavComponent,
        PageContentComponent,
        GuestFooterComponent
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
