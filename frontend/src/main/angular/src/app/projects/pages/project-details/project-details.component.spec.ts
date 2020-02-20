import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ProjectDetailsComponent } from './project-details.component';
import { Component, Input } from '@angular/core';
import { Project } from '../../project';
import { ModalDialogModule, ModalDialogInstanceService } from '@preeco-privacy/ngx-modal-dialog';

@Component({selector: 'app-tasks-table', template: ''})
class TasksTableComponent {
  @Input() project: Project;
}

describe('ProjectDetailsComponent', () => {
  let component: ProjectDetailsComponent;
  let fixture: ComponentFixture<ProjectDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ProjectDetailsComponent,
        TasksTableComponent
       ],
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
    fixture = TestBed.createComponent(ProjectDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
