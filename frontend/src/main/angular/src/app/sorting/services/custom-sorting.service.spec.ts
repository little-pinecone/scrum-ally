import {getTestBed, TestBed} from '@angular/core/testing';

import { CustomSortingService } from './custom-sorting.service';
import {SortableColumn} from "../sortable-column";

describe('CustomSortingService', () => {
  let injector: TestBed;
  let service: CustomSortingService;
  let sortableColumns: Array<SortableColumn>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CustomSortingService]
    });
    injector = getTestBed();
    service = injector.get(CustomSortingService);
    sortableColumns = [
      new SortableColumn('id', 'Id', null),
      new SortableColumn('name', 'Name', 'asc')
    ];
  });

  it('should be created', () => {
    const service: CustomSortingService = TestBed.get(CustomSortingService);
    expect(service).toBeTruthy();
  });

  it('should get a column chosen for sorting', () => {
    let result = service.getSortableColumn(sortableColumns);
    expect(result.name).toEqual('name');
  });

  it('should clear direction form previously chosen column, when new sorting is issued', () => {
    sortableColumns[0].direction = 'desc';
    service.clearPreviousSorting(sortableColumns[0], sortableColumns);
    expect(sortableColumns[0].name).toEqual('id');
    expect(sortableColumns[0].direction).toEqual('desc');
    expect(sortableColumns[1].name).toEqual('name');
    expect(sortableColumns[1].direction).toEqual(null);
  });

});
