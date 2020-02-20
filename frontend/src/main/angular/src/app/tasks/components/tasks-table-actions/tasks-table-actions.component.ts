import { Component, OnInit, Output, Input, EventEmitter, ViewContainerRef } from '@angular/core';
import { Task } from '../../task';
import { Router } from '@angular/router';
import { ModalDialogService } from '@preeco-privacy/ngx-modal-dialog';
import { TaskDataService } from '../../services/task-data.service';
import { ConfirmationModalComponent } from '../../../modals/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-tasks-table-actions',
  templateUrl: './tasks-table-actions.component.html',
  styleUrls: ['./tasks-table-actions.component.scss']
})
export class TasksTableActionsComponent implements OnInit {
  @Input() task: Task;
  @Output() taskDeleteEvent = new EventEmitter();

  constructor(
    private taskDataService: TaskDataService,
    private router: Router,
    private modalService: ModalDialogService,
    private viewRef: ViewContainerRef
  ) { }

  ngOnInit() {
  }

  confirmDeletion(task: Task) {
    this.modalService.openDialog(this.viewRef, {
      childComponent: ConfirmationModalComponent,
      data: () => this.delete(task)
    });
  }

  delete(task: Task): void {
    this.taskDataService.delete(task)
      .subscribe(() =>  {
        this.taskDeleteEvent.emit(null);
      });
  }

}
