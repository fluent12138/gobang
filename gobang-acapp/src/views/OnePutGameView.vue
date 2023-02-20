<template>
  <div class="gobang-oneput-game">
    <Back />
    <GameInfo class="gobang-game-info" :gameInfo="gameInfo" />
    <RankListCarousel class="gobang-ranklist-carousel" :mode="'oneput'" />
    <OnePutReputation class="oneput-reputation" />
    <n-grid :x-gap="12" :y-gap="20" :cols="2" class="gobang-game-level">
      <n-grid-item v-for="oneput in oneputInfos" :key="oneput.id">
        <n-space justify="center">
          <GameLevel :info="oneput" />
        </n-space>
      </n-grid-item>
    </n-grid>
    <n-space justify="center">
      <n-pagination
        size="small"
        v-model:page="page"
        :page-count="max_pages"
        :on-update="pullPage()"
      />
    </n-space>
  </div>
</template>

<script>
import Back from '../components/Back.vue'
import GameInfo from '../components/GameInfo.vue'
import RankListCarousel from '../components/RankLIstCarousel.vue'
import OnePutReputation from '../components/OnePutReputation.vue'
import GameLevel from '../components/GameLevel.vue'
import OnePut from '../api/oneput'
import notice from '../utils/notice'
import { NGrid, NGridItem, NPagination, NSpace } from 'naive-ui'
import { useUserStore, useOnePutStore } from '../store'
import { onMounted, ref } from '@vue/runtime-core'
export default {
  name: 'OnePutGameView',
  components: {
    Back, GameInfo, RankListCarousel, NGrid, NGridItem, NSpace, NPagination,
    GameLevel, OnePutReputation,
  },
  setup () {
    const userStore = useUserStore();
    const oneputStore = useOnePutStore();
    const gameInfo = {
      user_avatar: userStore.user.avatar,
      game_id: "1",
    }
    let total_levels = 0;
    let oneputInfos = ref([]);
    let page = ref(oneputStore.page);
    let last_page = ref(0);
    let max_pages = ref(0);
    onMounted(() => {
      pullPage();
    })

    const pullPage = () => {
      if (last_page.value === page.value) return false;
      last_page.value = page.value;
      oneputStore.updateOnePutPage(page.value);
      let res = OnePut.page(page.value);
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
      oneputInfos.value = data.levels;
      total_levels = data.count;
      max_pages.value = parseInt(Math.ceil(total_levels / 8));
    }
    return {
      gameInfo, oneputInfos, page, max_pages, pullPage,
    }
  }
}
</script>

<style scoped>
.gobang-oneput-game {
  user-select: none;
}
.gobang-ranklist-carousel {
  margin-bottom: 1vh;
}
.gobang-game-level {
  margin-bottom: 30px;
}
.oneput-reputation {
  margin-bottom: 3vh;
}
</style>