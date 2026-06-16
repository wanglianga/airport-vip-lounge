<template>
  <div class="flight-change-manage">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="航班号">
          <el-input v-model="searchForm.flightNo" placeholder="请输入航班号" clearable />
        </el-form-item>
        <el-form-item label="变更类型">
          <el-select v-model="searchForm.changeType" placeholder="请选择" clearable>
            <el-option label="状态变更" value="STATUS" />
            <el-option label="登机口变更" value="GATE" />
            <el-option label="远机位调整" value="FAR_STAND" />
            <el-option label="延误/时间调整" value="DELAY" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">航班变更记录</span>
          <el-button type="primary" @click="handleAdd">发起变更</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="flightNo" label="航班号" width="120" />
        <el-table-column prop="changeType" label="变更类型" width="140">
          <template #default="{ row }">
            <el-tag :type="getChangeTypeTag(row.changeType)">{{ getChangeTypeText(row.changeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="变更内容" min-width="200">
          <template #default="{ row }">
            <div class="change-content">
              <span v-if="row.oldStatus || row.newStatus">
                状态: {{ row.oldStatus }} → {{ row.newStatus }}
              </span>
              <span v-if="row.oldGate || row.newGate">
                登机口: {{ row.oldGate }} → {{ row.newGate }}
              </span>
              <span v-if="row.oldFarStand !== row.newFarStand">
                远机位: {{ row.oldFarStand ? '是' : '否' }} → {{ row.newFarStand ? '是' : '否' }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="notifiedByName" label="通知人" width="120" />
        <el-table-column prop="createTime" label="通知时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <el-dialog v-model="addDialogVisible" title="发起航班变更" width="600px">
      <el-form :model="changeForm" :rules="changeRules" ref="changeFormRef" label-width="100px">
        <el-form-item label="选择航班" prop="flightId">
          <el-select v-model="changeForm.flightId" placeholder="请选择航班" filterable @change="handleFlightSelect">
            <el-option v-for="flight in flightOptions" :key="flight.id" :label="`${flight.flightNo} - ${flight.destination}`" :value="flight.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="变更类型" prop="changeType">
          <el-select v-model="changeForm.changeType" placeholder="请选择变更类型" @change="handleChangeType">
            <el-option label="航班延误" value="DELAY" />
            <el-option label="航班取消" value="STATUS" />
            <el-option label="登机口变更" value="GATE" />
            <el-option label="远机位调整" value="FAR_STAND" />
          </el-select>
        </el-form-item>

        <template v-if="changeForm.changeType === 'DELAY'">
          <el-form-item label="新起飞时间" prop="newScheduledDeparture">
            <el-date-picker
              v-model="changeForm.newScheduledDeparture"
              type="datetime"
              placeholder="选择新的起飞时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DDTHH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </template>

        <template v-if="changeForm.changeType === 'STATUS'">
          <el-form-item label="新状态" prop="newStatus">
            <el-select v-model="changeForm.newStatus" placeholder="请选择新状态">
              <el-option label="取消" value="CANCELLED" />
              <el-option label="延误" value="DELAYED" />
              <el-option label="登机中" value="BOARDING" />
              <el-option label="已起飞" value="DEPARTED" />
            </el-select>
          </el-form-item>
        </template>

        <template v-if="changeForm.changeType === 'GATE'">
          <el-form-item label="新登机口" prop="newGate">
            <el-input v-model="changeForm.newGate" placeholder="请输入新登机口" />
          </el-form-item>
        </template>

        <template v-if="changeForm.changeType === 'FAR_STAND'">
          <el-form-item label="是否远机位" prop="newFarStand">
            <el-switch v-model="changeForm.newFarStand" />
          </el-form-item>
        </template>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="changeForm.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="航班变更详情" width="900px">
      <el-steps :active="currentStep" finish-status="success" align-center class="timeline-steps">
        <el-step title="航司通知" description="航班状态同步" />
        <el-step title="服务重算" description="座位/餐食/淋浴/会议室" />
        <el-step title="旅客确认" description="前台逐个确认" />
        <el-step title="车队更新" description="送机任务调整" />
      </el-steps>

      <el-tabs v-model="activeTab" class="detail-tabs">
        <el-tab-pane label="变更信息" name="change">
          <el-descriptions :column="2" border v-if="flightChangeDetail.flightChange">
            <el-descriptions-item label="航班号">{{ flightChangeDetail.flightChange.flightNo }}</el-descriptions-item>
            <el-descriptions-item label="变更类型">{{ getChangeTypeText(flightChangeDetail.flightChange.changeType) }}</el-descriptions-item>
            <el-descriptions-item label="通知人">{{ flightChangeDetail.flightChange.notifiedByName }}</el-descriptions-item>
            <el-descriptions-item label="通知时间">{{ flightChangeDetail.flightChange.createTime }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ flightChangeDetail.flightChange.remark || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <el-tab-pane label="服务调整" name="adjustment">
          <div class="adjustment-summary">
            <el-statistic title="座位调整" :value="getAdjustmentCount('SEAT')" />
            <el-statistic title="餐食调整" :value="getAdjustmentCount('MEAL')" />
            <el-statistic title="淋浴调整" :value="getAdjustmentCount('SHOWER')" />
            <el-statistic title="会议室调整" :value="getAdjustmentCount('MEETING')" />
          </div>
          <el-table :data="flightChangeDetail.adjustments || []" size="small" stripe>
            <el-table-column prop="passengerName" label="旅客姓名" width="100" />
            <el-table-column prop="serviceType" label="服务类型" width="100" />
            <el-table-column prop="adjustmentType" label="调整类型" width="120" />
            <el-table-column prop="oldStatus" label="原状态" width="100" />
            <el-table-column prop="newStatus" label="新状态" width="100" />
            <el-table-column prop="remark" label="调整说明" min-width="200" />
            <el-table-column prop="createTime" label="调整时间" width="160" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="旅客确认" name="confirmation">
          <div class="confirmation-summary">
            <el-statistic title="待确认" :value="flightChangeDetail.summary?.pending || 0" />
            <el-statistic title="已确认" :value="flightChangeDetail.summary?.confirmed || 0" />
            <el-statistic title="继续休息" :value="flightChangeDetail.summary?.continue || 0" />
            <el-statistic title="改签离开" :value="flightChangeDetail.summary?.rebook || 0" />
            <el-statistic title="需要打包餐" :value="flightChangeDetail.summary?.packedMeal || 0" />
          </div>
          <el-table :data="flightChangeDetail.confirmations || []" size="small" stripe>
            <el-table-column prop="passengerName" label="旅客姓名" width="100" />
            <el-table-column prop="passengerPhone" label="联系电话" width="130" />
            <el-table-column prop="confirmStatus" label="确认状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.confirmStatus === 'CONFIRMED' ? 'success' : 'warning'">
                  {{ row.confirmStatus === 'CONFIRMED' ? '已确认' : '待确认' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="passengerChoice" label="旅客选择" width="100">
              <template #default="{ row }">
                <span v-if="row.passengerChoice === 'CONTINUE'">继续休息</span>
                <span v-else-if="row.passengerChoice === 'REBOOK'">改签离开</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="needPackedMeal" label="打包餐食" width="90">
              <template #default="{ row }">
                <el-tag v-if="row.needPackedMeal" type="danger">是</el-tag>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column prop="confirmedByName" label="确认人" width="100" />
            <el-table-column prop="confirmTime" label="确认时间" width="160" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button
                  v-if="row.confirmStatus === 'PENDING'"
                  type="primary"
                  link
                  size="small"
                  @click="handleConfirm(row)"
                >确认</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="车辆任务" name="vehicle">
          <el-button type="primary" size="small" @click="handleRecalculateVehicle">重新计算车辆任务</el-button>
          <el-table :data="vehicleTasks" size="small" stripe style="margin-top: 10px">
            <el-table-column prop="plateNo" label="车牌号" width="100" />
            <el-table-column prop="passengerName" label="旅客姓名" width="100" />
            <el-table-column prop="taskType" label="任务类型" width="100" />
            <el-table-column prop="dropoffLocation" label="送达地点" width="150" />
            <el-table-column prop="scheduledPickupTime" label="计划接人时间" width="160" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTag(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="taskSource" label="任务来源" width="120" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <el-dialog v-model="confirmDialogVisible" title="确认旅客选择" width="500px">
      <el-form :model="confirmForm" label-width="100px">
        <el-form-item label="旅客姓名">
          <span>{{ currentConfirmation?.passengerName }}</span>
        </el-form-item>
        <el-form-item label="旅客选择" prop="passengerChoice">
          <el-radio-group v-model="confirmForm.passengerChoice">
            <el-radio value="CONTINUE">继续休息</el-radio>
            <el-radio value="REBOOK">改签离开</el-radio>
            <el-radio value="OTHER">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="confirmForm.passengerChoice === 'REBOOK'" label="改签航班">
          <el-input v-model="confirmForm.newFlightNo" placeholder="请输入改签航班号" />
        </el-form-item>
        <el-form-item label="打包餐食">
          <el-switch v-model="confirmForm.needPackedMeal" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="confirmForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="confirmDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import flightChangeApi from '@/api/flightChange'
import passengerConfirmationApi from '@/api/passengerConfirmation'
import serviceAdjustmentApi from '@/api/serviceAdjustment'
import vehicleTaskApi from '@/api/vehicleTask'
import flightApi from '@/api/flight'

const loading = ref(false)
const tableData = ref([])
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const searchForm = reactive({
  flightNo: '',
  changeType: ''
})

const addDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const confirmDialogVisible = ref(false)

const changeForm = reactive({
  flightId: null,
  changeType: '',
  newStatus: '',
  newGate: '',
  newFarStand: false,
  newScheduledDeparture: '',
  remark: ''
})

const changeRules = {
  flightId: [{ required: true, message: '请选择航班', trigger: 'change' }],
  changeType: [{ required: true, message: '请选择变更类型', trigger: 'change' }]
}

const flightOptions = ref([])
const currentFlight = ref(null)
const flightChangeDetail = ref({})
const activeTab = ref('change')
const currentStep = ref(0)
const currentConfirmation = ref(null)
const confirmForm = reactive({
  passengerChoice: '',
  newFlightNo: '',
  needPackedMeal: false,
  remark: ''
})

const vehicleTasks = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const res = await flightChangeApi.getList({
      current: pagination.current,
      size: pagination.size,
      flightNo: searchForm.flightNo,
      changeType: searchForm.changeType
    })
    if (res.code === 200) {
      tableData.value = res.data.records
      pagination.total = res.data.total
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadFlightOptions = async () => {
  try {
    const res = await flightApi.getList({ current: 1, size: 100 })
    if (res.code === 200) {
      flightOptions.value = res.data.records || []
    }
  } catch (e) {
    console.error(e)
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  searchForm.flightNo = ''
  searchForm.changeType = ''
  handleSearch()
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadData()
}

const handleAdd = () => {
  changeForm.flightId = null
  changeForm.changeType = ''
  changeForm.newStatus = ''
  changeForm.newGate = ''
  changeForm.newFarStand = false
  changeForm.newScheduledDeparture = ''
  changeForm.remark = ''
  currentFlight.value = null
  addDialogVisible.value = true
  loadFlightOptions()
}

const handleFlightSelect = (flightId) => {
  const flight = flightOptions.value.find(f => f.id === flightId)
  currentFlight.value = flight
}

const handleChangeType = () => {
}

const handleSubmit = async () => {
  try {
    const data = {
      flightId: changeForm.flightId,
      changeType: changeForm.changeType,
      remark: changeForm.remark,
      notifiedBy: 'AIRLINE',
      notifiedByName: '航司系统'
    }

    if (changeForm.changeType === 'DELAY') {
      data.newScheduledDeparture = changeForm.newScheduledDeparture
    } else if (changeForm.changeType === 'STATUS') {
      data.newStatus = changeForm.newStatus
    } else if (changeForm.changeType === 'GATE') {
      data.newGate = changeForm.newGate
    } else if (changeForm.changeType === 'FAR_STAND') {
      data.newFarStand = changeForm.newFarStand
    }

    const res = await flightChangeApi.create(data)
    if (res.code === 200) {
      ElMessage.success('航班变更已提交')
      addDialogVisible.value = false
      loadData()
    }
  } catch (e) {
    ElMessage.error('提交失败')
    console.error(e)
  }
}

const handleView = async (row) => {
  try {
    const res = await flightChangeApi.getById(row.id)
    if (res.code === 200) {
      flightChangeDetail.value = res.data
      detailDialogVisible.value = true
      activeTab.value = 'change'

      const confirmed = res.data.summary?.confirmed || 0
      const total = res.data.summary?.total || 1
      if (confirmed > 0) {
        currentStep.value = 2
      } else {
        currentStep.value = 1
      }

      loadVehicleTasks(row.id)
    }
  } catch (e) {
    console.error(e)
  }
}

const loadVehicleTasks = async (flightChangeId) => {
  try {
    const res = await vehicleTaskApi.getByFlightChangeId(flightChangeId)
    if (res.code === 200) {
      vehicleTasks.value = res.data
    }
  } catch (e) {
    console.error(e)
  }
}

const handleConfirm = (row) => {
  currentConfirmation.value = row
  confirmForm.passengerChoice = ''
  confirmForm.newFlightNo = ''
  confirmForm.needPackedMeal = false
  confirmForm.remark = ''
  confirmDialogVisible.value = true
}

const handleConfirmSubmit = async () => {
  try {
    if (!confirmForm.passengerChoice) {
      ElMessage.warning('请选择旅客选择')
      return
    }

    const data = {
      passengerChoice: confirmForm.passengerChoice,
      newFlightNo: confirmForm.newFlightNo,
      needPackedMeal: confirmForm.needPackedMeal,
      remark: confirmForm.remark,
      confirmedBy: 'RECEPTION',
      confirmedByName: '前台接待'
    }

    const res = await passengerConfirmationApi.confirm(currentConfirmation.value.id, data)
    if (res.code === 200) {
      ElMessage.success('确认成功')
      confirmDialogVisible.value = false
      handleView(flightChangeDetail.value.flightChange)
    }
  } catch (e) {
    ElMessage.error('确认失败')
    console.error(e)
  }
}

const handleRecalculateVehicle = async () => {
  try {
    const res = await vehicleTaskApi.recalculate(flightChangeDetail.value.flightChange.id)
    if (res.code === 200) {
      ElMessage.success('车辆任务已重新计算')
      loadVehicleTasks(flightChangeDetail.value.flightChange.id)
    }
  } catch (e) {
    ElMessage.error('计算失败')
    console.error(e)
  }
}

const getChangeTypeText = (type) => {
  const map = {
    'STATUS': '状态变更',
    'GATE': '登机口变更',
    'FAR_STAND': '远机位调整',
    'DELAY': '延误调整'
  }
  return map[type] || type
}

const getChangeTypeTag = (type) => {
  const map = {
    'STATUS': 'danger',
    'GATE': 'warning',
    'FAR_STAND': 'info',
    'DELAY': 'warning'
  }
  return map[type] || ''
}

const getAdjustmentCount = (type) => {
  const adjustments = flightChangeDetail.value.adjustments || []
  return adjustments.filter(a => a.serviceType === type).length
}

const getStatusTag = (status) => {
  const map = {
    'PENDING': 'info',
    'ASSIGNED': 'warning',
    'IN_PROGRESS': 'primary',
    'COMPLETED': 'success',
    'CANCELLED': 'danger'
  }
  return map[status] || ''
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待派单',
    'ASSIGNED': '已派单',
    'IN_PROGRESS': '执行中',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return map[status] || status
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.flight-change-manage {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-card {
  padding: 16px;
}

.table-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
}

.pagination {
  margin-top: 16px;
  justify-content: flex-end;
}

.change-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 13px;
}

.detail-tabs {
  margin-top: 20px;
}

.adjustment-summary,
.confirmation-summary {
  display: flex;
  gap: 30px;
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.timeline-steps {
  margin-bottom: 20px;
}
</style>
