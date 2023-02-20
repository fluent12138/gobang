<template>
  <Back />
  <n-space vertical align="center" class="oneput-reputations">
    <n-gradient-text type="info" class="oneput-edit-title">
      编辑关卡信息
    </n-gradient-text>
  </n-space>
  <OnePutMap class="oneput-level-map" />
  <n-space vertical>
    <n-descriptions>
      <n-descriptions-item>
        <template #label class="descriptions-item">
          &nbsp;&nbsp;&nbsp;&nbsp;答案坐标:
        </template>
        <n-space justify="center">
          <n-input
            v-model:value="x"
            size="small"
            placeholder="横坐标x : [0~11]"
          />
          <n-input
            v-model:value="y"
            size="small"
            placeholder="纵坐标y : [0~11]"
          />
        </n-space>
      </n-descriptions-item>
    </n-descriptions>

    <n-descriptions>
      <n-descriptions-item>
        <template #label class="descriptions-item">
          &nbsp;&nbsp;&nbsp;&nbsp;请描述它的正确性:
        </template>
        <n-input
          v-model:value="description"
          type="textarea"
          placeholder="可以提供链接或者自己描述"
          :autosize="{
            minRows: 1,
          }"
        />
      </n-descriptions-item>
    </n-descriptions>

    <n-input
      v-if="userStore.user.role === 1"
      v-model:value="statusInfo"
      placeholder="驳回或通过理由"
    />

    <n-space vertical align="center">
      <n-gradient-text
        type="error"
        v-if="
          oneputStore.levelObject !== null &&
          oneputStore.levelObject.statusInfo !== null &&
          oneputStore.levelObject.statusInfo !== ''
        "
      >
        {{ oneputStore.levelObject.statusInfo }}
      </n-gradient-text>
      <div v-if="userStore.user.role === 0">
        <n-button
          v-if="oneputStore.levelObject === null"
          strong
          secondary
          round
          type="info"
          size="small"
          class="commit-level"
          @click="commitLevel"
        >
          提交
        </n-button>

        <n-button
          v-else
          strong
          secondary
          round
          type="info"
          size="small"
          class="commit-level"
          @click="updateLevel"
        >
          更新
        </n-button>
      </div>
      <n-space v-else justify="space-around">
        <n-button
          strong
          secondary
          round
          type="error"
          size="small"
          class="commit-level"
          @click="rejectLevel"
        >
          驳回
        </n-button>
        <n-button
          v-if="oneputStore.levelObject === null"
          strong
          secondary
          round
          type="info"
          size="small"
          class="commit-level"
          @click="commitLevel"
        >
          提交
        </n-button>
        <n-button
          strong
          secondary
          round
          type="success"
          size="small"
          class="commit-level"
          @click="passLevel"
        >
          通过
        </n-button>
      </n-space>
    </n-space>
  </n-space>
</template>

<script>
import Back from '../components/Back.vue'
import OnePutMap from '../components/OnePutMap.vue'
import notice from '../utils/notice'
import OnePut from '../api/oneput'
import back from '../utils/routerBack'
import { useOnePutStore, useUserStore } from '../store'
import { NSpace, NGradientText, NInputNumber, NDescriptions, NDescriptionsItem, NInput, NButton, pProps } from 'naive-ui'
import { onMounted, onUnmounted, ref } from 'vue'

export default {
  components: {
    Back, OnePutMap,
    NSpace, NGradientText, NInputNumber, NDescriptions, NDescriptionsItem, NInput, NButton,
  },
  setup () {
    const oneputStore = useOnePutStore();
    const userStore = useUserStore();
    let x = ref(null);
    let y = ref(null);
    let description = ref(null);
    let statusInfo = ref(null);
    onMounted(() => {
      if (oneputStore.levelObject !== null) {
        x.value = oneputStore.levelObject.x + '';
        y.value = oneputStore.levelObject.y + '';
        description.value = oneputStore.levelObject.description;
      }
    })
    onUnmounted(() => {
      oneputStore.updateLevelObject(null);
    })
    const commitLevel = () => {
      let map = oneputStore.levelMap.getMapString();
      if (!(map.includes('1') || map.includes('2'))) {
        notice("地图不能为空", 1500, "warning");
        return;
      }
      if (description.value === null || description.value === "") {
        notice("描述信息不能为空", 1500, "warning");
        return;
      }
      if (x.value === null || y.value === null || x.value < 0 || x.value > 11 || y.value < 0 || y.value > 11) {
        notice("答案坐标范围是[0~11]", 1500, "warning");
        return;
      }
      let res = OnePut.addLevelMap({
        userId: userStore.user.id,
        x: parseInt(x.value),
        y: parseInt(y.value),
        description: description.value,
        map,
      });

      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("添加成功", 1000, "success");
          back();
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
    }

    const updateLevel = () => {
      let map = oneputStore.levelMap.getMapString();
      if (description.value === null || description.value === "") {
        notice("描述信息不能为空", 1500, "warning");
        return;
      }
      if (!(map.includes('1') || map.includes('2'))) {
        notice("地图不能为空", 1500, "warning");
        return;
      }
      let res = OnePut.updateLevelMap({
        id: oneputStore.levelObject.systemId,
        userId: userStore.user.id,
        x: parseInt(x.value),
        y: parseInt(y.value),
        description: description.value,
        map,
      });

      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("修改成功", 1000, "success");
          back();
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
    }

    const rejectLevel = () => {
      let map = oneputStore.levelMap.getMapString();
      let res = OnePut.adminUpdateLevel({
        id: oneputStore.levelObject.systemId,
        x: parseInt(x.value),
        y: parseInt(y.value),
        description: description.value,
        map,
        status: 2,
        statusInfo: statusInfo.value,
      });

      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("驳回", 1000, "success");
          back();
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
    }

    const passLevel = () => {
      let map = oneputStore.levelMap.getMapString();
      let res = OnePut.adminUpdateLevel({
        id: oneputStore.levelObject.systemId,
        x: parseInt(x.value),
        y: parseInt(y.value),
        description: description.value,
        userId: oneputStore.levelObject.userId,
        map,
        status: 0,
        statusInfo: statusInfo.value,
      });

      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("通过", 1000, "success");
          back();
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
    }
    return {
      x, y, description, commitLevel, oneputStore, updateLevel, userStore, rejectLevel, passLevel, statusInfo
    }
  }
}
</script>

<style scpoed>
@import "../assets/font/font.css";
.n-space {
  user-select: none;
  font-family: SmileySans;
}
.oneput-edit-title {
  margin-top: 5px;
  font-size: 20px;
}
.oneput-level-map {
  margin-bottom: 1vh;
}
.commit-level {
  width: 100px;
}
.descriptions-item {
  font-size: 12px;
}
</style>