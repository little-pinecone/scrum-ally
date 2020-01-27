import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ProjectDataService } from './project-data.service';
import { Project } from '../project';
import { Pageable } from '../../pagination/pageable';
import { environment } from '../../../environments/environment.prod';
import { SortableColumn } from 'src/app/sorting/sortable-column';
import {Page} from "../../pagination/page";

describe('ProjectDataService', () => {
  let injector: TestBed;
  let service: ProjectDataService;
  let httpMock: HttpTestingController;
  let apiProjectsUrl: String;
  let projectList: Array<any>;
  let pageable: Pageable;
  let sortableColumn: SortableColumn;
  let paginatedProjects: Page<any>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProjectDataService],
      imports: [HttpClientTestingModule]
    });
    injector = getTestBed();
    service = injector.get(ProjectDataService);
    httpMock = injector.get(HttpTestingController);
    apiProjectsUrl = environment.apiUrl + '/projects';
    projectList = [
      {"name":"test1", "description":"test1", "id":1},
      {"name":"test2", "id":2}
    ];
    pageable = new Pageable();
    sortableColumn = null;
    paginatedProjects = {
      "content": [
        {
          "name": "test_name_1",
          "description": "test_description",
          "id": 1
        }
      ],
      "pageable": {
        "sort": {
          "sorted": false,
          "unsorted": true
        },
        "pageSize": 20,
        "pageNumber": 0,
        "offset": 0,
        "unpaged": false,
        "paged": true
      },
      "last": true,
      "totalElements": 1,
      "totalPages": 1,
      "first": true,
      "sort": {
        "sorted": false,
        "unsorted": true
      },
      "numberOfElements": 1,
      "size": 20,
      "number": 0
    }
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should save a new project', () => {
    const project: Project = new Project();
    service.save(project).subscribe((projects: any) => {
      expect(projects).toEqual(projectList[0]);
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}`);
    expect(req.request.method).toBe("POST");
    req.flush(projectList[0]);
  });

  it('should retrieve all projects', () => {
    service.getAll().subscribe((projects: any) => {
      expect(projects).toEqual(projectList);
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}`);
    expect(req.request.method).toBe("GET");
    req.flush(projectList);
  });

  it('should retrieve default page with paginated projects', () => {
    service.getPage(pageable, sortableColumn).subscribe((projects: any) => {
      expect(projects).toEqual(paginatedProjects);
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}?page=0&size=3&sort=id`);
    expect(req.request.method).toBe("GET");
    req.flush(paginatedProjects);
  });

  it('should retrieve page with paginated projects', () => {
    pageable.pageNumber = 1;
    pageable.pageSize = 4;
    service.getPage(pageable, sortableColumn).subscribe((projects: any) => {
      expect(projects).toEqual(paginatedProjects);
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}?page=1&size=4&sort=id`);
    expect(req.request.method).toBe("GET");
    req.flush(paginatedProjects);
  });

  it('should find a project by id', () => {
    service.findById(1).subscribe((project: any) => {
      expect(project.name).toBe('test1');
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(projectList[0]);
  });

  it('should fail to find a non existing project by id', () => {
    service.findById(100).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/100`);
    expect(req.request.method).toBe('GET');
    req.flush({ status: 404, statusText: 'Not Found' });
  });

  it('should update a project', () => {
    const project: Project = new Project();
    project.id = 1;
    service.update(project).subscribe((project: any) => {
      expect(project.name).toBe('test1');
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(projectList[0]);
  });

  it('should fail to update a non existing project', () => {
    const project: Project = new Project();
    project.id = 100;
    service.update(project).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/100`);
    expect(req.request.method).toBe('PUT');
    req.flush({ status: 404, statusText: 'Not Found' });
  });

  it('should delete a project by id', () => {
    service.delete(1).subscribe(() => {
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should fail to delete a non existing project', () => {
    service.delete(100).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiProjectsUrl}/100`);
    expect(req.request.method).toBe('DELETE');
    req.flush({ status: 404, statusText: 'Not Found' });
  });

});
