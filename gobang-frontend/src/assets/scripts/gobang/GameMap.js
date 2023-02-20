import { AcGameObject } from "../AcGameObject";
import { Gobang } from './Gobang';
import { Cell } from "./Cell";
import { useRouterStore, useOnePutStore } from "../../../store";
import OnePut from "../../../api/oneput";
import notice from '../../../utils/notice'
import back from "../../../utils/routerBack";
import WsEvent from '../../../utils/WsEvent'
export class GameMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.store = store;
    this.L = 0;
    this.rows = 12;
    this.cols = 12;
    this.ws_event = new WsEvent();
    this.g = []; // 0空 1黑 2白
    this.gobang = [
      new Gobang({ id: 1, color: "black" }, this),
      new Gobang({ id: 2, color: "white" }, this),
    ];
    this.put_position = [];
    this.routerStore = useRouterStore();
    this.onePutStore = useOnePutStore();
    this.oneput = false;
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
    this.ctx.canvas.addEventListener("click", e => {
      let x = parseInt((e.clientY - this.parent.children[0].offsetTop) / this.L);
      let y = parseInt((e.clientX - this.parent.children[0].offsetLeft) / this.L);
      if (this.routerStore.router_name === "one_put_game_level") {
        if (this.check_vaild(x, y) && !this.oneput && !this.onePutStore.over) {
          this.one_put(x, y);
          this.checkAns(x, y);
        }
        return;
      }
      if (this.store.round && this.check_vaild(x, y)) {
        const message = JSON.stringify({
          event: this.ws_event.PUT,
          x,
          y,
        })
        this.store.socket.send(message)
      }
    })
  }
  checkAns (x, y) {
    let res = OnePut.checkAns({
      id: this.onePutStore.levelId,
      x,
      y,
      passTime: 10 - this.onePutStore.passTime,
    })
    if (this.onePutStore.over) return false;
    this.onePutStore.updateOver(true);
    res.then(resp => {
      let data = resp.data;
      if (data.code === 0) {
        if (data.data) {
          notice("Accepted！", 2e3, "success");
        } else {
          notice("wrong answer~", 2e3, "warning");
        }
        setTimeout(() => {
          notice("正在跳转..", 1500, "success");
          setTimeout(() => {
            back();
          }, 1000);
        }, 2000);
      } else {
        let notice_msg = data.message;
        if (data.description !== '') {
          notice_msg = data.description;
        }
        notice(notice_msg, 2e3, "error");
      }
    }).catch((resp) => {
      console.log(resp);
      notice("系统繁忙", 2e3, "error");
    })
  }
  init_map () {
    if (this.routerStore.router_name === "one_put_game_level") {
      this.generateMap();
    } else {
      this.g = this.store.gamemap;
    }
  }

  generateMap () {
    this.g = []
    let t = [];
    let map = " " + this.onePutStore.gamemap;
    for (let i = 1; i < map.length; i++) {
      t.push(parseInt(map[i]));
      if (i % this.cols === 0) {
        this.g.push(t);
        t = [];
      }
    }
  }

  play_audio () {
    this.parent.children[1].play();
  }
  check_vaild (x, y) {
    if (this.g[x][y] === 0) {
      return true;
    } else {
      notice("这个格子已经下棋了哟", 1500, "warning");
      return false;
    }
  }

  //todo 判断是否结束
  check_finished () {
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        for (let k = 0; k < 8; k++) {
          if (this.g[i][j] && this.dfs(i, j, k)) return true;
        }
      }
    }
    return false;
  }

  dfs (x, y, d) {
    const dx = [0, 1, 0, -1, 1, 1, -1, -1];
    const dy = [1, 0, -1, 0, -1, 1, 1, -1];
    let color = this.g[x][y];
    for (let i = 0; i < 4; i++) {
      x += dx[d], y += dy[d];
      if (x < 0 || x >= this.rows || y < 0 || y >= this.cols || this.g[x][y] !== color) return false;
    }
    return true;
  }
  // 模拟下棋
  put (x, y, id) {
    const [gobang1, gobang2] = this.gobang;
    this.g[x][y] = id;
    if (id === 1) {
      gobang1.put(x, y);
    } else {
      gobang2.put(x, y);
    }
  }
  one_put (x, y) {
    const [gobang1, gobang2] = [... this.gobang];
    let color = [...this.getPutColor()];
    if (color[0] === color[1]) gobang1.put(x, y);
    else gobang2.put(x, y);
    this.set_put_position(x, y);
    this.play_audio();
    this.oneput = true;
  }
  getPutColor () {
    let blackCount = 0, whiteCount = 0;
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        if (this.g[i][j] === 1) {
          blackCount++;
        } else if (this.g[i][j] === 2) {
          whiteCount++;
        }
      }
    }
    return [blackCount, whiteCount];
  }
  update_size () {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }
  refresh_map () {
    if (this.routerStore.router_name === "one_put_game_level") {
      this.generateMap();
    } else {
      this.g = this.store.gamemap;
    }
  }
  update () {
    this.update_size();
    this.refresh_map();
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
        if (this.routerStore.router_name === "one_put_game_level") {
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
}