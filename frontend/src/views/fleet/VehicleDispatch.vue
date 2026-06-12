<template>
  <div class="vehicle-dispatch">
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card class="vehicle-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">车辆列表</span>
              <div class="vehicle-stats">
                <el-tag type="success" size="small">空闲 {{ stats.idle }}</el-tag>
                <el-tag type="primary" size="small">任务中 {{ stats.busy }}</el-tag>
                <el-tag type="info" size="small">维护中 {{ stats.maintenance }}</el-tag>
              </div>
            </div>
          </template>
          <el-table :data="vehicleList" border style="width: 100%" v-loading="loading">
            <el-table-column prop="plateNo" label="车牌号" width="110" />
            <el-table-column prop="model" label="车型" width="120" />
            <el-table-column prop="driver" label="司机" width="100" />
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)" size="small">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="currentTask" label="当前任务" min-width="160">
              <template #default="{ row }">
                {{ row.currentTask || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="location" label="位置" width="130" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button 
                  v-if="row.status === '空闲' && selectedTask" 
                  type="primary" 
                  link 
                  size="small"
                  @click="handleAssignTask(row)"
                >
                  派单
                </el-button>
                <el-dropdown 
                  v-if="row.status !== '维护中'" 
                  trigger="click" 
                  @command="(cmd) => handleStatusUpdate(row, cmd)"
                >
                  <el-button type="success" link size="small">
                    更新状态
                    <el-icon class="el-icon--right"><arrow-down /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="空闲">空闲</el-dropdown-item>
                      <el-dropdown-item command="任务中">任务中</el-dropdown-item>
                      <el-dropdown-item command="返回中">返回中</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card class="task-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">待派单列表</span>
              <el-tag type="warning" size="small">{{ pendingTasks.length }} 单</el-tag>
            </div>
          </template>
          <div class="task-list">
            <div 
              v-for="task in pendingTasks" 
              :key="task.id"
              class="task-item"
              :class="{ active: selectedTask?.id === task.id }"
              @click="selectTask(task)"
            >
              <div class="task-header">
                <span class="task-passenger">{{ task.passengerName }}</span>
                <el-tag type="warning" size="small">{{ task.status }}</el-tag>
              </div>
              <div class="task-body">
                <div class="task-info">
                  <el-icon><Van /></el-icon>
                  <span>{{ task.flightNo }}</span>
                </div>
                <div class="task-info">
                  <el-icon><Location /></el-icon>
                  <span>{{ task.gate }} 登机口</span>
                </div>
                <div class="task-info" v-if="task.isRemoteStand">
                  <el-tag type="danger" size="small">远机位</el-tag>
                </div>
              </div>
              <div class="task-footer">
                <el-icon><Clock /></el-icon>
                <span>送达时限：{{ task.deadline }}</span>
              </div>
            </div>

            <el-empty v-if="pendingTasks.length === 0" description="暂无待派单" />
          </div>
        </el-card>

        <el-card class="task-record-card" style="margin-top: 16px">
          <template #header>
            <span class="card-title">任务状态更新</span>
          </template>
          <el-table :data="allTasks" border size="small">
            <el-table-column prop="passengerName" label="旅客" width="80" />
            <el-table-column prop="flightNo" label="航班" width="90" />
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="getTaskStatusType(row.status)" size="small">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160">
              <template #default="{ row }">
                <el-button 
                  v-if="row.status === '进行中'" 
                  type="primary" 
                  link 
                  size="small"
                  @click="updateTaskStatus(row, '已到达')"
                >
                  到达
                </el-button>
                <el-button 
                  v-if="row.status === '已到达'" 
                  type="success" 
                  link 
                  size="small"
                  @click="updateTaskStatus(row, '返回中')"
                >
                  返回
                </el-button>
                <el-button 
                  v-if="row.status === '返回中'" 
                  type="info" 
                  link 
                  size="small"
                  @click="updateTaskStatus(row, '已完成')"
                >
                  完成
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { vehicleApi } from '@/api'
import { 
  Van, 
  Location, 
  Clock,
  ArrowDown
} from '@element-plus/icons-vue'

const loading = ref(false)
const vehicleList = ref([])
const allTasks = ref([])
const selectedTask = ref(null)

const stats = ref({
  total: 0,
  idle: 0,
  busy: 0,
  maintenance: 0,
  pendingTasks: 0
})

const pendingTasks = computed(() => {
  return allTasks.value.filter(t => t.status === '待派单')
})

const getStatusTagType = (status) => {
  const map = {
    '空闲': 'success',
    '任务中': 'primary',
    '返回中': 'warning',
    '维护中': 'info'
  }
  return map[status] || 'info'
}

const getTaskStatusType = (status) => {
  const map = {
    '待派单': 'warning',
    '进行中': 'primary',
    '已到达': 'success',
    '返回中': 'warning',
    '已完成': 'info'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const [vehicleRes, taskRes, statsRes] = await Promise.all([
      vehicleApi.getVehicleList(),
      vehicleApi.getTaskList(),
      vehicleApi.getStatistics()
    ])
    if (vehicleRes.code === 200) vehicleList.value = vehicleRes.data
    if (taskRes.code === 200) allTasks.value = taskRes.data
    if (statsRes.code === 200) stats.value = statsRes.data
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const selectTask = (task) => {
  selectedTask.value = selectedTask.value?.id === task.id ? null : task
}

const handleAssignTask = (vehicle) => {
  if (!selectedTask.value) {
    ElMessage.warning('请先选择一个待派单')
    return
  }
  ElMessageBox.confirm(
    `确定将 ${selectedTask.value.passengerName} 的任务派给 ${vehicle.driver}（${vehicle.plateNo}）吗？`,
    '派单确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(async () => {
    try {
      const res = await vehicleApi.assignTask(vehicle.id, selectedTask.value.id)
      if (res.code === 200) {
        ElMessage.success('派单成功')
        selectedTask.value = null
        loadData()
      }
    } catch (e) {
      ElMessage.error('派单失败')
    }
  })
}

const handleStatusUpdate = async (vehicle, status) => {
  try {
    const res = await vehicleApi.updateVehicleStatus(vehicle.id, status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      loadData()
    }
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const updateTaskStatus = async (task, status) => {
  try {
    const res = await vehicleApi.updateTaskStatus(task.id, status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      loadData()
    }
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.vehicle-dispatch {
  height: calc(100vh - 100px);
}

.vehicle-card,
.task-card,
.task-record-card {
  height: 100%;
  border-radius: 8px;
}

.task-record-card {
  height: auto;
  max-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.vehicle-stats {
  display: flex;
  gap: 8px;
}

.task-list {
  max-height: 340px;
  overflow-y: auto;
}

.task-item {
  padding: 12px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.task-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
}

.task-item.active {
  border-color: #409eff;
  background: #ecf5ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.task-passenger {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}

.task-body {
  margin-bottom: 10px;
}

.task-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #606266;
  margin-bottom: 6px;
}

.task-footer {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #e6a23c;
  padding-top: 8px;
  border-top: 1px dashed #ebeef5;
}

.el-row {
  height: 100%;
}

.el-col {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.vehicle-card :deep(.el-card__body) {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.vehicle-card :deep(.el-table) {
  flex: 1;
}
</style>
