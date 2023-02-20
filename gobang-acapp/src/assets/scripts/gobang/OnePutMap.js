import { AcGameObject } from "../AcGameObject";
import { Cell } from './Cell';
import notice from '../../../utils/notice'
export class OnePutMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.L = 0;
    this.rows = 12;
    this.cols = 12;
    this.store = store;
    this.g = []; // 0空 1黑 2白
  }

  start () {
    this.init_map();
    this.add_listening_events();
  }

  set_put_position (x, y) {
    this.put_position = [x, y];
  }

  add_listening_events () {
    this.ctx.canvas.focus();
    let k = 1;
    this.ctx.canvas.addEventListener("click", e => {
      //计算x坐标
      var actualLeft = this.parent.children[0].offsetLeft;
      var current = this.parent.children[0].offsetParent;
      while (current !== null) {
        actualLeft += current.offsetLeft;
        current = current.offsetParent;
      }
      //计算y坐标
      var actualTop = this.parent.children[0].offsetTop;
      var current = this.parent.children[0].offsetParent;
      while (current !== null) {
        actualTop += (current.offsetTop + current.clientTop);
        current = current.offsetParent;
      }
      let x = parseInt((e.clientY - actualTop) / this.L);
      let y = parseInt((e.clientX - actualLeft) / this.L);
      if (k % 2 === 1) {
        if (this.check_vaild(x, y)) {
          this.put(x, y, 1);
          k++;
        }
      } else if (k % 2 === 0) {
        if (this.check_vaild(x, y)) {
          this.put(x, y, 2);
          k++;
        }
      }
    })

  }

  init_map () {
    if (this.store.levelObject !== null) {
      let map = " " + this.store.levelObject.map;
      let t = [];
      for (let i = 1; i < map.length; i++) {
        t.push(parseInt(map[i]));
        if (i % this.cols === 0) {
          this.g.push(t);
          t = [];
        }
      }
    } else {
      for (let i = 0; i < this.rows; i++) {
        let t = [];
        for (let j = 0; j < this.cols; j++) {
          t.push(0);
        }
        this.g.push(t);
      }
    }
  }

  getMapString () {
    let map = "";
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        map += this.g[i][j];
      }
    }
    return map;
  }

  check_vaild (x, y) {
    if (this.g[x][y] === 0) {
      return true;
    } else {
      notice("这个格子已经下棋了哟", 1500, "warning");
      return false;
    }
  }

  // 模拟下棋
  put (x, y, id) {
    this.g[x][y] = id;
  }

  update_size () {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  update () {
    this.update_size();
    this.render();
  }

  render () {
    const color_even = "#a7aea7", color_odd = "#979e97";
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(this.L * c, this.L * r, this.L, this.L);

        let cell = new Cell(r, c);
        if (this.g[r][c] === 1) {
          this.ctx.fillStyle = "black";
        } else if (this.g[r][c] === 2) {
          this.ctx.fillStyle = "white";
        }
        this.ctx.beginPath();
        this.ctx.arc(cell.x * this.L, cell.y * this.L, this.L * 0.4, 0, Math.PI * 2);
        this.ctx.fill();
      }
    }
  }
}