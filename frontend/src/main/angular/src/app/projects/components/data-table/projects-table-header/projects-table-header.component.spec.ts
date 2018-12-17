import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsTableHeaderComponent } from './projects-table-header.component';

describe('ProjectsTableHeaderComponent', () => {
  let component: ProjectsTableHeaderComponent;
  let fixture: ComponentFixture<ProjectsTableHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectsTableHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectsTableHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
