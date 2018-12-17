import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { TasksTableHeaderComponent } from './tasks-table-header.component';

describe('TasksTableHeaderComponent', () => {
  let component: TasksTableHeaderComponent;
  let fixture: ComponentFixture<TasksTableHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TasksTableHeaderComponent ],
      imports: [
        RouterTestingModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksTableHeaderComponent);
    component = fixture.componentInstance;
    component.project = {id:1, name: 'test1', description: ''};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
