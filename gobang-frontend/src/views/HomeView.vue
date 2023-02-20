<template>
  <Back />
  <n-space vertical align="center">
    <n-h4 class="title">房间: {{ homeStore.homeId }}</n-h4>
    <InfoDisplay :user="opponentInfo" />
    <n-gradient-text class="notice-font" :size="20" type="info">
      说点什么吧~
    </n-gradient-text>
    <n-space vertical>
      <div class="chat-board" ref="boardContent">
        <div
          class="content-item"
          v-for="content in contentList"
          :key="content.id"
        >
          {{ content.username }}: {{ content.content }}
        </div>
      </div>
    </n-space>
    <n-space justify="space-around">
      <n-input round v-model:value="content" placeholder="说点什么吧" />
      <n-button @click="send">发送</n-button>
    </n-space>
    <InfoDisplay :user="userInfo" />
  </n-space>
</template>

<script>
import Back from '../components/Back.vue'
import InfoDisplay from '../components/InfoDisplay.vue'
import WsEvent from '../utils/WsEvent'
import notice from '../utils/notice'
import Home from '../api/home'
import { NSpace, NH4, NButton, NInput, NScrollbar, NGradientText } from 'naive-ui'
import { onMounted, ref, nextTick } from 'vue'
import { useGobangStore, useUserStore, useRouterStore, useHomeStore } from '../store'
export default {
  name: "HomeView",
  components: {
    NSpace, NH4, NButton, NInput, NScrollbar, NGradientText,
    Back, InfoDisplay,
  },
  setup () {
    const [routerStore, userStore, gobangStore, homeStore] = [useRouterStore(), useUserStore(), useGobangStore(), useHomeStore()];
    const ws_event = new WsEvent();
    let boardContent = ref();
    let socket = null;
    let master = ref(null);
    let contentList = ref([]);
    let content = ref(null);
    let opponentInfo = ref({
      avatar: homeStore.opponent_photo,
      username: homeStore.opponent_username,
      rate: homeStore.opponent_rate,
      home: homeStore.home,
    });
    let userInfo = ref({
      ...userStore.user,
      home: homeStore.home,
    });
    onMounted(() => {
      socket = gobangStore.socket;
      socket.ws.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.event) {
          case ws_event.READY:
            onReady(data);
            break;
          case ws_event.CHAT_DISCONNECT:
            onDisconnect(data);
            break;
          case ws_event.INIT_HOME:
            initHome(data);
            break;
          case ws_event.CHAT_MSG:
            onContent(data);
            break;
          case ws_event.MEMBER_QUIT:
            onMemberQuit();
            break;
          case ws_event.MASTER_QUIT:
            onMasterQuit();
            break;
          case ws_event.PLAYING:
            onPlaying(data);
          default:
            break;
        }
      }
    })
    const initHome = (data) => {
      setTimeout(() => {
        // 延迟展示;
        homeStore.updateHomeInfo(data);
        // 会修改master的信息
        opponentInfo.value = {
          avatar: homeStore.opponent_photo,
          username: homeStore.opponent_username,
          rate: homeStore.opponent_rate,
          home: homeStore.home,
        };

        userInfo.value = {
          ...userStore.user,
          home: homeStore.home,
        };
      }, 1500);
    }
    const onReady = (data) => {
      homeStore.updateOpponentReady(data.ready);
    }

    const onPlaying = (data) => {
      routerStore.updateLastRouterName("play_ground");
      routerStore.updateRouterName("pk");
      homeStore.clearHomeInfo();
      let round = (userStore.user.id === data.game.a_id);
      let gameInfo = {
        opponent_photo: data.opponent_photo,
        opponent_rate: data.opponent_rate,
        opponent_username: data.opponent_username,
        a_id: data.game.a_id,
        b_id: data.game.b_id,
        map: data.game.map,
        round,
      };
      gobangStore.updateGameStartInfo(gameInfo);
    }

    const onMemberQuit = () => {
      homeStore.clearOpponentInfo();
      contentList.value = [];
      opponentInfo.value = {
        avatar: "https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/501/defaultAvatar.jpg",
        username: "正在等待他人进入..",
        rate: "未知",
        home: homeStore.home,
      }
      userInfo.value = {
        ...userStore.user,
        home: homeStore.home,
      };
    }

    const onMasterQuit = () => {
      notice("房间即将解散", 1500, "info");
      setTimeout(() => {
        routerStore.updateRouterName("play_ground");
        homeStore.clearHomeInfo();
      }, 1500);
    }

    const send = () => {
      if (content.value === null) {
        notice("不能发空白信息", 1500, "warning")
        return;
      }
      if (homeStore.bId === null) {
        notice("复制房间号给你的好友, 让他加入进来!", 1500, "success");
        return;
      }
      gobangStore.socket.send(JSON.stringify({
        event: "chat",
        content: content.value,
      }))
      contentList.value.push({
        id: contentList.value.length + 1,
        content: content.value,
        username: userStore.user.username,
      })
      content.value = null;
      nextTick(() => {
        boardContent.value.scrollTop = boardContent.value.scrollHeight;
      })
    }

    const onContent = (data) => {
      contentList.value.push({
        id: contentList.value.length + 1,
        content: data.content,
        username: homeStore.opponent_username,
      })
      nextTick(() => {
        boardContent.value.scrollTop = boardContent.value.scrollHeight;
      })
    }

    const onDisconnect = (data) => {
      // 房主掉线
      if (homeStore.aId === userStore.user.id) {
        onMemberQuit();
      } else {// 成员掉线
        onMasterQuit();
      }

    }
    return {
      homeStore, userStore, opponentInfo, userInfo, master, boardContent,
      contentList, content, send,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.chat-board {
  width: 80vw;
  height: 55vh;
  border-radius: 3vh;
  overflow-y: scroll;
  overflow-x: hidden;
  background-color: rgba(58, 161, 196, 0.1);
}
.title {
  margin-top: 1vh;
}
.content-item {
  box-sizing: border-box;
  width: 70vw;
  margin: 0 auto;
  margin-top: 1vh;
  border: 1px solid rgba(58, 161, 196, 0.5);
  border-radius: 1vh;
  font-family: Consolas, Monaco, Lucida Console, Liberation Mono,
    DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace;
}

/*滚动条样式*/
.chat-board::-webkit-scrollbar {
  width: 4px;
}
.chat-board::-webkit-scrollbar-thumb {
  background: #d2d2d2;
}
.notice-font {
  font-family: SmileySans;
}
</style>