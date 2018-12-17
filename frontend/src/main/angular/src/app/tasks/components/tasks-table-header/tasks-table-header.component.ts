import { Component, OnInit, Input } from '@angular/core';
import { Project } from '../../../projects/project';

@Component({
  selector: 'app-tasks-table-header',
  templateUrl: './tasks-table-header.component.html',
  styleUrls: ['./tasks-table-header.component.scss']
})
export class TasksTableHeaderComponent implements OnInit {
  @Input() project:Project;

  constructor() { }

  ngOnInit() {
  }

}
