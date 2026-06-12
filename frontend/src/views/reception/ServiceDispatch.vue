<template>
  <div class="service-dispatch">
    <el-card class="main-card">
      <el-tabs v-model="activeTab" class="service-tabs">
        <el-tab-pane label="座位" name="seat">
          <div class="tab-content">
            <div class="content-header">
              <div class="stats-row">
                <el-statistic title="总数" :value="seatStats.total" />
                <el-statistic title="可用" :value="seatStats.available" />
                <el-statistic title="占用" :value="seatStats.occupied" />
                <el-statistic title="维修" :value="seatStats.maintenance" />
              </div>
            </div>
            <el-table :data="seatList" border style="width: 100%">
              <el-table-column prop="seatNo" label="座位号" width="100" />
              <el-table-column prop="area" label="区域" width="80" />
              <el-table-column prop="type" label="类型" width="120" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getSeatStatusType(row.status)" size="small">
                    {{ row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="passengerName" label="使用人" width="120">
                <template #default="{ row }">
                  {{ row.passengerName || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <el-button 
                    v-if="row.status === '可用'" 
                    type="primary" 
                    link 
                    size="small"
                    @click="handleSeatAssign(row)"
                  >
                    分配
                  </el-button>
                  <el-button 
                    v-if="row.status === '占用'" 
                    type="success" 
                    link 
                    size="small"
                    @click="handleSeatRelease(row)"
                  >
                    释放
                  </el-button>
                  <el-button 
                    v-if="row.status !== '维修'" 
                    type="warning" 
                    link 
                    size="small"
                    @click="handleSeatMaintenance(row)"
                  >
                    设为维修
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="淋浴间" name="shower">
          <div class="tab-content">
            <div class="content-header">
              <div class="stats-row">
                <el-statistic title="总数" :value="showerStats.total" />
                <el-statistic title="可用" :value="showerStats.available" />
                <el-statistic title="使用中" :value="showerStats.inUse" />
                <el-statistic title="清洁中" :value="showerStats.cleaning" />
              </div>
            </div>
            <div class="room-grid">
              <div 
                v-for="room in showerList" 
                :key="room.id"
                class="room-card"
                :class="room.status.toLowerCase()"
              >
                <div class="room-header">
                  <span class="room-no">{{ room.roomNo }}</span>
                  <el-tag :type="getShowerStatusType(room.status)" size="small">
                    {{ room.status }}
                  </el-tag>
                </div>
                <div v-if="room.passengerName" class="room-info">
                  <div>使用人：{{ room.passengerName }}</div>
                  <div>开始时间：{{ room.startTime }}</div>
                  <div>时长：{{ room.duration }}分钟</div>
                </div>
                <div v-else class="room-empty">空闲</div>
                <div class="room-actions">
                  <el-button 
                    v-if="room.status === '可用'" 
                    type="primary" 
                    size="small"
                    @click="handleShowerAssign(room)"
                  >
                    分配
                  </el-button>
                  <el-button 
                    v-if="room.status === '使用中'" 
                    type="success" 
                    size="small"
                    @click="handleShowerFinish(room)"
                  >
                    结束
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="会议室" name="meeting">
          <div class="tab-content">
            <div class="content-header">
              <div class="stats-row">
                <el-statistic title="总数" :value="meetingStats.total" />
                <el-statistic title="可用" :value="meetingStats.available" />
                <el-statistic title="使用中" :value="meetingStats.inUse" />
                <el-statistic title="预约中" :value="meetingStats.reserved" />
              </div>
            </div>
            <el-table :data="meetingList" border style="width: 100%">
              <el-table-column prop="roomNo" label="房间号" width="100" />
              <el-table-column prop="name" label="名称" width="140" />
              <el-table-column prop="capacity" label="容纳人数" width="100" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getMeetingStatusType(row.status)" size="small">
                    {{ row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="currentMeeting" label="当前会议" width="160">
                <template #default="{ row }">
                  {{ row.currentMeeting || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <el-button 
                    v-if="row.status === '可用'" 
                    type="primary" 
                    link 
                    size="small"
                    @click="handleMeetingReserve(row)"
                  >
                    预约
                  </el-button>
                  <el-button 
                    v-if="row.status === '预约中'" 
                    type="primary" 
                    link 
                    size="small"
                    @click="handleMeetingStart(row)"
                  >
                    开始
                  </el-button>
                  <el-button 
                    v-if="row.status === '使用中'" 
                    type="success" 
                    link 
                    size="small"
                    @click="handleMeetingEnd(row)"
                  >
                    结束
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="儿童区" name="kids">
          <div class="tab-content">
            <el-empty description="儿童区功能开发中" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="行李" name="luggage">
          <div class="tab-content">
            <el-empty description="行李服务功能开发中" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-card class="service-record-card">
      <div class="card-header">
        <div class="card-title">服务记录</div>
      </div>
      <el-table :data="serviceList" border style="width: 100%" v-loading="serviceLoading">
        <el-table-column prop="type" label="服务类型" width="120" />
        <el-table-column prop="passengerName" label="旅客姓名" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getServiceStatusType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="120" />
        <el-table-column prop="endTime" label="结束时间" width="120">
          <template #default="{ row }">
            {{ row.endTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="120">
          <template #default="{ row }">
            {{ row.operator || '-' }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { seatApi, showerApi, meetingApi, serviceApi } from '@/api'

const activeTab = ref('seat')

const seatList = ref([])
const seatStats = ref({ total: 0, available: 0, occupied: 0, maintenance: 0 })

const showerList = ref([])
const showerStats = ref({ total: 0, available: 0, inUse: 0, cleaning: 0, maintenance: 0 })

const meetingList = ref([])
const meetingStats = ref({ total: 0, available: 0, inUse: 0, reserved: 0 })

const serviceList = ref([])
const serviceLoading = ref(false)

const getSeatStatusType = (status) => {
  const map = { '可用': 'success', '占用': 'danger', '维修': 'info' }
  return map[status] || 'info'
}

const getShowerStatusType = (status) => {
  const map = { '可用': 'success', '使用中': 'primary', '清洁中': 'warning', '维修': 'info' }
  return map[status] || 'info'
}

const getMeetingStatusType = (status) => {
  const map = { '可用': 'success', '使用中': 'primary', '预约中': 'warning' }
  return map[status] || 'info'
}

const getServiceStatusType = (status) => {
  const map = { '待服务': 'warning', '进行中': 'primary', '已完成': 'success' }
  return map[status] || 'info'
}

const loadSeatData = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      seatApi.getList(),
      seatApi.getStatistics()
    ])
    if (listRes.code === 200) seatList.value = listRes.data.list
    if (statsRes.code === 200) seatStats.value = statsRes.data
  } catch (e) {
    console.error(e)
  }
}

const loadShowerData = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      showerApi.getList(),
      showerApi.getStatistics()
    ])
    if (listRes.code === 200) showerList.value = listRes.data
    if (statsRes.code === 200) showerStats.value = statsRes.data
  } catch (e) {
    console.error(e)
  }
}

const loadMeetingData = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      meetingApi.getList(),
      meetingApi.getStatistics()
    ])
    if (listRes.code === 200) meetingList.value = listRes.data
    if (statsRes.code === 200) meetingStats.value = statsRes.data
  } catch (e) {
    console.error(e)
  }
}

const loadServiceList = async () => {
  serviceLoading.value = true
  try {
    const res = await serviceApi.getList()
    if (res.code === 200) serviceList.value = res.data.list
  } catch (e) {
    console.error(e)
  } finally {
    serviceLoading.value = false
  }
}

const handleSeatAssign = (row) => {
  ElMessage.info('请先选择旅客后再分配')
}

const handleSeatRelease = async (row) => {
  try {
    const res = await seatApi.release(row.id)
    if (res.code === 200) {
      ElMessage.success('释放成功')
      loadSeatData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleSeatMaintenance = async (row) => {
  try {
    const res = await seatApi.setMaintenance(row.id)
    if (res.code === 200) {
      ElMessage.success('设置成功')
      loadSeatData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleShowerAssign = (room) => {
  ElMessage.info('请先选择旅客后再分配')
}

const handleShowerFinish = async (room) => {
  try {
    const res = await showerApi.finish(room.id)
    if (res.code === 200) {
      ElMessage.success('已完成')
      loadShowerData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleMeetingReserve = (room) => {
  ElMessage.info('预约功能演示中')
}

const handleMeetingStart = async (room) => {
  try {
    const res = await meetingApi.startMeeting(room.id)
    if (res.code === 200) {
      ElMessage.success('会议已开始')
      loadMeetingData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleMeetingEnd = async (room) => {
  try {
    const res = await meetingApi.endMeeting(room.id)
    if (res.code === 200) {
      ElMessage.success('会议已结束')
      loadMeetingData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadSeatData()
  loadShowerData()
  loadMeetingData()
  loadServiceList()
})
</script>

<style scoped>
.service-dispatch {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.main-card {
  border-radius: 8px;
}

.service-tabs {
  min-height: 400px;
}

.tab-content {
  padding: 8px 0;
}

.content-header {
  margin-bottom: 16px;
}

.stats-row {
  display: flex;
  gap: 32px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 16px;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.room-card {
  padding: 16px;
  border-radius: 8px;
  border: 2px solid #ebeef5;
  background: #fff;
}

.room-card.可用 {
  border-color: #67c23a;
}

.room-card.使用中 {
  border-color: #409eff;
  background: #ecf5ff;
}

.room-card.清洁中 {
  border-color: #e6a23c;
  background: #fdf6ec;
}

.room-card.维修 {
  border-color: #909399;
  opacity: 0.6;
}

.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.room-no {
  font-size: 18px;
  font-weight: 600;
}

.room-info {
  font-size: 13px;
  color: #606266;
  line-height: 1.8;
  margin-bottom: 12px;
}

.room-empty {
  text-align: center;
  color: #c0c4cc;
  padding: 20px 0;
}

.room-actions {
  display: flex;
  justify-content: center;
}

.service-record-card {
  border-radius: 8px;
}

.card-header {
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
</style>
