import { Component, OnInit } from '@angular/core';

import { TaskDataService } from '../../services/task-data.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { Task } from '../../task';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.scss']
})
export class TaskFormComponent implements OnInit {
  task:Task = new Task();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskDataService: TaskDataService
  ) { }

  ngOnInit() {
    this.findById();
  }

  saveTask(task:Task) {
    this.route.params.forEach((params: Params) => {
      if (params['project-id'] !== undefined) {
        const projectId = +this.route.snapshot.paramMap.get('project-id');
        task.projectId = projectId;
        this.taskDataService.save(task)
            .subscribe(() => this.router.navigate(['project-details', projectId]));
      }
    });
  }

  findById(): void {
    this.route.params.forEach((params: Params) => {
      if (params['id'] !== undefined) {
        const id = +this.route.snapshot.paramMap.get('id');
        this.taskDataService.findById(id)
            .subscribe(task => this.task = task);
      }
    });
  }

  updateTask(task) {
    this.route.queryParams.subscribe(params => {
        this.taskDataService.update(task)
            .subscribe(() => this.router.navigate(['project-details', task.projectId]));
    });
  }

}
