import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { TasksTableActionsComponent } from './tasks-table-actions.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModalDialogModule, ModalDialogInstanceService } from '@preeco-privacy/ngx-modal-dialog';

describe('TasksTableActionsComponent', () => {
  let component: TasksTableActionsComponent;
  let fixture: ComponentFixture<TasksTableActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TasksTableActionsComponent ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ModalDialogModule
     ],
     providers: [ModalDialogInstanceService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksTableActionsComponent);
    component = fixture.componentInstance;
    component.task = {id:1, name: 'test1', description: '', projectId: 1};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
