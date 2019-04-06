import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable} from 'rxjs';

import { Project } from '../project' ;
import { Page } from '../../pagination/page' ;
import { Pageable } from '../../pagination/pageable' ;
import { SortableColumn } from '../../sorting/sortable-column' ;

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
const API_URL = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class ProjectDataService {
  private projectsUrl = API_URL + '/projects';

  constructor(private http: HttpClient) { }

  public save(project: Project): Observable<Project> {
    if (!project.id) {
      return this.http.post<Project>(this.projectsUrl, project, httpOptions);
    }
  }

  public getAll(): Observable<Page<Project>> {
    return this.http.get<Page<Project>>(this.projectsUrl, httpOptions);
  }

  public getPage(pageable: Pageable, sortableColumn: SortableColumn)
  : Observable<Page<Project>> {
    let url = this.projectsUrl
    + '?page=' + pageable.pageNumber
    + '&size=' + pageable.pageSize
    + this.getSortParameters(sortableColumn)
    return this.http.get<Page<Project>>(url, httpOptions);
  }

  private getSortParameters(sortableColumn: SortableColumn): string {
    if(sortableColumn == null) {
      return '&sort=id';
    }
    return '&sort=' + sortableColumn.name + ',' + sortableColumn.direction;
  }

  public findById(id: number): Observable<Project> {
    const url = `${this.projectsUrl}/${id}`;
    return this.http.get<Project>(url, httpOptions);
  }

  public update(project: Project): Observable<Project> {
    const id = typeof project === 'number' ? project : project.id;
    const url = `${this.projectsUrl}/${id}`;
    return this.http.put<Project>(url, project, httpOptions);
  }

  delete (project: Project | number): Observable<Project> {
    const id = typeof project === 'number' ? project : project.id;
    const url = `${this.projectsUrl}/${id}`;
    return this.http.delete<Project>(url, httpOptions);
  }
}
