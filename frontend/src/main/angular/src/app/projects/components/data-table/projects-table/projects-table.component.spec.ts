import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, Input } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ProjectsTableComponent } from './projects-table.component';
import { Project } from '../../../project';
import { Page } from '../../../../pagination/page';
import { SortableColumn } from 'src/app/sorting/sortable-column';

@Component({selector: 'app-projects-table-header', template: ''})
class ProjectsTableHeaderComponent {}

@Component({selector: 'app-projects-table-actions', template: ''})
class ProjectsTableActionsComponent {
  @Input() project: Project;
}

@Component({selector: 'app-sortable-headers', template: ''})
class SortableHeadersComponent {
  @Input() sortableColumns: Array<SortableColumn>;
}

@Component({selector: 'app-custom-pagination', template: ''})
class CustomPaginationComponent {
  @Input() page: Page<Project>;
}

describe('ProjectsTableComponent', () => {
  let component: ProjectsTableComponent;
  let fixture: ComponentFixture<ProjectsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProjectsTableComponent,
        ProjectsTableHeaderComponent,
        ProjectsTableActionsComponent,
        SortableHeadersComponent,
        CustomPaginationComponent
      ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
     ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
