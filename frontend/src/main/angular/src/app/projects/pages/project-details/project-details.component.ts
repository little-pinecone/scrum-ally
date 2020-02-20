import { Component, OnInit, Input, ViewContainerRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Project } from '../../project' ;
import { ProjectDataService } from '../../services/project-data.service'
import { ModalDialogService } from '@preeco-privacy/ngx-modal-dialog';
import { ConfirmationModalComponent } from '../../../modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.scss']
})
export class ProjectDetailsComponent implements OnInit {
  @Input() project: Project;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private projectDataService: ProjectDataService,
    private modalService: ModalDialogService,
    private viewRef: ViewContainerRef
  ) { }

  ngOnInit() {
    this.findById();
  }

  findById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projectDataService.findById(id)
        .subscribe(project => this.project = project);
  }

  confirmDeletion(project: Project) {
    this.modalService.openDialog(this.viewRef, {
      childComponent: ConfirmationModalComponent,
      data: () => this.delete(project)
    });
  }

  delete(project: Project): void {
    this.projectDataService.delete(project)
    .subscribe(() => this.router.navigate(['projects']));
  }
}
