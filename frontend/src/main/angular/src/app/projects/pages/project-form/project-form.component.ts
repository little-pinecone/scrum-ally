import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Project } from '../../project' ;
import { ProjectDataService } from '../../services/project-data.service'

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.scss']
})
export class ProjectFormComponent implements OnInit {
  project: Project = new Project();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private projectDataService: ProjectDataService
  ) { }

  ngOnInit() {
    this.findById();
  }

  findById(): void {
    this.route.params.forEach((params: Params) => {
      if (params['id'] !== undefined) {
        const id = +this.route.snapshot.paramMap.get('id');
        this.projectDataService.findById(id)
            .subscribe(project => this.project = project);
      }
    });
  }

  saveProject(project) {
    this.projectDataService.save(project as Project)
        .subscribe(() => this.router.navigate(['projects']));
  }

  updateProject(project) {
    this.projectDataService.update(project as Project)
        .subscribe(() => this.router.navigate(['project-details', project.id]));
  }
}
