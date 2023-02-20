
export class Cell {
  constructor(r, c) {
    this.r = r; // x
    this.c = c; // y
    this.x = c + 0.5; // canvas x
    this.y = r + 0.5; // canvas y
  }
}