import { Component, OnInit } from '@angular/core';

import { Project } from '../../../project';
import { Page } from '../../../../pagination/page';
import { Pageable } from '../../../../pagination/pageable';
import { ProjectDataService } from '../../../services/project-data.service';
import { CustomPaginationService } from '../../../../pagination/services/custom-pagination.service';

@Component({
  selector: 'app-projects-table',
  templateUrl: './projects-table.component.html',
  styleUrls: ['./projects-table.component.scss']
})
export class ProjectsTableComponent implements OnInit {
  page: Page<Project> = new Page();

  constructor(
    private projectDataService: ProjectDataService,
    private paginationService: CustomPaginationService
  ) { }

  ngOnInit() {
    this.getData();
  }

  private getData(): void {
    this.projectDataService.getPage(this.page.pageable)
    .subscribe(page => this.page = page);
  }

  public getNextPage(): void {
    this.page.pageable = this.paginationService.getNextPage(this.page);
    this.getData();
  }

  public getPreviousPage(): void {
    this.page.pageable = this.paginationService.getPreviousPage(this.page);
    this.getData();
  }

  public getPageInNewSize(pageSize: number): void {
    this.page.pageable = this.paginationService.getPageInNewSize(this.page, pageSize);
    this.getData();
  }

  private updateProjectsList():void {
    this.page.pageable.pageNumber = Pageable.FIRST_PAGE_NUMBER;
    this.getData();
  }

}
