<template>
  <Back />
  <n-space vertical align="center" class="oneput-self-reputation">
    <n-gradient-text type="success" class="oneput-self-reputation-title">
      我的贡献
    </n-gradient-text>
  </n-space>

  <n-data-table
    :bordered="false"
    :columns="columns"
    :data="data"
    :pagination="pagination"
  />

  <n-modal
    v-model:show="deleteModal"
    preset="dialog"
    title="删除"
    content="你确认删除关卡信息吗?"
    positive-text="确认"
    negative-text="算了"
    @positive-click="confirmDelete"
  />
</template>

<script>
import { h, onMounted, ref } from 'vue'
import { NSpace, NGradientText, NDataTable, NTag, NButton, NModal } from 'naive-ui'
import { useOnePutStore, useRouterStore, useUserStore } from '../store'
import Back from '../components/Back.vue'
import OnePut from '../api/oneput'
import notice from '../utils/notice'
export default {
  components: {
    Back,
    NSpace, NGradientText, NDataTable, NModal
  },
  setup () {
    const oneputStore = useOnePutStore();
    const routerStore = useRouterStore();
    const userStore = useUserStore();
    const createColumns = ({
      updateLevel, deleteLevel
    }) => {
      return [
        {
          title: "id",
          key: "id"
        },
        {
          title: "x",
          key: "x"
        },
        {
          title: "y",
          key: "y"
        },
        {
          title: "状态",
          key: "status",
          render (row) {
            const status = row.status.map((tagKey) => {
              let type = "info", value = "";
              if (tagKey === 1) {
                value = "审核中";
              } else if (tagKey === 0) {
                type = "success";
                value = "已通过";
              } else {
                type = "error";
                value = "未通过";
              }
              return h(
                NTag,
                {
                  style: {
                    marginRight: "6px"
                  },
                  type,
                  bordered: false
                },
                {
                  default: () => value
                }
              );
            });
            return status;
          }
        },
        {
          title: "操作",
          key: "actions",
          render (row) {
            if (row.status[0] === 0 && userStore.user.role === 0) {
              return h(
                NButton,
                {
                  size: "small",
                  onClick: () => updateLevel(row)
                },
                { default: () => "修改" },
              );
            }
            return [
              h(
                NButton,
                {
                  style: {
                    "width": "50px",
                  },
                  size: "small",
                  onClick: () => updateLevel(row)
                },
                { default: () => "修改" },
              ),
              h(
                NButton,
                {
                  style: {
                    "margin-left": "10px",
                    "width": "50px",
                  },
                  size: "small",
                  type: "error",
                  onClick: () => deleteLevel(row)
                },
                { default: () => "删除" },
              )
            ];
          }
        }
      ];
    };
    let tableData = ref([]);
    let deleteModal = ref(false);
    let levelData = ref();
    onMounted(() => {
      routerStore.updateLastRouterName("one_put_game");
      pullReputation();
    })
    const pullReputation = () => {
      let res = null;
      if (userStore.user.role === 0) {
        res = OnePut.getSelfReputation();
      } else {
        res = OnePut.getAdminReputation();
      }
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
      }).catch(() => {
        notice("系统繁忙", 2e3, "error");
      })
    }

    const convert = (data) => {
      tableData.value = [];
      for (let i = 0; i < data.length; i++) {
        let level = {
          ...data[i],
          key: i,
          id: i + 1,
          systemId: data[i].id,
          status: [data[i].status],
        };
        tableData.value.push(level);
      }
    }

    const updateLevel = (level) => {
      oneputStore.updateLevelObject(level);
      routerStore.updateLastRouterName("one_put_self_reputation");
      setTimeout(() => {
        routerStore.updateRouterName("one_put_level_edit");
      }, 200);
    }

    const confirmDelete = () => {
      let level = levelData.value;
      let res = null;
      if (userStore.user.role === 0) {
        res = OnePut.deleteLevel({
          id: parseInt(level.systemId),
          status: parseInt(level.status[0]),
        });
      } else {
        res = OnePut.adminDeleteLevel({
          id: parseInt(level.systemId),
        });
      }

      res.then(resp => {
        let data = resp.data;
        if (data.code === 0) {
          notice("删除成功", 2e3, "success");
          pullReputation();
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

    const deleteLevel = (level) => {
      deleteModal.value = true;
      levelData.value = level;
    }

    return {
      data: tableData,
      columns: createColumns({
        updateLevel, deleteLevel,
      }),
      pagination: {
        pageSize: 8
      },
      deleteModal,
      confirmDelete,
    };
  }
}
</script>

<style scoped>
@import "../assets/font/font.css";
.n-space {
  user-select: none;
  font-family: SmileySans;
}
.oneput-self-reputation-title {
  margin-top: 2vh;
  margin-bottom: 1vh;
  font-size: 24px;
}
</style>