import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpHeaders, HttpClient } from '@angular/common/http';

import { Task } from '../task';
import { Observable } from 'rxjs';
import { Page } from '../../pagination/page';
import { Pageable} from '../../pagination/pageable';
import { SortableColumn } from 'src/app/sorting/sortable-column';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const API_URL = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class TaskDataService {
  private tasksUrl = API_URL + '/tasks';

  constructor(private http: HttpClient) { }

  public save(task: Task) {
    if(!task.id) {
      const url = `${this.tasksUrl}`;
      return this.http.post<Task>(url, task, httpOptions);
    }
  }

  public getPage(projectId: number, pageable: Pageable, sortableColumn: SortableColumn)
  : Observable<Page<Task>> {
    let url = this.tasksUrl
    + '?page=' + pageable.pageNumber
    + '&size=' + pageable.pageSize
    + this.getSortParameters(sortableColumn)
    + '&project-id=' + projectId;
    return this.http.get<Page<Task>>(url, httpOptions);
  }

  private getSortParameters(sortableColumn: SortableColumn): string {
    if(sortableColumn == null) {
      return '&sort=id';
    }
    return '&sort=' + sortableColumn.name + ',' + sortableColumn.direction;
  }

  public findById(id: number): Observable<Task> {
    const url = `${this.tasksUrl}/${id}`;
    return this.http.get<Task>(url, httpOptions);
  }

  public update(task: Task): Observable<Task> {
    const id = typeof task === 'number' ? task : task.id;
    const url = `${this.tasksUrl}/${id}`;
    return this.http.put<Task>(url, task, httpOptions);
  }

  delete (task: Task | number): Observable<Task> {
    const id = typeof task === 'number' ? task : task.id;
    const url = `${this.tasksUrl}/${id}`;
    return this.http.delete<Task>(url, httpOptions);
  }
}
