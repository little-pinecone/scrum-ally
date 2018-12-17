import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { TaskDetailsComponent } from './task-details.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ModalDialogInstanceService } from 'ngx-modal-dialog/src/modal-dialog-instance.service';
import { ModalDialogModule } from 'ngx-modal-dialog';

describe('TaskDetailsComponent', () => {
  let component: TaskDetailsComponent;
  let fixture: ComponentFixture<TaskDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TaskDetailsComponent ],
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
    fixture = TestBed.createComponent(TaskDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
