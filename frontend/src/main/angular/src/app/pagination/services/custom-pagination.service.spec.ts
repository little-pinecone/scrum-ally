import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { CustomPaginationService } from './custom-pagination.service';
import { Page } from '../page';
import { Pageable } from '../pageable';

describe('CustomPaginationService', () => {
  let injector: TestBed;
  let service: CustomPaginationService;
  let page: Page<any>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CustomPaginationService],
      imports: [HttpClientTestingModule]
    });
    injector = getTestBed();
    service = injector.get(CustomPaginationService);
    page = new Page();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should increment page number', () => {
    page.last = false;
    page.pageable.pageNumber = 0;
    let result = service.getNextPage(page);
    expect(result.pageNumber).toEqual(1);
  });

  it('should not increment page number if it is currently on the last page', () => {
    page.last = true;
    page.pageable.pageNumber = 0;
    let result = service.getNextPage(page);
    expect(result.pageNumber).toEqual(0);
  });

  it('should decrement page number', () => {
    page.first = false;
    page.pageable.pageNumber = 1;
    let result = service.getPreviousPage(page);
    expect(result.pageNumber).toEqual(0);
  });

  it('should not decrement page number if it is currently on the first page', () => {
    page.first = true;
    page.pageable.pageNumber = Pageable.FIRST_PAGE_NUMBER;
    let result = service.getPreviousPage(page);
    expect(result.pageNumber).toEqual(Pageable.FIRST_PAGE_NUMBER);
  });

  it('should get page in new size', () => {
    page.pageable.pageSize = 10;
    let result = service.getPageInNewSize(page, 20);
    expect(result.pageSize).toEqual(20);
  });

  it('should refresh page number when getting page in new size', () => {
    page.pageable.pageSize = 10;
    page.pageable.pageNumber = 1;
    let result = service.getPageInNewSize(page, 20);
    expect(result.pageSize).toEqual(20);
    expect(result.pageNumber).toEqual(Pageable.FIRST_PAGE_NUMBER);
  });

});
