import { defineStore } from 'pinia';

export const useGobangStore = defineStore('gobang', {
  state: () => ({
    status: "",
    socket: null,
    gameObejct: null,
    gamemap: null,
    round: false, // 谁的回合
    opponent_username: "",
    opponent_photo: "",
    opponent_rate: null,
    opponent_ready_status: "未准备",
    a_id: 0,
    b_id: 0,
    loser: "none", //none, all, A, B
  }),
  actions: {
    updateSocket (socket) {
      this.socket = socket;
    },
    updateGameObject (gameObject) {
      this.gameObejct = gameObject;
    },
    updateGameStartInfo (gameInfo) {
      this.gamemap = gameInfo.map;
      this.a_id = gameInfo.a_id;
      this.b_id = gameInfo.b_id;
      this.round = gameInfo.round;
      this.opponent_username = gameInfo.opponent_username;
      this.opponent_photo = gameInfo.opponent_photo;
      this.opponent_rate = gameInfo.opponent_rate;
    },
    updateGameMap (map) {
      this.gamemap = map;
    },
    updateRound (round) {
      this.round = round;
    },
    updateLoser (loser) {
      this.loser = loser;
    },
    clearGobangInfo () {
      this.status = "";
      this.gameObejct = null;
      this.gamemap = null;
      this.round = false; // 谁的回合
      this.opponent_username = "";
      this.opponent_photo = "";
      this.opponent_rate = null;
      this.opponent_ready_status = "未准备";
      this.a_id = 0;
      this.b_id = 0;
      this.loser = "none"; //none, all, A, B
    }
  },
});
