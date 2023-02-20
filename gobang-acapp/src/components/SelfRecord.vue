<template>
  <n-card size="large" class="self-record">
    <n-space justify="space-around">
      <n-statistic label="你进行了" tabular-nums>
        <n-number-animation
          ref="numberAnimationInstRef"
          :from="0"
          :to="pk_count"
        />
        <template #suffix> 场对局 </template>
      </n-statistic>
      <n-statistic label="rating: " tabular-nums>
        <n-number-animation
          ref="numberAnimationInstRef"
          :from="0"
          :to="userStore.user.rate"
        />
      </n-statistic>
    </n-space>
  </n-card>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { NCard, NStatistic, NSpace, NNumberAnimation, } from 'naive-ui'
import { useUserStore } from '../store'
import User from '../api/user'
import notice from '../utils/notice'
export default {
  name: "SelfRecord",
  components: {
    NCard, NStatistic, NSpace, NNumberAnimation,
  },
  setup () {
    const numberAnimationInstRef = ref(null);
    const userStore = useUserStore();
    const pk_count = computed(() => userStore.user.pkCount);

    onMounted(() => {
      if (!pk_count.value) {
        notice("你没有进行过对局，来一局吧~", 1.5e3, "info")
      }
      let res = User.getinfo();
      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          userStore.updateSelfRecord(data.data);
        } else {
          let notice_msg = data.message;
          if (data.description !== '') {
            notice_msg = data.description;
          }
          notice(notice_msg, 2e3, "error");
        }
      }).catch(() => {
        notice("系统繁忙", 2e3, "error");
      })
    });
    return {
      numberAnimationInstRef,
      pk_count, userStore
    };
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.self-record {
  font-family: SmileySans;
}
</style>