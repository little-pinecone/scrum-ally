import { Component, OnInit, Input, Output, EventEmitter, ViewContainerRef } from '@angular/core';
import { Router } from '@angular/router';

import { Project } from '../../../project';
import { ProjectDataService } from '../../../services/project-data.service'
import { ModalDialogService } from '@preeco-privacy/ngx-modal-dialog';
import { ConfirmationModalComponent } from '../../../../modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-projects-table-actions',
  templateUrl: './projects-table-actions.component.html',
  styleUrls: ['./projects-table-actions.component.scss']
})
export class ProjectsTableActionsComponent implements OnInit {
  @Input() project: Project;
  @Output() projectDeleteEvent = new EventEmitter();

  constructor(
    private projectDataService: ProjectDataService,
    private router: Router,
    private modalService: ModalDialogService,
    private viewRef: ViewContainerRef
  ) { }

  ngOnInit() {
  }

  confirmDeletion(project: Project) {
    this.modalService.openDialog(this.viewRef, {
      childComponent: ConfirmationModalComponent,
      data: () => this.delete(project)
    });
  }

  delete(project: Project): void {
    this.projectDataService.delete(project)
      .subscribe(() =>  {
        this.projectDeleteEvent.emit(null);
      });
  }
}
