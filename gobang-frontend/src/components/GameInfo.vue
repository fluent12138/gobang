<template>
  <n-space justify="space-around" class="game-info-display">
    <NAvatar class="avatar" round :size="52" :src="gameInfo.user_avatar" />
    <div class="game-info" @click="howToPlay">
      <n-icon size="40" :component="GameController" color="#209DF5" />
      <div class="game-info-font">玩法</div>
      <n-modal v-model:show="howPlayModal" preset="card" title="玩法介绍">
        <n-ul v-if="mode === 'pk'">
          <n-li>
            <n-h5 class="how-play-info-item">
              这是随机对战模式, 可以为你<span class="how-play-info-item-strong"
                >匹配水平相当</span
              >的对手
            </n-h5>
          </n-li>
          <n-li>
            <n-h5 class="how-play-info-item">
              这个是
              <span class="how-play-info-item-strong">没有禁手</span>的五子棋,
              如果你知道这个知识点那你的优势会很大
            </n-h5>
          </n-li>
          <n-li>
            <n-h5 class="how-play-info-item">
              可以了解一下
              <span class="how-play-info-item-strong">必胜态</span>
              , 这会对你很有帮助
            </n-h5>
          </n-li>
        </n-ul>
        <n-ul v-else>
          <n-li>
            <n-h5 class="how-play-info-item">
              这是OnePut模式, 意味着你只能
              <span class="how-play-info-item-strong">下一步棋</span>
            </n-h5>
          </n-li>
          <n-li>
            <n-h5 class="how-play-info-item">
              你需要在<span class="how-play-info-item-strong">10秒内</span
              >找到你的必胜态或者直接取胜, 直接取胜<span
                class="how-play-info-item-strong"
                >优先级最高</span
              >
            </n-h5>
          </n-li>
          <n-li>
            <n-h5 class="how-play-info-item">
              每关只允许<span class="how-play-info-item-strong">通过一次</span
              >哦
            </n-h5>
          </n-li>
        </n-ul>
      </n-modal>
    </div>
    <div class="game-ranklist">
      <n-button strong secondary round type="info" @click="getRankList">
        排行榜
      </n-button>

      <n-modal v-model:show="ranklistModal" preset="card" :title="ranklistName">
        <div class="user" v-for="user in users" :key="user.id">
          <div>#{{ user.rank }}</div>
          <div>
            <img :src="user.avatar" alt="" />
          </div>
          <div>{{ user.username }}</div>
          <div>{{ user.rate }}</div>
        </div>
      </n-modal>
    </div>
  </n-space>
</template>

<script>
import { GameControllerOutline, GameController } from "@vicons/ionicons5";
import { NAvatar, NSpace, NIcon, NButton, NModal, NH5, NText, NUl, NLi } from 'naive-ui'
import { ref } from 'vue'
import { useUserStore } from '../store'
import User from '../api/user'
import OnePut from '../api/oneput'
import notice from '../utils/notice'
export default {
  name: "GameInfo",
  components: {
    NAvatar, NSpace, NIcon, NButton, NModal, NH5, NUl, NLi, NText,
    GameController, GameControllerOutline,
  },
  props: {
    gameInfo: {
      type: Object,
      required: true,
    },
    mode: {
      type: String,
    }
  },
  setup (props) {
    const mode = props.mode;
    let ranklistName = mode === 'pk' ? "pk排行榜" : "闯关排行榜";
    let ranklistModal = ref(false);
    let howPlayModal = ref(false);
    let users = ref([]);
    const userStore = useUserStore();
    const howToPlay = () => {
      howPlayModal.value = true;
    }

    const getRankList = () => {
      let res = null;
      ranklistModal.value = true;
      if (mode === 'pk') {
        res = User.getPkRankList(userStore.user.id);
      } else {
        res = OnePut.getRankList();
      }
      res.then(resp => {
        let data = resp.data
        if (data.code === 0) {
          if (mode === 'pk') {
            data = JSON.parse(data.data);
            users.value = data.users;
          } else {
            convert(data.data);
          }
        } else {
          let notice_msg = data.message;
          if (data.description !== '') {
            notice_msg = data.description;
          }
          notice(notice_msg, 2e3, "error");
        }
      }).catch((error) => {
        console.log(error)
        notice("系统繁忙", 2e3, "error");
      })
    }
    const convert = (data) => {
      users.value = [];
      for (let i = 0; i < data.length; i++) {
        users.value.push({
          ...data[i],
          rate: data[i].passCount + " 关·" + data[i].passTime + "s",
        })
      }
    }
    return {
      GameController, ranklistModal, howPlayModal,
      users, ranklistName,
      getRankList, howToPlay
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.game-info {
  font-family: SmileySans;
}
.how-play-info-item {
  font-family: SmileySans;
  color: #437c90;
}
.how-play-info-item-strong {
  font-weight: bold;
  color: #437c90;
}
.game-info-display {
  margin-top: 5vh;
}
.game-info-font {
  font-size: 12px;
  text-align: center;
}
.game-info:hover {
  cursor: pointer;
}
.avatar:hover {
  cursor: pointer;
  -webkit-animation: rotate-center 0.6s ease-in-out both;
  animation: rotate-center 0.6s ease-in-out both;
}

div.user {
  display: grid;
  position: relative;
  left: -24px;
  user-select: none;
  width: 100vw;
  height: 5vh;
  grid-template-columns: repeat(4, 20vw);
  line-height: 5vh;
  background-color: lightblue;
  margin-top: 1vh;
  border-radius: 1vh;
}
div.user > div {
  text-align: center;
}
div.user:nth-child(1) {
  background-color: #ddbe07;
}
div.user > div:nth-child(1) {
  color: darkred;
}
div.user > div:nth-child(2) {
  display: flex;
  justify-content: center;
  align-items: center;
}
div.user > div:nth-child(3) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: pre;
  text-align: center;
  color: white;
}
div.user > div:nth-child(4) {
  color: white;
  font-size: 10px;
  z-index: 2;
}
div.user > div > img {
  width: 3vh;
  height: 3vh;
  border-radius: 50%;
}
@-webkit-keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes rotate-center {
  0% {
    -webkit-transform: rotate(0);
    transform: rotate(0);
  }
  100% {
    -webkit-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
</style>