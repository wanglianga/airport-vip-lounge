# VIP 贵宾休息室管理系统 - 前端

## 原始需求

> 在 d:\code\solocode-wl-0608\wl-310\frontend 目录下创建完整的 Vue3 前端项目。
> 
> ## 技术栈
> - Vue 3 + Vite
> - Vue Router 4
> - Pinia
> - Element Plus
> - Axios
> - ECharts (用于大屏展示)
> - dayjs
> 
> ## 项目结构
> ```
> frontend/
> ├── package.json
> ├── vite.config.js
> ├── index.html
> ├── Dockerfile
> ├── .dockerignore
> ├── nginx.conf
> └── src/
>     ├── main.js
>     ├── App.vue
>     ├── router/
>     │   └── index.js
>     ├── stores/
>     │   └── app.js
>     ├── api/
>     │   ├── index.js
>     │   ├── passenger.js
>     │   ├── flight.js
>     │   ├── seat.js
>     │   ├── meal.js
>     │   ├── shower.js
>     │   ├── meeting.js
>     │   ├── vehicle.js
>     │   ├── service.js
>     │   ├── staff.js
>     │   └── dashboard.js
>     ├── utils/
>     │   └── request.js
>     ├── layouts/
>     │   └── MainLayout.vue
>     └── views/
>         ├── dashboard/
>         │   └── CommandCenter.vue   (指挥台总览大屏)
>         ├── airline/
>         │   ├── PassengerManage.vue (航司-旅客管理)
>         │   └── FlightManage.vue    (航司-航班管理)
>         ├── reception/
>         │   ├── CheckIn.vue         (前台-身份核验入住)
>         │   └── ServiceDispatch.vue (前台-服务调度)
>         ├── catering/
>         │   └── MealPrepare.vue     (餐饮-餐食准备)
>         └── fleet/
>             └── VehicleDispatch.vue (车队-车辆调度)
> ```
> 
> ## 页面功能详细设计
> 
> ### 1. 指挥台总览大屏 (CommandCenter.vue)
> 这是最重要的页面，类似机场指挥中心，需要展示：
> - 顶部：统计卡片 - 在室旅客数、待准备餐食、可用淋浴间、可用会议室、可用车辆、航班延误数
> - 左侧：在室旅客列表 - 显示旅客姓名、航班号、状态、座位号、使用的服务
> - 中间：实时活动流 - 时间线展示最近的服务活动（旅客入住、餐食备好、车辆派出等）
> - 右侧：今日航班动态 - 航班号、目的地、计划/实际起飞时间、登机口、状态
> - 底部：服务人员在岗情况 - 各角色在岗人数、忙碌人数
> - 整体风格：深色主题，科技感，类似大屏监控
> - 每30秒自动刷新数据
> 
> ### 2. 航司贵宾部 - 旅客管理 (PassengerManage.vue)
> - 旅客列表表格：姓名、身份证号、等级、航司、航班号、同行人数、儿童、外宾、餐食禁忌、远机位需求、状态
> - 搜索筛选：按姓名、航班号、等级、状态筛选
> - 新增旅客按钮和表单
> - 批量导入功能（模拟Excel导入，可用JSON）
> - 编辑、删除操作
> 
> ### 3. 航司贵宾部 - 航班管理 (FlightManage.vue)
> - 航班列表表格：航班号、航司、目的地、计划起飞、实际起飞、登机口、远机位、状态、旅客数
> - 搜索筛选
> - 新增/编辑航班
> - 更新航班状态
> 
> ### 4. 休息室前台 - 身份核验入住 (CheckIn.vue)
> - 左侧：搜索框 - 输入身份证号或姓名查询旅客
> - 旅客信息卡片：显示旅客详细信息、航班信息、权益等级
> - 右侧：可用座位图 - 按区域展示座位状态（不同颜色表示可用/占用/维修）
> - 操作按钮：确认入住、分配座位
> - 同行人信息展示
> 
> ### 5. 休息室前台 - 服务调度 (ServiceDispatch.vue)
> - 标签页切换：座位、淋浴间、会议室、儿童区、行李
> - 各资源的状态展示
> - 旅客选择和服务分配
> - 服务记录列表
> 
> ### 6. 餐饮供应商 - 餐食准备 (MealPrepare.vue)
> - 顶部统计：待准备、准备中、已备好、已送达
> - 餐食列表：按航班起飞时间排序
>   - 旅客姓名、航班号、起飞时间、餐食类型、数量、特殊要求、状态
> - 操作按钮：开始准备、标记备好、送出
> - 筛选：按状态、航班号筛选
> 
> ### 7. 接送车队 - 车辆调度 (VehicleDispatch.vue)
> - 车辆列表：车牌号、车型、司机、状态、当前任务、位置
> - 待派单列表：旅客姓名、航班号、登机口、远机位、送达时限
> - 派单操作：选择车辆并分配任务
> - 任务状态更新：出发、到达、返回
> 
> ## 技术要求
> 1. 使用 Element Plus 组件库
> 2. 所有API请求封装在 api 目录，使用 axios
> 3. 使用 Pinia 管理全局状态
> 4. 响应式布局，支持大屏展示
> 5. Dockerfile 使用 nginx 部署
> 6. 代理 /api 到后端 http://localhost:8080
> 7. 主布局包含侧边栏导航和顶部标题
> 8. 导航菜单包含：指挥台、航司贵宾部（子菜单：旅客管理、航班管理）、休息室前台（子菜单：入住登记、服务调度）、餐饮供应、车队调度
> 9. 使用模拟数据填充页面，确保页面能正常展示

## 启动方式

### 前置要求

- Node.js >= 16.0.0
- npm 或 pnpm
- Docker & Docker Compose（如需 Docker 部署）

### 本地开发启动

#### 1. 安装依赖

```bash
cd frontend
npm install
```

#### 2. 启动开发服务

```bash
npm run dev
```

访问地址：http://localhost:5173

#### 3. 生产构建

```bash
npm run build
```

构建产物将输出到 `dist` 目录。

### Docker 一键启动

#### 前置要求

- Docker >= 20.10
- Docker Compose >= 2.0

#### 启动命令

```bash
cd frontend
docker compose up --build
```

如需后台运行：

```bash
docker compose up --build -d
```

访问地址：http://localhost:80

#### 停止服务

```bash
docker compose down
```

## 项目简介

本项目是 VIP 贵宾休息室管理系统的前端部分，基于 Vue 3 + Vite 构建，使用 Element Plus 组件库，提供了指挥台大屏、旅客管理、航班管理、入住登记、服务调度、餐食准备、车辆调度等功能模块。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Vue Router 4** - Vue.js 官方路由
- **Pinia** - Vue 官方状态管理库
- **Element Plus** - 基于 Vue 3 的组件库
- **Axios** - HTTP 客户端
- **ECharts** - 数据可视化图表库
- **dayjs** - 轻量级日期处理库

## 功能模块

### 指挥台总览
- 深色科技感大屏风格
- 实时数据统计卡片
- 在室旅客列表
- 实时活动时间线
- 今日航班动态
- 服务人员在岗情况
- 每30秒自动刷新数据

### 航司贵宾部
- 旅客信息管理（增删改查）
- 旅客批量导入（JSON格式）
- 航班信息管理
- 航班状态更新
- 多条件搜索筛选

### 休息室前台
- 旅客身份核验与入住
- 座位图可视化展示
- 座位分配与释放
- 淋浴间管理
- 会议室预约与管理
- 多服务类型调度

### 餐饮供应
- 餐食状态统计
- 餐食准备流程管理
- 特殊要求标注
- 按航班起飞时间排序

### 车队调度
- 车辆状态管理
- 待派单列表
- 派单操作
- 任务状态跟踪

## API 代理配置

开发环境下，Vite 代理配置已将 `/api` 路径代理到 `http://localhost:8080`。

生产环境下，Nginx 配置已将 `/api/` 路径代理到后端服务 `backend:8080`。

## 目录结构

```
frontend/
├── package.json          # 项目依赖配置
├── vite.config.js        # Vite 配置
├── index.html            # HTML 入口
├── Dockerfile            # Docker 构建配置
├── .dockerignore         # Docker 忽略文件
├── nginx.conf            # Nginx 配置
└── src/
    ├── main.js           # 应用入口
    ├── App.vue           # 根组件
    ├── router/           # 路由配置
    ├── stores/           # Pinia 状态管理
    ├── api/              # API 接口封装
    ├── utils/            # 工具函数
    ├── layouts/          # 布局组件
    └── views/            # 页面组件
```

## 注意事项

1. 当前版本使用模拟数据（Mock Data）展示页面效果
2. 接入真实后端时，只需修改 `src/utils/request.js` 和各 API 文件
3. 大屏页面建议在 1920x1080 及以上分辨率下查看效果最佳
4. Docker 部署时，请确保后端服务名称为 `backend` 且在同一网络中
