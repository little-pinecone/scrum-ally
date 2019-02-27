import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { TasksTableComponent } from './tasks-table.component';
import { Component, Input } from '@angular/core';
import { Project } from '../../../projects/project';
import { Page } from '../../../pagination/page';
import { Task } from '../../task';
import { SortableColumn } from 'src/app/sorting/sortable-column';

@Component({selector: 'app-tasks-table-header', template: ''})
class TasksTableHeaderComponent {
  @Input() project: Project;
}

@Component({selector: 'app-tasks-table-actions', template: ''})
class TasksTableActionsComponent {
  @Input() task: Task;
}

@Component({selector: 'app-sortable-headers', template: ''})
class SortableHeadersComponent {
  @Input() sortableColumns: Array<SortableColumn>;
}

@Component({selector: 'app-custom-pagination', template: ''})
class CustomPaginationComponent {
  @Input() page: Page<Task>;
}

describe('TasksTableComponent', () => {
  let component: TasksTableComponent;
  let fixture: ComponentFixture<TasksTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        TasksTableComponent,
        TasksTableHeaderComponent,
        TasksTableActionsComponent,
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
    fixture = TestBed.createComponent(TasksTableComponent);
    component = fixture.componentInstance;
    component.project = {id:1, name: 'test1', description: ''};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
