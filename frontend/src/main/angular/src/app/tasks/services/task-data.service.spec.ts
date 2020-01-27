import {TestBed, inject, getTestBed} from '@angular/core/testing';

import {TaskDataService} from './task-data.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Task} from '../task';
import {environment} from '../../../environments/environment';
import {Pageable} from '../../pagination/pageable';
import {SortableColumn} from 'src/app/sorting/sortable-column';

describe('TaskDataService', () => {
  let injector: TestBed;
  let service: TaskDataService;
  let httpMock: HttpTestingController;
  let pageable: Pageable;
  let sortableColumn: SortableColumn;
  let apiTasksUrl: String;
  let expectedTask: Task;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TaskDataService],
      imports: [HttpClientTestingModule]
    });
    injector = getTestBed();
    service = injector.get(TaskDataService);
    httpMock = injector.get(HttpTestingController);
    pageable = new Pageable();
    sortableColumn = null;
    apiTasksUrl = environment.apiUrl + '/tasks';
    expectedTask = {
      "name": "task",
      "description": "",
      "id": 1,
      "projectId": 1
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', inject([TaskDataService], (service: TaskDataService) => {
    expect(service).toBeTruthy();
  }));

  it('should save a new task', () => {
    const task: Task = new Task();
    service.save(task).subscribe((tasks: any) => {
      expect(tasks).toEqual(expectedTask);
    });

    const req = httpMock.expectOne(`${apiTasksUrl}`);
    expect(req.request.method).toBe("POST");
    req.flush(expectedTask);
  });

  it('should retrieve page with paginated tasks', () => {
    let paginatedTasks = {
      "content": [
        {
          "name": "task",
          "description": "",
          "id": 1,
          "projectId": 1
        }
      ],
      "pageable": {
        "sort": {
          "sorted": false,
          "unsorted": true
        },
        "pageSize": 4,
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
      "size": 4,
      "number": 0
    };
    pageable.pageNumber = 0;
    pageable.pageSize = 4;
    service.getPage(1, pageable, sortableColumn).subscribe((tasks: any) => {
      expect(tasks).toEqual(paginatedTasks);
    });

    const req = httpMock.expectOne(`${apiTasksUrl}?page=0&size=4&sort=id&project-id=1`);
    expect(req.request.method).toBe("GET");
    req.flush(paginatedTasks);
  });

  it('should find a task by id', () => {
    service.findById(1).subscribe((task: any) => {
      expect(task.name).toBe('task');
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(expectedTask);
  });

  it('should fail to find a non existing task by id', () => {
    service.findById(100).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/100`);
    expect(req.request.method).toBe('GET');
    req.flush({status: 404, statusText: 'Not Found'});
  });

  it('should update a task', () => {
    service.update(expectedTask).subscribe((task: any) => {
      expect(task.name).toBe('task');
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/1`);
    expect(req.request.method).toBe('PUT');
    req.flush(expectedTask);
  });

  it('should fail to update a non existing task', () => {
    const task: Task = new Task();
    task.id = 100;
    service.update(task).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/100`);
    expect(req.request.method).toBe('PUT');
    req.flush({status: 404, statusText: 'Not Found'});
  });

  it('should delete a task by id', () => {
    service.delete(1).subscribe(() => {
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should fail to delete a non existing task', () => {
    service.delete(100).subscribe((error: any) => {
      expect(error.status).toEqual(404, 'status');
    });

    const req = httpMock.expectOne(`${apiTasksUrl}/100`);
    expect(req.request.method).toBe('DELETE');
    req.flush({status: 404, statusText: 'Not Found'});
  });
});
