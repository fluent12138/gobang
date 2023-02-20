import { AcGameObject } from "../AcGameObject";
import { Cell } from './Cell'
export class Gobang extends AcGameObject {
  constructor(info, gameMap) {
    super();
    this.id = info.id;
    this.color = info.color;
    this.gameMap = gameMap;
    this.cells = [];
  }

  start () {
  }

  update () {
    this.render();
  }

  put (x, y) {
    this.cells.push(new Cell(x, y));
  }


  set_cells (cells) {
    let t = [];
    let new_cells = [];
    t = cells.split(' ');
    for (let i = 0; i < t.length - 1; i++) {
      let [x, y] = t[i].split(',');
      new_cells.push(new Cell(parseInt(x), parseInt(y)));
    }
    this.cells = new_cells;
  }

  render () {
    const L = this.gameMap.L;
    const ctx = this.gameMap.ctx;

    ctx.fillStyle = this.color;

    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, L * 0.4, 0, Math.PI * 2);
      ctx.fill();
      if (cell.r === this.gameMap.put_position[0] && cell.c === this.gameMap.put_position[1]) {
        ctx.fillStyle = "red";
        ctx.beginPath();
        ctx.arc(cell.x * L, cell.y * L, L * 0.1, 0, Math.PI * 2);
        ctx.fill();
      }
    }
  }
}