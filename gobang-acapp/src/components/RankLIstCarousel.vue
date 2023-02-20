<template>
  <n-carousel
    autoplay
    :interval="2000"
    direction="vertical"
    dot-placement="right"
    mousewheel
    style="width: 100%; height: 10vh"
  >
    <div class="carousel-item" v-for="user in topThreeList" :key="user.id">
      <n-space justify="space-around">
        <n-icon :size="50" class="carousel-item-icon">
          <svg
            t="1673179168177"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1815"
            width="128"
            height="128"
          >
            <path
              d="M671.3 545.5l-102.1-14.9-45.7-92.6c-2.8-5.7-9.3-7.7-14.4-7.7-5.2 0-11.7 2-14.5 7.7l-45.7 92.5-102.2 14.9c-6 0.9-11.1 5.2-13 11-1.9 5.8-0.3 12.3 4.1 16.5l73.9 72.1-17.4 101.8c-1 6 1.5 12.2 6.4 15.8 2.8 2 6 3.1 9.5 3.1 2.6 0 5.2-0.6 7.5-1.9l91.4-48 91.4 48c2.3 1.2 4.9 1.9 7.5 1.9 3.5 0 6.7-1.1 9.5-3.1 5-3.6 7.5-9.7 6.4-15.7L606.4 645l73.9-72.1c4.4-4.2 6-10.7 4.1-16.5-1.9-5.8-6.9-10-13.1-10.9z m-96.7 79.1c-4.9 4.7-7.1 11.6-5.9 18.3l11.8 68.9-61.9-32.5c-2.9-1.6-6.3-2.4-9.6-2.4s-6.6 0.8-9.6 2.4l-61.9 32.5 11.8-68.9c1.1-6.7-1.1-13.5-5.9-18.3l-50-48.7 69.1-10.1c6.7-1 12.6-5.2 15.6-11.3l30.9-62.7 31 62.7c3 6.1 8.8 10.3 15.5 11.3l69.1 10.1-50 48.7z"
              fill="#6B400D"
              p-id="1816"
            ></path>
            <path
              d="M540 554.5l-31-62.7-30.9 62.7c-3 6.1-8.8 10.3-15.6 11.3l-69.1 10.1 50 48.7c4.9 4.7 7.1 11.6 5.9 18.3l-11.8 68.9 61.9-32.5a20.39 20.39 0 0 1 19.2 0l61.9 32.5-11.8-68.9c-1.2-6.7 1.1-13.5 5.9-18.3l50-48.7-69.1-10.1c-6.7-1-12.5-5.2-15.5-11.3z"
              fill="#FFD524"
              p-id="1817"
            ></path>
            <path
              d="M818.9 412.6l137.4-245.5c-15-11.8-91.7-66.2-143.9-102.8H205.6c-49.9 36.1-123.1 89.6-137.3 101.2L199 412.7l-1.4 2.3c-16.4 27.8-29 58.2-37.5 90.2h0.5l-1.4 5.6c-7.4 29.6-11.2 58.7-11.1 86.6 0 199 161.9 360.9 360.9 360.9 199 0 360.9-161.9 360.9-360.9 0.1-64.1-17.1-127.2-49.6-182.4l-1.4-2.4z m69.2-242.5l2.3 4-2.5 3.7L786.6 367l-4-4.6c-18.8-21.9-40.3-41.6-63.8-58.4l-3.4-2.4 94.4-187.4 78.3 55.9z m-132.6-63.8l-83.7 169-3.7-1.8c-49.1-24.2-104.2-37-159.4-37-41.2 0-82.8 7.6-123.6 22.6l-3.8 1.1c-0.5 0.2-4.6 2.1-6.1 2.6 0 0-8.3 3.1-9.7 3.6v0.4l-7.9 3.3c-2.5 1-5.1 2-7.5 3.2l-3.7 1.8-83.8-169h492.9z m-626.7 70.1l-2.5-3.7 81.9-58.6 94.4 187.4-3.4 2.4c-23.5 16.8-44.9 36.4-63.8 58.4l-4 4.6-102.6-190.5z m380.2 731c-170.9 0-310-139.1-310-310 0-35.7 6.5-71.5 19.3-106.3l1.4-3.9c0.8-2.3 1.6-4.6 2.5-6.8 3.8-9.2 8.1-18.4 13.3-28.1 30.3-56.8 76.8-102.2 134.7-131.4 44.1-22.3 90.9-33.6 138.9-33.6 171 0 310.1 139.1 310.1 310S680 907.4 509 907.4z"
              fill="#6B400D"
              p-id="1818"
            ></path>
            <path
              d="M380.5 792.1c-43.7-25.5-69.4-58.1-88.8-112.7l-1.3-3.3c-2.9-5.7-8.9-9.4-15.6-9.4-1.9 0.1-3.6 0.4-5.2 1-7.7 2.7-12.4 10.6-11.1 18.9l1.3 4.4c21.6 60.9 52.2 99.5 102 129 0.1 0 6.8 4.3 9.5 4.3l2.5 0.1h0.1c1.7 0 3.3-0.4 4.9-0.9 8.8-3.1 13.4-12.9 10.3-21.8-1.6-4.4-4.8-7.9-8.6-9.6zM265.8 649.4c4.7-1.5 8.5-4.8 10.7-9.1 2.2-4.4 2.6-9.4 1.1-14.1-2.5-7.6-9.5-12.7-17.5-12.7-2 0-3.9 0.3-5.7 0.9-9.6 3.2-14.9 13.6-11.8 23.2 2.5 7.6 9.5 12.7 17.5 12.7 1.9 0 3.8-0.4 5.7-0.9z"
              fill="#6B400D"
              p-id="1819"
            ></path>
          </svg>
        </n-icon>
        <n-gradient-text :size="20" type="info">
          {{ user.username }}
        </n-gradient-text>
        <n-gradient-text :size="20" type="warning">
          {{ user.rate }}
        </n-gradient-text>
      </n-space>
    </div>
  </n-carousel>
</template>

<script>
import { NCarousel, NGradientText, NIcon, NSpace } from 'naive-ui'
import { onMounted, ref } from '@vue/runtime-core';
import User from '../api/user'
import OnePut from '../api/oneput'
import notice from '../utils/notice'
export default {
  name: "RankListCarousel",
  components: {
    NCarousel, NGradientText, NSpace, NIcon
  },
  props: {
    mode: {
      type: String,
      required: true,
    },
  },
  setup (props) {
    const topThreeList = ref([]);
    onMounted(() => {
      if (props.mode === 'pk') {
        let res = User.getPkTopThree();
        res.then(resp => {
          let data = resp.data;
          if (data.code === 0) {
            topThreeList.value = data.data;
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
      } else {
        let res = OnePut.getTopThree();
        res.then(resp => {
          let data = resp.data;
          if (data.code === 0) {
            convert(data.data);
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
    })
    const convert = (data) => {
      for (let i = 0; i < data.length; i++) {
        topThreeList.value.push({
          ...data[i],
          rate: "通关: " + data[i].passCount + " 耗时: " + data[i].passTime + "s",
        })
      }
    }
    return {
      topThreeList,
    }
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.carousel-img {
  width: 100%;
  height: 10vh;
  object-fit: cover;
}
.carousel-item {
  height: 10vh;
  width: 100%;
  text-align: center;
  line-height: 10vh;
  font-size: 24px;
  font-family: SmileySans;
}

.carousel-item-icon {
  margin-top: 3vh;
}
</style>