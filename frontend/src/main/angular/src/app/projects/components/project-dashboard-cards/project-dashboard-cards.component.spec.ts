import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ProjectDashboardCardsComponent } from './project-dashboard-cards.component';

describe('ProjectDashboardCardsComponent', () => {
  let component: ProjectDashboardCardsComponent;
  let fixture: ComponentFixture<ProjectDashboardCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectDashboardCardsComponent ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
     ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectDashboardCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
