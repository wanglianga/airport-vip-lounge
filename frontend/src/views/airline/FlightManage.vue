<template>
  <div class="flight-manage">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="航班号">
          <el-input 
            v-model="searchForm.flightNo" 
            placeholder="请输入航班号" 
            clearable 
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="航司">
          <el-input 
            v-model="searchForm.airline" 
            placeholder="请输入航司" 
            clearable 
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择" 
            clearable 
            style="width: 140px"
          >
            <el-option label="计划中" value="计划中" />
            <el-option label="延误" value="延误" />
            <el-option label="登机中" value="登机中" />
            <el-option label="已起飞" value="已起飞" />
            <el-option label="取消" value="取消" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <div class="table-title">航班列表</div>
        <div class="table-actions">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增航班
          </el-button>
        </div>
      </div>

      <el-table 
        :data="tableData" 
        style="width: 100%" 
        border 
        v-loading="loading"
      >
        <el-table-column prop="flightNo" label="航班号" width="110" />
        <el-table-column prop="airline" label="航司" width="120" />
        <el-table-column prop="destination" label="目的地" width="140" />
        <el-table-column prop="plannedDeparture" label="计划起飞" width="170" />
        <el-table-column prop="actualDeparture" label="实际起飞" width="170">
          <template #default="{ row }">
            {{ row.actualDeparture || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="gate" label="登机口" width="90" />
        <el-table-column prop="isRemoteStand" label="远机位" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isRemoteStand" type="danger" size="small">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="passengerCount" label="旅客数" width="80" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-dropdown trigger="click" @command="(cmd) => handleStatusChange(row, cmd)">
              <el-button type="success" link size="small">
                更新状态
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="计划中">计划中</el-dropdown-item>
                  <el-dropdown-item command="延误">延误</el-dropdown-item>
                  <el-dropdown-item command="登机中">登机中</el-dropdown-item>
                  <el-dropdown-item command="已起飞">已起飞</el-dropdown-item>
                  <el-dropdown-item command="取消">取消</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="550px"
      @close="handleDialogClose"
    >
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="formRules" 
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="航班号" prop="flightNo">
              <el-input v-model="formData.flightNo" placeholder="请输入航班号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="航司" prop="airline">
              <el-input v-model="formData.airline" placeholder="请输入航司" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="formData.destination" placeholder="请输入目的地" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划起飞" prop="plannedDeparture">
              <el-date-picker
                v-model="formData.plannedDeparture"
                type="datetime"
                placeholder="选择日期时间"
                style="width: 100%"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际起飞">
              <el-date-picker
                v-model="formData.actualDeparture"
                type="datetime"
                placeholder="选择日期时间"
                style="width: 100%"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="登机口" prop="gate">
              <el-input v-model="formData.gate" placeholder="请输入登机口" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="旅客数" prop="passengerCount">
              <el-input-number v-model="formData.passengerCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="远机位">
              <el-switch v-model="formData.isRemoteStand" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="formData.status" style="width: 100%">
                <el-option label="计划中" value="计划中" />
                <el-option label="延误" value="延误" />
                <el-option label="登机中" value="登机中" />
                <el-option label="已起飞" value="已起飞" />
                <el-option label="取消" value="取消" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { flightApi } from '@/api'
import { 
  Search, 
  Refresh, 
  Plus,
  ArrowDown
} from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增航班')
const formRef = ref(null)
const isEdit = ref(false)

const searchForm = reactive({
  flightNo: '',
  airline: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  flightNo: '',
  airline: '',
  destination: '',
  plannedDeparture: '',
  actualDeparture: '',
  gate: '',
  isRemoteStand: false,
  status: '计划中',
  passengerCount: 0
})

const formRules = {
  flightNo: [{ required: true, message: '请输入航班号', trigger: 'blur' }],
  airline: [{ required: true, message: '请输入航司', trigger: 'blur' }],
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  plannedDeparture: [{ required: true, message: '请选择计划起飞时间', trigger: 'change' }],
  gate: [{ required: true, message: '请输入登机口', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getStatusTagType = (status) => {
  const map = {
    '计划中': 'info',
    '延误': 'warning',
    '登机中': 'primary',
    '已起飞': 'success',
    '取消': 'danger'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await flightApi.getList({
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    if (res.code === 200) {
      tableData.value = res.data.list
      pagination.total = res.data.total
    }
  } catch (e) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.flightNo = ''
  searchForm.airline = ''
  searchForm.status = ''
  pagination.page = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增航班'
  Object.assign(formData, {
    id: null,
    flightNo: '',
    airline: '',
    destination: '',
    plannedDeparture: '',
    actualDeparture: '',
    gate: '',
    isRemoteStand: false,
    status: '计划中',
    passengerCount: 0
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑航班'
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const handleStatusChange = async (row, status) => {
  try {
    const res = await flightApi.updateStatus(row.id, status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
      loadData()
    }
  } catch (e) {
    ElMessage.error('更新失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除航班 "${row.flightNo}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await flightApi.delete(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadData()
      }
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    let res
    if (isEdit.value) {
      res = await flightApi.update(formData.id, formData)
    } else {
      res = await flightApi.add(formData)
    }
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadData()
    }
  } catch (e) {
    if (e !== false) {
      ElMessage.error('操作失败')
    }
  }
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.flight-manage {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-card {
  border-radius: 8px;
}

.search-form {
  margin: 0;
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

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
