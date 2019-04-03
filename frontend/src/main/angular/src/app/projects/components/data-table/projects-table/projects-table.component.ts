import { Component, OnInit } from '@angular/core';

import { Project } from '../../../project';
import { Page } from '../../../../pagination/page';
import { Pageable } from '../../../../pagination/pageable';
import { ProjectDataService } from '../../../services/project-data.service';
import { CustomPaginationService } from '../../../../pagination/services/custom-pagination.service';
import { SortableColumn } from 'src/app/sorting/sortable-column';
import {CustomSortingService} from "../../../../sorting/services/custom-sorting.service";

@Component({
  selector: 'app-projects-table',
  templateUrl: './projects-table.component.html',
  styleUrls: ['./projects-table.component.scss']
})
export class ProjectsTableComponent implements OnInit {
  page: Page<Project> = new Page();
  sortableColumns: Array<SortableColumn> = [
    new SortableColumn('name', 'Name', 'asc')
  ];

  constructor(
    private projectDataService: ProjectDataService,
    private paginationService: CustomPaginationService,
    private sortingService: CustomSortingService
  ) { }

  ngOnInit() {
    this.getData();
  }

  private getData(): void {
    let column = this.sortingService.getSortableColumn(this.sortableColumns);
    this.projectDataService.getPage(this.page.pageable, column)
    .subscribe(page => this.page = page);
  }

  public sort(sortableColumn: SortableColumn): void {
    this.sortingService.clearPreviousSorting(sortableColumn, this.sortableColumns);
    this.getData();
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
