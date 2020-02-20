import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { ProjectsTableActionsComponent } from './projects-table-actions.component';
import { ModalDialogModule, ModalDialogInstanceService } from '@preeco-privacy/ngx-modal-dialog';

describe('ProjectsTableActionsComponent', () => {
  let component: ProjectsTableActionsComponent;
  let fixture: ComponentFixture<ProjectsTableActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectsTableActionsComponent ],
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
    fixture = TestBed.createComponent(ProjectsTableActionsComponent);
    component = fixture.componentInstance;
    component.project = {id:1, name: 'test1', description: ''};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
