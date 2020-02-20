import { Component, OnInit, Input, ViewContainerRef } from '@angular/core';
import { Location } from '@angular/common';
import { Task } from '../../task';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskDataService } from 'src/app/tasks/services/task-data.service';
import { ModalDialogService } from '@preeco-privacy/ngx-modal-dialog';
import { ConfirmationModalComponent } from '../../../modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.scss']
})
export class TaskDetailsComponent implements OnInit {
  @Input() task: Task;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private taskDataService: TaskDataService,
    private modalService: ModalDialogService,
    private viewRef: ViewContainerRef
  ) { }

  ngOnInit() {
    this.findById();
  }

  findById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.taskDataService.findById(id)
        .subscribe(task => this.task = task);
  }

  confirmDeletion(task: Task) {
    this.modalService.openDialog(this.viewRef, {
      childComponent: ConfirmationModalComponent,
      data: () => this.delete(task)
    });
  }

  delete(task: Task): void {
    this.taskDataService.delete(task)
    .subscribe(() => this.router.navigate(['project-details', task.projectId]));
  }

}
