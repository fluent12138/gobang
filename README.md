
# Gobang

AcApp比赛作品 : 五子棋

**三种模式**

- 神之一手 : `在10s内找到你的必胜态或直接取胜, 下一步棋, 只能通过一次`
- 随机对战 : `通过rate分值匹配对手, 中途退出或掉线会直接输掉比赛`
- 自定义对战: `可以创建房间, 邀请你的好友加入房间, 进行对战(规则与随机对战一致)`
- 其他功能：`首页点击头像既可选择更换头像`，`神之一手支持自定义关卡、查看贡献榜以及自己的贡献`


## 🚀Authors

- [@Fluent](https://www.acwing.com/user/myspace/index/36510/)


## 🛠Tech Stack

**Client:**  Vue3, Pinia, Axios, Naive UI, Vue3Lottie, Animate

**Server:** SpringBoot, SaToken, WebSocket


## 👋WebSite 
- 移动端 : [Gobang-Mobile ](https://app4362.acapp.acwing.com.cn/)
- AcWing : [Gobang-AcApp](https://app4362.acapp.acwing.com.cn/)
**移动端展示**
![移动端展示](https://cdn.acwing.com/media/article/image/2023/01/15/36510_c3d7dd5a94-web.jpg)


**acapp展示**
![AcApp展示](https://cdn.acwing.com/media/article/image/2023/01/15/36510_debf52a994-acapp.jpg)
## 🤔Installation 

### Frontend

```bash
// cd到前端目录
cd gobang-frontend
// 安装相关依赖
npm install
// 运行
npx vite --port=4000
```

### AcApp

```bash
// cd到acapp目录
cd gobang-acapp
// 安装相关依赖
npm install
// 运行
npm run serve
```

### Backend

**mysql数据库配置**

```bash
// cd到后端目录
cd gobang-backend
// cd到sql文件夹
// 管理员账号密码: admin 123456
cd sql
// 导入数据库文件(使用命令行或者可视化工具都行)
source xx.sql
// idea打开文件利用maven工具导入依赖, 运行
```

[**redis安装**](https://www.runoob.com/redis/redis-install.html)

```bash
https://www.runoob.com/redis/redis-install.html
```

### Scripts

```bash
// scripts目录下为脚本文件

1. to_test => 切换为本地环境
2. to_dev => 切换为线上环境
3. to_acapp => 切换为acapp格式
4. scp_web => 打包上传web文件到服务器
5. build_server => 打包后端文件
6. scp_server => 上传后端文件到服务器
7. scp_acapp => 上传acapp到服务器
8. scp_full => 打包上传所有文件到服务器
```





