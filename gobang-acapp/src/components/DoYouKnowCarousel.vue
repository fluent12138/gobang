<template>
  <n-carousel
    autoplay
    :interval="2000"
    dot-placement="right"
    draggable
    mousewheel
    class="know-carousel"
  >
    <div class="carousel-item">你知道吗: 最长的刑决刑期为384912年</div>
    <div class="carousel-item">你知道吗: 疑犯潜逃最长时间为67年</div>
    <div class="carousel-item">你知道吗: 背诵圆周率最长的人是原口证</div>
    <div class="carousel-item">你知道吗: 英语书写最长的地名新西兰-92字母</div>
    <div class="carousel-item">你知道吗: 最强的纯酸是碳硼烷酸</div>
    <div class="carousel-item">你知道吗: 最硬的物质是硫化碳炔</div>
    <div class="carousel-item">你知道吗: 最辛辣的植物是龙之气息</div>
    <div class="carousel-item">你知道吗: 最高的花是巨花魔芋</div>
    <div class="carousel-item">你知道吗: 最老的植物是欧洲云杉</div>
    <div class="carousel-item">你知道吗: 持续时间最长的彩虹是无名彩虹</div>
  </n-carousel>
</template>

<script>
import { NCarousel } from 'naive-ui'
export default {
  name: "DoYouKnowCarousel",
  components: {
    NCarousel,
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.know-carousel {
  width: 100%;
  height: 10vh;
}
.carousel-item {
  height: 10vh;
  width: 100%;
  text-align: center;
  line-height: 10vh;
  color: rgb(111, 170, 222);
  font-size: 20px;
  font-family: SmileySans;
}
.carousel-item:hover {
  cursor: pointer;
}
</style>