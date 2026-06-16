# 机场贵宾休息室服务调度平台

一个面向机场贵宾休息室的一体化服务调度平台，连接航司贵宾部、休息室前台、餐饮供应商和接送车队，实现旅客服务全流程数字化管理。

## 原始需求

> 请实现机场贵宾休息室服务调度平台，Vue3 页面给航司贵宾部、休息室前台、餐饮供应商和接送车队使用，Spring Boot 接口维护旅客权益、航班动态、座位资源、餐食准备、淋浴间、会议室和送机车辆。航司导入旅客等级、航班、同行人、儿童或外宾信息、餐食禁忌和远机位需求；前台核验身份后安排座位、行李照看、淋浴、会议室或儿童区；餐饮商按起飞时间准备热餐、酒水和打包点心；车队根据安检状态、登机口和摆渡任务安排送机。这个产品要像机场现场的指挥台，能同时看见旅客是否入室、餐食是否备好、航班是否变更、车辆是否接单和哪位服务员正在处理，不能让高端旅客服务散落在纸质名单、微信群和电话里。

> 新增航班变更联动：航班延误、取消、换登机口或远机位调整后，航司先同步航班状态，平台重新计算座位占用、餐食保温、淋浴预约、会议室时段和摆渡车等待；前台逐个确认旅客是否继续休息、改签离开或需要打包餐食，车队收到新的送机窗口后再更新车辆任务，整条链路要留下谁通知、旅客怎么回复和哪些服务被延长。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Java 17

### 前端
- Vue 3 + Vite
- Vue Router 4
- Pinia 状态管理
- Element Plus UI 组件库
- ECharts 图表库
- Axios HTTP 客户端
- dayjs 日期处理

## 项目结构

```
wl-310/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/...
│   ├── sql/init.sql           # 数据库初始化脚本
│   ├── pom.xml
│   ├── Dockerfile
│   └── .dockerignore
├── frontend/                   # Vue3 前端
│   ├── src/
│   │   ├── api/               # API 接口模块
│   │   ├── views/             # 页面组件
│   │   ├── layouts/           # 布局组件
│   │   ├── router/            # 路由配置
│   │   ├── stores/            # Pinia 状态
│   │   └── utils/             # 工具函数
│   ├── package.json
│   ├── vite.config.js
│   ├── nginx.conf
│   ├── Dockerfile
│   └── .dockerignore
├── docker-compose.yml          # Docker Compose 编排
├── Dockerfile                  # 根目录 Dockerfile（默认构建后端）
├── .dockerignore               # 根目录 Docker 忽略
└── README.md
```

## 功能模块

### 指挥台总览大屏
- 实时统计：在室旅客数、待准备餐食、可用淋浴间、可用会议室、可用车辆、航班动态
- 在室旅客列表：实时展示旅客状态、座位、使用服务
- 实时活动流：时间线展示服务活动记录
- 今日航班动态：航班状态、登机口、起飞时间变更
- 服务人员在岗情况：各角色人员状态统计

### 航司贵宾部
- 旅客管理：旅客信息维护、等级设置、餐食禁忌、远机位需求
- 航班管理：航班动态维护、登机口变更、状态更新
- 航班变更联动：航班延误、取消、换登机口、远机位调整的全链路联动
- 批量导入：支持旅客信息批量导入
- 同行人、儿童、外宾信息管理

### 休息室前台
- 身份核验：身份证/姓名查询旅客信息
- 入住登记：核验身份后安排座位
- 座位图可视化：按区域展示座位状态
- 服务调度：淋浴间、会议室、儿童区、行李照看
- 服务记录管理

### 餐饮供应商
- 餐食准备：按航班起飞时间排序展示
- 餐食类型：热餐、酒水、点心、打包餐
- 状态流转：待准备 → 准备中 → 已备好 → 已送达
- 特殊饮食要求管理

### 接送车队
- 车辆调度：车辆状态、位置管理
- 派单管理：根据航班安排送机任务
- 任务跟踪：出发、到达、返回状态
- 远机位摆渡任务管理

## 启动方式

### 方式一：Docker 一键启动（推荐）

#### 前置要求

- Docker 20.10+
- Docker Compose 2.0+

#### 启动步骤

```bash
docker compose up --build
```

后台运行：

```bash
docker compose up --build -d
```

停止服务：

```bash
docker compose down
```

查看日志：

```bash
docker compose logs -f
```

#### 访问地址

- 前端页面：http://localhost
- 后端 API：http://localhost:8080
- 数据库：localhost:3306（用户名：vip_lounge，密码：vip_lounge_2024）

### 方式二：本地开发启动

#### 前置要求

- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+

#### 1. 准备数据库

创建数据库并执行初始化脚本：

```bash
mysql -u root -p
```

```sql
CREATE DATABASE vip_lounge CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vip_lounge;
SOURCE backend/sql/init.sql;
```

#### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests
java -jar target/vip-lounge-1.0.0.jar
```

后端服务地址：http://localhost:8080

#### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端访问地址：http://localhost:5173

## API 接口概览

### 指挥台
- `GET /api/dashboard/summary` - 获取总览统计数据
- `GET /api/dashboard/passengers` - 在室旅客列表
- `GET /api/dashboard/flights` - 今日航班动态
- `GET /api/dashboard/activities` - 实时活动流

### 旅客管理
- `GET /api/passengers` - 旅客列表（分页）
- `GET /api/passengers/{id}` - 旅客详情
- `POST /api/passengers` - 新增旅客
- `PUT /api/passengers/{id}` - 更新旅客
- `DELETE /api/passengers/{id}` - 删除旅客
- `POST /api/passengers/batch-import` - 批量导入
- `PUT /api/passengers/{id}/check-in` - 入住登记
- `PUT /api/passengers/{id}/check-out` - 离店

### 航班管理
- `GET /api/flights` - 航班列表
- `POST /api/flights` - 新增航班
- `PUT /api/flights/{id}/status` - 更新航班状态

### 资源管理
- 座位：`/api/seats`
- 淋浴间：`/api/shower-rooms`
- 会议室：`/api/meeting-rooms`
- 车辆：`/api/vehicles`
- 餐食：`/api/meals`

### 服务记录
- `GET /api/service-records` - 服务记录列表
- `POST /api/service-records` - 创建服务记录
- `PUT /api/service-records/{id}/complete` - 完成服务

### 人员管理
- `GET /api/staffs` - 服务人员列表
- `POST /api/staffs` - 新增服务人员

## 注意事项

1. 项目首次启动会自动执行 SQL 初始化脚本，包含示例数据
2. 默认数据库账号密码请根据实际情况修改
3. 前端开发模式下通过 Vite 代理访问后端 API
4. Docker 部署时通过 Nginx 反向代理 API 请求
5. 所有接口返回统一格式：`{code: 200, message: "success", data: ...}`
