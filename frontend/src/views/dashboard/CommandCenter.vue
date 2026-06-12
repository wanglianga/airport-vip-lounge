<template>
  <div class="command-center">
    <div class="cc-header">
      <div class="cc-title">
        <h1>VIP 贵宾休息室指挥中心</h1>
        <span class="cc-subtitle">实时监控大屏</span>
      </div>
      <div class="cc-time">
        <div class="time-display">{{ currentTime }}</div>
        <div class="date-display">{{ currentDate }}</div>
      </div>
    </div>

    <div class="cc-stats">
      <div class="stat-card" v-for="stat in stats" :key="stat.label">
        <div class="stat-icon" :class="stat.iconClass">
          <el-icon><component :is="stat.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </div>

    <div class="cc-main">
      <div class="cc-left">
        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">在室旅客</span>
            <span class="panel-count">{{ inRoomPassengers.length }} 人</span>
          </div>
          <div class="panel-body passenger-list">
            <div class="passenger-item" v-for="p in inRoomPassengers" :key="p.id">
              <div class="passenger-avatar">
                <el-avatar :size="36">{{ p.name.charAt(0) }}</el-avatar>
              </div>
              <div class="passenger-info">
                <div class="passenger-name">{{ p.name }}</div>
                <div class="passenger-detail">
                  <span class="flight-tag">{{ p.flightNo }}</span>
                  <span class="seat-tag">{{ p.seatNo }}</span>
                </div>
              </div>
              <div class="passenger-services">
                <el-tag 
                  v-for="svc in p.services" 
                  :key="svc" 
                  size="small" 
                  type="info"
                  effect="dark"
                >
                  {{ svc }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="cc-center">
        <div class="panel activity-panel">
          <div class="panel-header">
            <span class="panel-title">实时活动流</span>
            <span class="refresh-indicator">
              <el-icon class="is-loading"><Refresh /></el-icon>
              自动刷新
            </span>
          </div>
          <div class="panel-body activity-timeline">
            <div class="timeline-item" v-for="item in activityStream" :key="item.id">
              <div class="timeline-time">{{ item.time }}</div>
              <div class="timeline-dot" :class="item.type"></div>
              <div class="timeline-content">
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-desc">{{ item.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="cc-right">
        <div class="panel">
          <div class="panel-header">
            <span class="panel-title">今日航班动态</span>
          </div>
          <div class="panel-body flight-list">
            <div class="flight-item" v-for="f in todayFlights" :key="f.id">
              <div class="flight-header">
                <span class="flight-no">{{ f.flightNo }}</span>
                <el-tag 
                  :type="getFlightStatusType(f.status)" 
                  size="small"
                  effect="dark"
                >
                  {{ f.status }}
                </el-tag>
              </div>
              <div class="flight-body">
                <span class="flight-dest">{{ f.destination }}</span>
                <span class="flight-gate">{{ f.gate }} 登机口</span>
              </div>
              <div class="flight-footer">
                <span>计划 {{ f.plannedDeparture }}</span>
                <span v-if="f.actualDeparture">实际 {{ f.actualDeparture }}</span>
                <el-tag v-if="f.isRemoteStand" size="small" type="warning" effect="dark">远机位</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="cc-footer">
      <div class="panel staff-panel">
        <div class="panel-header">
          <span class="panel-title">服务人员在岗情况</span>
        </div>
        <div class="panel-body staff-grid">
          <div class="staff-card" v-for="s in staffStatus" :key="s.role">
            <div class="staff-role">{{ s.role }}</div>
            <div class="staff-stats">
              <div class="staff-stat">
                <span class="stat-num on-duty">{{ s.onDuty }}</span>
                <span class="stat-text">在岗</span>
              </div>
              <div class="staff-stat">
                <span class="stat-num busy">{{ s.busy }}</span>
                <span class="stat-text">忙碌</span>
              </div>
              <div class="staff-stat">
                <span class="stat-num idle">{{ s.onDuty - s.busy }}</span>
                <span class="stat-text">空闲</span>
              </div>
            </div>
            <div class="staff-bar">
              <div 
                class="bar-fill busy-bar" 
                :style="{ width: (s.busy / s.onDuty * 100) + '%' }"
              ></div>
              <div 
                class="bar-fill idle-bar" 
                :style="{ width: ((s.onDuty - s.busy) / s.onDuty * 100) + '%' }"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { dashboardApi } from '@/api'
import { 
  User, 
  Coffee, 
  House, 
  OfficeBuilding, 
  Van, 
  Warning,
  Refresh 
} from '@element-plus/icons-vue'

const currentTime = ref('')
const currentDate = ref('')
const inRoomPassengers = ref([])
const activityStream = ref([])
const todayFlights = ref([])
const staffStatus = ref([])
const overview = ref({
  inRoomPassengers: 0,
  pendingMeals: 0,
  availableShowers: 0,
  availableMeetings: 0,
  availableVehicles: 0,
  delayedFlights: 0
})

const stats = computed(() => [
  { label: '在室旅客', value: overview.value.inRoomPassengers, icon: User, iconClass: 'icon-blue' },
  { label: '待准备餐食', value: overview.value.pendingMeals, icon: Coffee, iconClass: 'icon-orange' },
  { label: '可用淋浴间', value: overview.value.availableShowers, icon: House, iconClass: 'icon-cyan' },
  { label: '可用会议室', value: overview.value.availableMeetings, icon: OfficeBuilding, iconClass: 'icon-purple' },
  { label: '可用车辆', value: overview.value.availableVehicles, icon: Van, iconClass: 'icon-green' },
  { label: '航班延误', value: overview.value.delayedFlights, icon: Warning, iconClass: 'icon-red' }
])

let timeTimer = null
let dataTimer = null

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
  currentDate.value = now.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric', 
    weekday: 'long' 
  })
}

const loadData = async () => {
  try {
    const [overviewRes, passengersRes, activityRes, flightsRes, staffRes] = await Promise.all([
      dashboardApi.getOverview(),
      dashboardApi.getInRoomPassengers(),
      dashboardApi.getActivityStream(),
      dashboardApi.getTodayFlights(),
      dashboardApi.getStaffStatus()
    ])
    
    if (overviewRes.code === 200) {
      overview.value = overviewRes.data
    }
    if (passengersRes.code === 200) {
      inRoomPassengers.value = passengersRes.data
    }
    if (activityRes.code === 200) {
      activityStream.value = activityRes.data
    }
    if (flightsRes.code === 200) {
      todayFlights.value = flightsRes.data
    }
    if (staffRes.code === 200) {
      staffStatus.value = staffRes.data
    }
  } catch (e) {
    console.error('加载数据失败', e)
  }
}

const getFlightStatusType = (status) => {
  const map = {
    '计划中': 'info',
    '延误': 'warning',
    '登机中': 'primary',
    '已起飞': 'success',
    '取消': 'danger'
  }
  return map[status] || 'info'
}

onMounted(() => {
  updateTime()
  loadData()
  timeTimer = setInterval(updateTime, 1000)
  dataTimer = setInterval(loadData, 30000)
})

onUnmounted(() => {
  if (timeTimer) clearInterval(timeTimer)
  if (dataTimer) clearInterval(dataTimer)
})
</script>

<style scoped>
.command-center {
  background: linear-gradient(135deg, #0a1628 0%, #1a2a4a 100%);
  min-height: calc(100vh - 100px);
  padding: 16px;
  color: #fff;
  border-radius: 8px;
}

.cc-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(64, 158, 255, 0.3);
}

.cc-title h1 {
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(90deg, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.cc-subtitle {
  color: #8492a6;
  font-size: 14px;
}

.cc-time {
  text-align: right;
}

.time-display {
  font-size: 32px;
  font-weight: bold;
  font-family: 'Courier New', monospace;
  color: #409eff;
}

.date-display {
  color: #8492a6;
  font-size: 14px;
}

.cc-stats {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(64, 158, 255, 0.2);
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;
}

.stat-card:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(64, 158, 255, 0.5);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.icon-blue {
  background: rgba(64, 158, 255, 0.2);
  color: #409eff;
}

.icon-orange {
  background: rgba(230, 162, 60, 0.2);
  color: #e6a23c;
}

.icon-cyan {
  background: rgba(90, 200, 250, 0.2);
  color: #5ac8fa;
}

.icon-purple {
  background: rgba(155, 89, 182, 0.2);
  color: #9b59b6;
}

.icon-green {
  background: rgba(103, 194, 58, 0.2);
  color: #67c23a;
}

.icon-red {
  background: rgba(245, 108, 108, 0.2);
  color: #f56c6c;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1;
}

.stat-label {
  font-size: 13px;
  color: #8492a6;
  margin-top: 4px;
}

.cc-main {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.panel {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(64, 158, 255, 0.15);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  height: 400px;
}

.panel-header {
  padding: 12px 16px;
  border-bottom: 1px solid rgba(64, 158, 255, 0.15);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  font-size: 16px;
  font-weight: 500;
  color: #409eff;
}

.panel-count {
  font-size: 13px;
  color: #67c23a;
}

.refresh-indicator {
  font-size: 12px;
  color: #8492a6;
  display: flex;
  align-items: center;
  gap: 4px;
}

.panel-body {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.passenger-list {
  padding: 8px;
}

.passenger-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 6px;
  margin-bottom: 8px;
  transition: background 0.2s;
}

.passenger-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.passenger-info {
  flex: 1;
  min-width: 0;
}

.passenger-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
}

.passenger-detail {
  display: flex;
  gap: 8px;
  font-size: 12px;
  color: #8492a6;
}

.passenger-services {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  max-width: 120px;
  justify-content: flex-end;
}

.activity-panel {
  height: 400px;
}

.activity-timeline {
  padding: 8px 16px;
}

.timeline-item {
  display: flex;
  gap: 12px;
  padding-bottom: 16px;
  position: relative;
}

.timeline-item:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 75px;
  top: 24px;
  bottom: 0;
  width: 1px;
  background: rgba(64, 158, 255, 0.2);
}

.timeline-time {
  font-size: 12px;
  color: #8492a6;
  min-width: 60px;
  font-family: 'Courier New', monospace;
}

.timeline-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-top: 4px;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
}

.timeline-dot.checkin {
  background: #409eff;
  box-shadow: 0 0 8px #409eff;
}

.timeline-dot.meal {
  background: #e6a23c;
  box-shadow: 0 0 8px #e6a23c;
}

.timeline-dot.vehicle {
  background: #67c23a;
  box-shadow: 0 0 8px #67c23a;
}

.timeline-dot.shower {
  background: #5ac8fa;
  box-shadow: 0 0 8px #5ac8fa;
}

.timeline-dot.meeting {
  background: #9b59b6;
  box-shadow: 0 0 8px #9b59b6;
}

.timeline-content {
  flex: 1;
}

.timeline-title {
  font-size: 14px;
  margin-bottom: 2px;
}

.timeline-desc {
  font-size: 12px;
  color: #8492a6;
}

.flight-list {
  padding: 8px;
}

.flight-item {
  padding: 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 6px;
  margin-bottom: 8px;
  transition: background 0.2s;
}

.flight-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.flight-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.flight-no {
  font-size: 15px;
  font-weight: 600;
}

.flight-body {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #c0c4cc;
  margin-bottom: 6px;
}

.flight-footer {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #8492a6;
  align-items: center;
}

.cc-footer {
  width: 100%;
}

.staff-panel {
  height: auto;
}

.staff-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 16px;
}

.staff-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(64, 158, 255, 0.1);
  border-radius: 8px;
  padding: 16px;
}

.staff-role {
  font-size: 15px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #409eff;
}

.staff-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 12px;
}

.staff-stat {
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-num.on-duty {
  color: #409eff;
}

.stat-num.busy {
  color: #e6a23c;
}

.stat-num.idle {
  color: #67c23a;
}

.stat-text {
  font-size: 12px;
  color: #8492a6;
}

.staff-bar {
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  display: flex;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
}

.busy-bar {
  background: #e6a23c;
}

.idle-bar {
  background: #67c23a;
}

.panel-body::-webkit-scrollbar {
  width: 4px;
}

.panel-body::-webkit-scrollbar-track {
  background: transparent;
}

.panel-body::-webkit-scrollbar-thumb {
  background: rgba(64, 158, 255, 0.3);
  border-radius: 2px;
}
</style>
