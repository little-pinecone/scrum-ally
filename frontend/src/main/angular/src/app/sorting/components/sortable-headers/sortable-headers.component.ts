import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

import { SortableColumn} from "../../sortable-column";

@Component({
  selector: 'app-sortable-headers',
  templateUrl: './sortable-headers.component.html',
  styleUrls: ['./sortable-headers.component.scss']
})
export class SortableHeadersComponent implements OnInit {

  @Input() sortableColumns: Array<SortableColumn>;
  @Output() sortEvent: EventEmitter<SortableColumn> = new EventEmitter<SortableColumn>();

  constructor() { }

  ngOnInit() {
  }

  sort(column: SortableColumn): void {
    column.toggleDirection();
      this.sortEvent.emit(column);
  }
}
