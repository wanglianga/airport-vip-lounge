<template>
  <div class="meal-prepare">
    <el-card class="stats-card">
      <div class="stats-grid">
        <div class="stat-item pending">
          <div class="stat-icon">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.pending }}</div>
            <div class="stat-label">待准备</div>
          </div>
        </div>
        <div class="stat-item preparing">
          <div class="stat-icon">
            <el-icon><Loading /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.preparing }}</div>
            <div class="stat-label">准备中</div>
          </div>
        </div>
        <div class="stat-item ready">
          <div class="stat-icon">
            <el-icon><Check /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.ready }}</div>
            <div class="stat-label">已备好</div>
          </div>
        </div>
        <div class="stat-item delivered">
          <div class="stat-icon">
            <el-icon><Suitcase /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.delivered }}</div>
            <div class="stat-label">已送达</div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <div class="table-title">餐食列表</div>
        <div class="filter-bar">
          <el-select 
            v-model="filterStatus" 
            placeholder="按状态筛选" 
            clearable 
            style="width: 140px"
            @change="loadData"
          >
            <el-option label="待准备" value="待准备" />
            <el-option label="准备中" value="准备中" />
            <el-option label="已备好" value="已备好" />
            <el-option label="已送达" value="已送达" />
          </el-select>
          <el-input 
            v-model="filterFlight" 
            placeholder="按航班号筛选" 
            clearable 
            style="width: 180px"
            @keyup.enter="loadData"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="loadData">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
      </div>

      <el-table :data="mealList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="passengerName" label="旅客姓名" width="110" />
        <el-table-column prop="flightNo" label="航班号" width="100" />
        <el-table-column prop="departureTime" label="起飞时间" width="170" />
        <el-table-column prop="mealType" label="餐食类型" width="100" />
        <el-table-column prop="quantity" label="数量" width="70" />
        <el-table-column prop="specialRequest" label="特殊要求" min-width="140">
          <template #default="{ row }">
            {{ row.specialRequest || '无' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === '待准备'" 
              type="primary" 
              link 
              size="small"
              @click="handleStartPreparing(row)"
            >
              开始准备
            </el-button>
            <el-button 
              v-if="row.status === '准备中'" 
              type="success" 
              link 
              size="small"
              @click="handleMarkReady(row)"
            >
              标记备好
            </el-button>
            <el-button 
              v-if="row.status === '已备好'" 
              type="warning" 
              link 
              size="small"
              @click="handleDeliver(row)"
            >
              送出
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { mealApi } from '@/api'
import { 
  Clock, 
  Loading, 
  Check, 
  Suitcase,
  Search,
  Refresh
} from '@element-plus/icons-vue'

const loading = ref(false)
const mealList = ref([])
const stats = reactive({
  pending: 0,
  preparing: 0,
  ready: 0,
  delivered: 0
})

const filterStatus = ref('')
const filterFlight = ref('')

const getStatusTagType = (status) => {
  const map = {
    '待准备': 'warning',
    '准备中': 'primary',
    '已备好': 'success',
    '已送达': 'info'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const [listRes, statsRes] = await Promise.all([
      mealApi.getList({ 
        status: filterStatus.value, 
        flightNo: filterFlight.value 
      }),
      mealApi.getStatistics()
    ])
    if (listRes.code === 200) {
      mealList.value = listRes.data.list
    }
    if (statsRes.code === 200) {
      Object.assign(stats, statsRes.data)
    }
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  filterStatus.value = ''
  filterFlight.value = ''
  loadData()
}

const handleStartPreparing = async (row) => {
  try {
    const res = await mealApi.startPreparing(row.id)
    if (res.code === 200) {
      ElMessage.success('已开始准备')
      loadData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleMarkReady = async (row) => {
  try {
    const res = await mealApi.markReady(row.id)
    if (res.code === 200) {
      ElMessage.success('已标记备好')
      loadData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDeliver = async (row) => {
  try {
    const res = await mealApi.deliver(row.id)
    if (res.code === 200) {
      ElMessage.success('已送出')
      loadData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.meal-prepare {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stats-card {
  border-radius: 8px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-radius: 8px;
  background: #f5f7fa;
}

.stat-item.pending {
  background: #fdf6ec;
}

.stat-item.pending .stat-icon {
  background: #e6a23c;
}

.stat-item.preparing {
  background: #ecf5ff;
}

.stat-item.preparing .stat-icon {
  background: #409eff;
}

.stat-item.ready {
  background: #f0f9eb;
}

.stat-item.ready .stat-icon {
  background: #67c23a;
}

.stat-item.delivered {
  background: #f4f4f5;
}

.stat-item.delivered .stat-icon {
  background: #909399;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-top: 4px;
}

.table-card {
  border-radius: 8px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.table-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.filter-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}
</style>
