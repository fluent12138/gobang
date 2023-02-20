<template>
  <Back />
  <n-space justify="center">
    <n-button
      type="info"
      strong
      round
      secondary
      class="create-home"
      @click="createHomeModal"
    >
      创建房间
    </n-button>
    <n-gradient-text type="info" class="play-ground-header">
      游戏大厅
    </n-gradient-text>
  </n-space>
  <n-space vertical>
    <n-modal
      v-model:show="searchModal"
      preset="dialog"
      :title="`房间号: ${modelHomeId}`"
      positive-text="加入"
      negative-text="取消"
      transform-origin="center"
      @positive-click="submitSearch"
      style="width: 360px"
      :z-index="3000"
    >
      <n-h5 v-if="searchRes.password">
        <n-input
          round
          v-model:value="homePassword"
          placeholder="请输入密码"
          style="width: 80vw"
        >
          <template #suffix>
            <n-icon :component="FlashOutline" />
          </template>
        </n-input>
      </n-h5>
      <n-h5 v-else>无密码, 直接进入!</n-h5>
    </n-modal>

    <n-modal
      v-model:show="craeteModal"
      preset="card"
      :title="`创建房间`"
      style="width: 360px"
      transform-origin="center"
      :z-index="3000"
    >
      <n-space vertical align="center">
        <n-space justify="center">
          <div class="create-modal-offset">密码</div>
          <n-switch
            class="create-modal-offset"
            v-model:value="craeteHavePassword"
          />
          <n-input
            v-if="craeteHavePassword"
            round
            v-model:value="craeteHomeInputPassword"
            placeholder="请输入密码"
          ></n-input>
          <n-skeleton v-else :width="150" :sharp="false" size="medium" />
        </n-space>
        <n-button
          size="small"
          @click="
            confirmCreateHome(craeteHavePassword, craeteHomeInputPassword)
          "
        >
          确认
        </n-button>
      </n-space>
    </n-modal>

    <n-space justify="space-around">
      <n-input
        round
        v-model:value="searchHomeId"
        placeholder="搜索房间"
        style="width: 32vh"
      >
        <template #suffix>
          <n-icon :component="FlashOutline" />
        </template>
      </n-input>
      <n-icon
        size="25"
        class="text-offset search-home"
        color="#3AA1C4"
        @click="searchHome(searchHomeId)"
      >
        <svg
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          x="0px"
          y="0px"
          viewBox="0 0 512 512"
          enable-background="new 0 0 512 512"
          xml:space="preserve"
        >
          <g>
            <g>
              <g>
                <g>
                  <path
                    d="M337.509,305.372h-17.501l-6.571-5.486c20.791-25.232,33.922-57.054,33.922-93.257
					C347.358,127.632,283.896,64,205.135,64C127.452,64,64,127.632,64,206.629s63.452,142.628,142.225,142.628
					c35.011,0,67.831-13.167,92.991-34.008l6.561,5.487v17.551L415.18,448L448,415.086L337.509,305.372z M206.225,305.372
					c-54.702,0-98.463-43.887-98.463-98.743c0-54.858,43.761-98.742,98.463-98.742c54.7,0,98.462,43.884,98.462,98.742
					C304.687,261.485,260.925,305.372,206.225,305.372z"
                  ></path>
                </g>
              </g>
            </g>
          </g>
        </svg>
      </n-icon>
    </n-space>
    <div id="homeListScrollbar" ref="homeListScrollbar" :class="homeListSytle">
      <n-card size="small" v-for="(home, index) in homes" :key="home.id">
        <n-space justify="space-around">
          <n-text class="text-offset">#{{ index + 1 }}</n-text>
          <n-text class="text-offset"> {{ home.homeId }}</n-text>
          <n-icon class="text-offset" size="25" v-if="home.password">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              viewBox="0 0 24 24"
            >
              <g fill="none">
                <path
                  d="M12 2a4 4 0 0 1 4 4v2h2.5A1.5 1.5 0 0 1 20 9.5v11a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 4 20.5v-11A1.5 1.5 0 0 1 5.5 8H8V6a4 4 0 0 1 4-4zm0 11.5a1.5 1.5 0 1 0 0 3a1.5 1.5 0 0 0 0-3zM12 4a2 2 0 0 0-2 2v2h4V6a2 2 0 0 0-2-2z"
                  fill="currentColor"
                ></path>
              </g>
            </svg>
          </n-icon>
          <n-icon v-else class="text-offset" size="25">
            <svg
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              x="0px"
              y="0px"
              viewBox="0 0 512 512"
              enable-background="new 0 0 512 512"
              xml:space="preserve"
            >
              <path
                d="M376,192H188v-48c0-18.1,7.1-35.1,20-48s29.9-20,48-20s35.1,7.1,48,20s20,29.9,20,48h0l0,0c0,7.7,6.3,14,14,14s14-6.3,14-14
	l0,0h0c0-53.2-43.9-96.7-97.3-96C202,48.7,160,92.5,160,145.3V192h-24c-22,0-40,18-40,40v192c0,22,18,40,40,40h240c22,0,40-18,40-40
	V232C416,210,398,192,376,192z M270,316.8v68.8c0,7.5-5.8,14-13.3,14.4c-8,0.4-14.7-6-14.7-14v-69.2c-11.5-5.6-19.1-17.8-17.9-31.7
	c1.4-15.5,14.1-27.9,29.6-29c18.7-1.3,34.3,13.5,34.3,31.9C288,300.7,280.7,311.6,270,316.8z"
              ></path>
            </svg>
          </n-icon>

          <n-button
            type="info"
            strong
            round
            secondary
            class="enter-home"
            @click="enterHome(home.homeId)"
          >
            进入房间
          </n-button>
        </n-space>
      </n-card>
    </div>

    <n-empty v-if="!homes.length" description="没有人创建房间, 创建一个吧">
      <template #extra>
        <n-button size="small" @click="createHomeModal"> 创建房间 </n-button>
      </template>
    </n-empty>
    <n-space justify="center">
      <n-pagination
        size="small"
        v-model:page="page"
        :page-count="max_pages"
        :on-update="pullPage()"
      />
    </n-space>
  </n-space>
</template>

<script>
import $ from 'jquery'
import Back from '../components/Back.vue'
import Home from '../api/home'
import notice from '../utils/notice'
import Ws from '../utils/Ws'
import WsEvent from '../utils/WsEvent'
import { FlashOutline } from '@vicons/ionicons5'
import { useGobangStore, useUserStore, useRouterStore, useHomeStore } from '../store'
import naive, {
  NSpace, NButton, NGradientText, NCard, NText, NSkeleton,
  NIcon, NInput, NPagination, NModal, NH5, NEmpty, NSwitch,
} from 'naive-ui'
import { ref, onMounted, nextTick } from 'vue'
export default {
  name: "PlayGroundView",
  components: {
    Back,
    NSpace, NButton, NGradientText, NCard, NText, NSkeleton,
    NIcon, NInput, NPagination, NModal, NH5, NEmpty, NSwitch,
  },
  setup () {
    let total_homes = 0;
    let homes = ref([]);
    let page = ref(1);
    let last_page = ref(0);
    let max_pages = ref(0);
    let searchHomeId = ref(null);
    let searchModal = ref(false);
    let craeteModal = ref(false);
    let modelHomeId = ref();
    let searchRes = ref();
    let homePassword = ref(null);
    let craeteHomeInputPassword = ref(null);
    let homeListScrollbar = ref();
    let homeListSytle = ref();
    const [routerStore, userStore, gobangStore, homeStore] = [useRouterStore(), useUserStore(), useGobangStore(), useHomeStore()];
    const socketUrl = `ws://localhost:10020/api/websocket/${userStore.user.id}`;
    const ws_event = new WsEvent();
    let socket = null;
    onMounted(() => {
      pullPage();
      socket = new Ws(socketUrl);
      gobangStore.updateSocket(socket);

      socket.ws.onmessage = msg => {
        naive
        const data = JSON.parse(msg.data);
        switch (data.event) {
          case ws_event.HOME:
            homes.value = data.homeList;
            break;
          case ws_event.INIT_HOME:
            initHome(data);
            break;
          default:
            break;
        }
      }
      onWheel();
    })

    const onWheel = () => {
      // 适配一下显示器与笔记本
      if (userStore.screenH) {
        homeListSytle.value = "home-list-show-tall";
      } else {
        homeListSytle.value = "home-list-show-low";
      }

      let $ac_game = $('#homeListScrollbar');
      $ac_game.on("wheel", (e) => {
        //上下滚动
        if (e.originalEvent.wheelDelta > 0) {
          //向上滚动
          nextTick(() => { $ac_game.scrollTop($ac_game.scrollTop() - 40); });
        }
        if (e.originalEvent.wheelDelta < 0) {
          //向下滚动
          nextTick(() => { $ac_game.scrollTop($ac_game.scrollTop() + 40); });
        }
      })
    }
    const pullPage = () => {
      if (last_page.value === page.value) return false;
      last_page.value = page.value;
      let res = Home.page(page.value);
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          data = JSON.parse(data.data);
          convert(data);
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

    const convert = (data) => {
      homes.value = data.homes;
      total_homes = data.count;
      max_pages.value = parseInt(Math.ceil(total_homes / 7));
    }

    const searchHome = (homeId) => {
      modelHomeId.value = homeId;
      let res = Home.search(homeId);
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          callSearchModal(data.data);
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

    const callSearchModal = (data) => {
      searchRes.value = data;
      searchModal.value = true;
    }

    const submitSearch = () => {
      let res = Home.join(modelHomeId.value, homePassword.value);
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("即将进入房间", 1500, "success");
          setTimeout(() => {
            routerStore.updateLastRouterName("play_ground");
            routerStore.updateRouterName("home");
          }, 1500);
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
      searchHomeId.value = null;
      homePassword.value = null;
    }

    const enterHome = (homeId) => {
      searchHome(homeId);
    }

    const createHomeModal = () => {
      craeteModal.value = !craeteModal.value;
    }

    const confirmCreateHome = (havePssword, password) => {
      createHomeModal();
      let res = null;
      if (havePssword) {
        res = Home.create(password);
      } else {
        res = Home.craeteNoPwd();
      }
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          homeStore.updateOnCreateHome({
            aId: userStore.user.id,
            homeId: data.data.homeId,
          });
          routerStore.updateLastRouterName("play_ground");
          routerStore.updateRouterName("home");
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

    const initHome = (data) => {
      homeStore.updateHomeInfo(data);
    }

    return {
      FlashOutline, searchModal, craeteModal,
      page, max_pages, homes, searchHomeId, searchRes, homePassword, modelHomeId, craeteHavePassword: ref(false),
      craeteHomeInputPassword,
      pullPage, searchHome, enterHome, submitSearch, createHomeModal, confirmCreateHome,
      homeListScrollbar, homeListSytle,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.n-space {
  user-select: none;
  font-family: Consolas, Monaco, Lucida Console, Liberation Mono,
    DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace;
}
.play-ground-header {
  margin-top: 2vh;
  font-size: 24px;
}
.create-home {
  position: absolute;
  top: 6vh;
  left: 1vh;
}
.search-home:hover {
  cursor: pointer;
}
.n-card {
  background-color: rgba(58, 161, 196, 0.3);
  color: rgb(58, 161, 196);
  border-radius: 2vh;
  padding: 0;
}
.text-offset {
  position: relative;
  top: 5px;
}
.enter-home:hover {
  transform: scale(1.1);
  transition: 200ms;
}
.create-modal-offset {
  margin-top: 5px;
}
.home-list-show-low {
  margin-top: 2vh;
  max-height: 350px;
  overflow-y: scroll;
  overflow-x: hidden;
}
.home-list-show-tall {
  margin-top: 2vh;
  max-height: 500px;
  overflow-y: scroll;
  overflow-x: hidden;
}
/*滚动条样式*/
.home-list-show-low::-webkit-scrollbar {
  width: 4px;
}
.home-list-show-low::-webkit-scrollbar-thumb {
  background: #d2d2d2;
}

.home-list-show-tall::-webkit-scrollbar {
  width: 4px;
}
.home-list-show-tall::-webkit-scrollbar-thumb {
  background: #d2d2d2;
}
</style>