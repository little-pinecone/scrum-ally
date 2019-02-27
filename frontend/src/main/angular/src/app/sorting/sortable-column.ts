export class SortableColumn {
  name: string;
  title: string;
  direction: string;

  public constructor(name: string, title: string, direction: string) {
    this.name = name;
    this.title = title;
    this.direction = direction;
  }

  public toggleDirection() {
    if(this.direction == 'desc') {
      this.direction = null;
    } else if(this.direction == 'asc') {
      this.direction = 'desc';
    } else {
      this.direction = 'asc';
    }
  }
}
