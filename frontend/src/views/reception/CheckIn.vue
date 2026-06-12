<template>
  <div class="checkin-page">
    <div class="checkin-left">
      <el-card class="search-card">
        <div class="card-title">旅客搜索</div>
        <el-input 
          v-model="searchKeyword" 
          placeholder="请输入身份证号或姓名" 
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>

        <div v-if="searchResults.length > 0" class="search-results">
          <div class="result-item" 
               v-for="p in searchResults" 
               :key="p.id"
               :class="{ active: selectedPassenger?.id === p.id }"
               @click="selectPassenger(p)"
          >
            <el-avatar :size="40">{{ p.name.charAt(0) }}</el-avatar>
            <div class="result-info">
              <div class="result-name">{{ p.name }}</div>
              <div class="result-detail">{{ p.idCard }} · {{ p.flightNo }}</div>
            </div>
            <el-tag :type="getStatusTagType(p.status)" size="small">
              {{ p.status }}
            </el-tag>
          </div>
        </div>
      </el-card>

      <el-card v-if="selectedPassenger" class="passenger-card">
        <div class="card-title">旅客信息</div>
        <div class="passenger-header">
          <el-avatar :size="64">{{ selectedPassenger.name.charAt(0) }}</el-avatar>
          <div class="passenger-basic">
            <h3 class="passenger-name">{{ selectedPassenger.name }}</h3>
            <div class="passenger-tags">
              <el-tag :type="getLevelTagType(selectedPassenger.level)" size="small">
                {{ selectedPassenger.level }}
              </el-tag>
              <el-tag v-if="selectedPassenger.isForeign" type="info" size="small">外宾</el-tag>
              <el-tag v-if="selectedPassenger.hasChild" type="warning" size="small">带儿童</el-tag>
            </div>
          </div>
        </div>

        <el-descriptions :column="2" border size="small" class="passenger-desc">
          <el-descriptions-item label="身份证号">{{ selectedPassenger.idCard }}</el-descriptions-item>
          <el-descriptions-item label="航司">{{ selectedPassenger.airline }}</el-descriptions-item>
          <el-descriptions-item label="航班号">{{ selectedPassenger.flightNo }}</el-descriptions-item>
          <el-descriptions-item label="同行人数">{{ selectedPassenger.companionCount }} 人</el-descriptions-item>
          <el-descriptions-item label="餐食禁忌">
            {{ selectedPassenger.dietaryRestriction || '无' }}
          </el-descriptions-item>
          <el-descriptions-item label="远机位需求">
            {{ selectedPassenger.needRemoteStand ? '是' : '否' }}
          </el-descriptions-item>
        </el-descriptions>

        <div v-if="selectedPassenger.companionCount > 0" class="companion-section">
          <div class="section-title">同行人信息</div>
          <div class="companion-list">
            <el-tag v-for="i in selectedPassenger.companionCount" :key="i" size="large">
              同行人 {{ i }}
            </el-tag>
          </div>
        </div>

        <div class="card-actions">
          <el-button 
            type="primary" 
            size="large" 
            @click="handleCheckIn"
            :disabled="selectedPassenger.status === '在室'"
          >
            <el-icon><Check /></el-icon>
            {{ selectedPassenger.status === '在室' ? '已入住' : '确认入住' }}
          </el-button>
          <el-button size="large" @click="showSeatSelect = true">
            <el-icon><Grid /></el-icon>
            分配座位
          </el-button>
        </div>
      </el-card>
    </div>

    <div class="checkin-right">
      <el-card class="seat-map-card">
        <div class="card-header">
          <div class="card-title">座位图</div>
          <div class="seat-legend">
            <span class="legend-item">
              <span class="legend-color available"></span>
              可用
            </span>
            <span class="legend-item">
              <span class="legend-color occupied"></span>
              占用
            </span>
            <span class="legend-item">
              <span class="legend-color maintenance"></span>
              维修
            </span>
          </div>
        </div>

        <el-tabs v-model="activeArea" class="seat-tabs">
          <el-tab-pane label="A区" name="A区" />
          <el-tab-pane label="B区" name="B区" />
          <el-tab-pane label="C区" name="C区" />
          <el-tab-pane label="D区" name="D区" />
        </el-tabs>

        <div class="seat-grid">
          <div 
            v-for="seat in currentSeats" 
            :key="seat.id"
            class="seat-item"
            :class="seat.status.toLowerCase()"
            @click="handleSeatClick(seat)"
          >
            <div class="seat-no">{{ seat.seatNo }}</div>
            <div class="seat-type">{{ seat.type }}</div>
            <div v-if="seat.passengerName" class="seat-passenger">
              {{ seat.passengerName }}
            </div>
          </div>
        </div>

        <div class="seat-stats">
          <el-statistic title="总计" :value="seatStats.total" />
          <el-statistic title="可用" :value="seatStats.available" />
          <el-statistic title="占用" :value="seatStats.occupied" />
          <el-statistic title="维修" :value="seatStats.maintenance" />
        </div>
      </el-card>
    </div>

    <el-dialog v-model="seatSelectDialog" title="选择座位" width="500px">
      <el-select v-model="selectedSeatId" placeholder="请选择座位" style="width: 100%">
        <el-option 
          v-for="seat in availableSeats" 
          :key="seat.id" 
          :label="`${seat.seatNo} - ${seat.type}`" 
          :value="seat.id"
        />
      </el-select>
      <template #footer>
        <el-button @click="seatSelectDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmSeatAssign">确认分配</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { passengerApi, seatApi } from '@/api'
import { 
  Search, 
  Check, 
  Grid 
} from '@element-plus/icons-vue'

const searchKeyword = ref('')
const searchResults = ref([])
const selectedPassenger = ref(null)
const showSeatSelect = ref(false)
const seatSelectDialog = ref(false)
const selectedSeatId = ref(null)
const activeArea = ref('A区')
const seatList = ref([])
const seatStats = ref({
  total: 0,
  available: 0,
  occupied: 0,
  maintenance: 0
})

const currentSeats = computed(() => {
  return seatList.value.filter(s => s.area === activeArea.value)
})

const availableSeats = computed(() => {
  return seatList.value.filter(s => s.status === '可用')
})

const getStatusTagType = (status) => {
  const map = {
    '待入住': 'warning',
    '在室': 'success',
    '已离港': 'info'
  }
  return map[status] || ''
}

const getLevelTagType = (level) => {
  const map = {
    '白金卡': 'warning',
    '金卡': 'success',
    '银卡': 'info',
    '普卡': ''
  }
  return map[level] || ''
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    searchResults.value = []
    return
  }
  try {
    const res = await passengerApi.search(searchKeyword.value)
    if (res.code === 200) {
      searchResults.value = res.data
    }
  } catch (e) {
    ElMessage.error('搜索失败')
  }
}

const selectPassenger = (passenger) => {
  selectedPassenger.value = passenger
}

const loadSeats = async () => {
  try {
    const [listRes, statsRes] = await Promise.all([
      seatApi.getList(),
      seatApi.getStatistics()
    ])
    if (listRes.code === 200) {
      seatList.value = listRes.data.list
    }
    if (statsRes.code === 200) {
      seatStats.value = statsRes.data
    }
  } catch (e) {
    console.error('加载座位数据失败')
  }
}

const handleSeatClick = (seat) => {
  if (seat.status === '维修') {
    ElMessage.warning('该座位正在维修')
    return
  }
  if (seat.status === '占用') {
    ElMessage.info(`该座位已被 ${seat.passengerName} 占用`)
    return
  }
  if (!selectedPassenger.value) {
    ElMessage.warning('请先选择旅客')
    return
  }
  selectedSeatId.value = seat.id
  seatSelectDialog.value = true
}

const handleCheckIn = () => {
  if (!selectedPassenger.value) return
  seatSelectDialog.value = true
}

const confirmSeatAssign = async () => {
  if (!selectedPassenger.value || !selectedSeatId.value) return
  try {
    const seat = seatList.value.find(s => s.id === selectedSeatId.value)
    const res = await passengerApi.checkIn(selectedPassenger.value.id, seat?.seatNo)
    if (res.code === 200) {
      ElMessage.success('入住成功')
      seatSelectDialog.value = false
      showSeatSelect.value = false
      loadSeats()
      handleSearch()
      if (selectedPassenger.value) {
        selectedPassenger.value.status = '在室'
        selectedPassenger.value.seatNo = seat?.seatNo
      }
    }
  } catch (e) {
    ElMessage.error('入住失败')
  }
}

onMounted(() => {
  loadSeats()
})
</script>

<style scoped>
.checkin-page {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 16px;
  height: calc(100vh - 100px);
}

.checkin-left {
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow-y: auto;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 16px;
}

.search-results {
  margin-top: 16px;
  max-height: 300px;
  overflow-y: auto;
}

.result-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
  margin-bottom: 8px;
}

.result-item:hover {
  background: #f5f7fa;
}

.result-item.active {
  background: #ecf5ff;
  border: 1px solid #409eff;
}

.result-info {
  flex: 1;
  min-width: 0;
}

.result-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 2px;
}

.result-detail {
  font-size: 12px;
  color: #909399;
}

.passenger-card {
  flex: 1;
}

.passenger-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.passenger-basic {
  flex: 1;
}

.passenger-name {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.passenger-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.passenger-desc {
  margin-bottom: 16px;
}

.companion-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

.section-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 12px;
  color: #606266;
}

.companion-list {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.card-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.card-actions .el-button {
  flex: 1;
}

.checkin-right {
  height: 100%;
}

.seat-map-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-header .card-title {
  margin: 0;
}

.seat-legend {
  display: flex;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #606266;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 3px;
}

.legend-color.available {
  background: #67c23a;
}

.legend-color.occupied {
  background: #f56c6c;
}

.legend-color.maintenance {
  background: #909399;
}

.seat-tabs {
  margin-bottom: 16px;
}

.seat-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  align-content: start;
  overflow-y: auto;
  padding: 8px;
}

.seat-item {
  padding: 12px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.seat-item.available {
  background: #f0f9eb;
  border-color: #67c23a;
}

.seat-item.available:hover {
  background: #e1f3d8;
  transform: translateY(-2px);
}

.seat-item.占用 {
  background: #fef0f0;
  border-color: #f56c6c;
  cursor: not-allowed;
}

.seat-item.维修 {
  background: #f4f4f5;
  border-color: #909399;
  cursor: not-allowed;
  opacity: 0.6;
}

.seat-no {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.seat-type {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.seat-passenger {
  font-size: 12px;
  color: #f56c6c;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.seat-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 16px;
  margin-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>
