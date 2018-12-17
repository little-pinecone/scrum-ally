import { Component, OnInit } from '@angular/core';

import { Project } from '../../project';
import { Page } from '../../../pagination/page';
import { ProjectDataService } from '../../services/project-data.service';

@Component({
  selector: 'app-project-dashboard-cards',
  templateUrl: './project-dashboard-cards.component.html',
  styleUrls: ['./project-dashboard-cards.component.scss']
})
export class ProjectDashboardCardsComponent implements OnInit {
  projects: Project[];
  page: Page<Project>;

  constructor(private projectDataService: ProjectDataService) { }

  ngOnInit() {
    this.getProjects();
  }

  private getProjects():void {
    this.projectDataService.getAll()
    .subscribe(page => this.projects = page.content);
  }

}
