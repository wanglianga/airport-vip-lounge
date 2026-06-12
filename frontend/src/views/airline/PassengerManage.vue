<template>
  <div class="passenger-manage">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input 
            v-model="searchForm.name" 
            placeholder="请输入姓名" 
            clearable 
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="航班号">
          <el-input 
            v-model="searchForm.flightNo" 
            placeholder="请输入航班号" 
            clearable 
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="等级">
          <el-select 
            v-model="searchForm.level" 
            placeholder="请选择" 
            clearable 
            style="width: 140px"
          >
            <el-option label="白金卡" value="白金卡" />
            <el-option label="金卡" value="金卡" />
            <el-option label="银卡" value="银卡" />
            <el-option label="普卡" value="普卡" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择" 
            clearable 
            style="width: 140px"
          >
            <el-option label="待入住" value="待入住" />
            <el-option label="在室" value="在室" />
            <el-option label="已离港" value="已离港" />
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
        <div class="table-title">旅客列表</div>
        <div class="table-actions">
          <el-button type="success" @click="handleImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增旅客
          </el-button>
        </div>
      </div>

      <el-table 
        :data="tableData" 
        style="width: 100%" 
        border 
        v-loading="loading"
      >
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="idCard" label="身份证号" width="180" />
        <el-table-column prop="level" label="等级" width="90">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.level)" size="small">{{ row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="airline" label="航司" width="100" />
        <el-table-column prop="flightNo" label="航班号" width="100" />
        <el-table-column prop="companionCount" label="同行人数" width="90" />
        <el-table-column prop="hasChild" label="儿童" width="70">
          <template #default="{ row }">
            <el-tag v-if="row.hasChild" type="warning" size="small">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isForeign" label="外宾" width="70">
          <template #default="{ row }">
            <el-tag v-if="row.isForeign" type="info" size="small">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="dietaryRestriction" label="餐食禁忌" width="100">
          <template #default="{ row }">
            {{ row.dietaryRestriction || '无' }}
          </template>
        </el-table-column>
        <el-table-column prop="needRemoteStand" label="远机位" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.needRemoteStand" type="danger" size="small">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
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
      width="600px"
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
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="等级" prop="level">
              <el-select v-model="formData.level" placeholder="请选择" style="width: 100%">
                <el-option label="白金卡" value="白金卡" />
                <el-option label="金卡" value="金卡" />
                <el-option label="银卡" value="银卡" />
                <el-option label="普卡" value="普卡" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="航司" prop="airline">
              <el-input v-model="formData.airline" placeholder="请输入航司" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="航班号" prop="flightNo">
              <el-input v-model="formData.flightNo" placeholder="请输入航班号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同行人数" prop="companionCount">
              <el-input-number v-model="formData.companionCount" :min="0" :max="10" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="儿童">
              <el-switch v-model="formData.hasChild" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="外宾">
              <el-switch v-model="formData.isForeign" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="远机位需求">
              <el-switch v-model="formData.needRemoteStand" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="餐食禁忌">
          <el-input v-model="formData.dietaryRestriction" placeholder="请输入餐食禁忌，无则留空" />
        </el-form-item>
        <el-form-item label="状态" v-if="formData.id">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="待入住" value="待入住" />
            <el-option label="在室" value="在室" />
            <el-option label="已离港" value="已离港" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="importVisible" title="批量导入旅客" width="500px">
      <el-alert 
        title="导入说明" 
        type="info" 
        :closable="false"
        style="margin-bottom: 16px"
      >
        请上传 JSON 格式文件，数组格式，每个对象包含 name, idCard, level, airline, flightNo 等字段
      </el-alert>
      <el-upload
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".json"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            只能上传 JSON 文件
          </div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="importVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm" :disabled="!importFile">
          确认导入
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { passengerApi } from '@/api'
import { 
  Search, 
  Refresh, 
  Plus, 
  Upload, 
  UploadFilled 
} from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增旅客')
const importVisible = ref(false)
const importFile = ref(null)
const formRef = ref(null)
const isEdit = ref(false)

const searchForm = reactive({
  name: '',
  flightNo: '',
  level: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  name: '',
  idCard: '',
  level: '',
  airline: '',
  flightNo: '',
  companionCount: 0,
  hasChild: false,
  isForeign: false,
  dietaryRestriction: '',
  needRemoteStand: false,
  status: '待入住'
})

const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  level: [{ required: true, message: '请选择等级', trigger: 'change' }],
  airline: [{ required: true, message: '请输入航司', trigger: 'blur' }],
  flightNo: [{ required: true, message: '请输入航班号', trigger: 'blur' }]
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

const getStatusTagType = (status) => {
  const map = {
    '待入住': 'warning',
    '在室': 'success',
    '已离港': 'info'
  }
  return map[status] || ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await passengerApi.getList({
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
  searchForm.name = ''
  searchForm.flightNo = ''
  searchForm.level = ''
  searchForm.status = ''
  pagination.page = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增旅客'
  Object.assign(formData, {
    id: null,
    name: '',
    idCard: '',
    level: '',
    airline: '',
    flightNo: '',
    companionCount: 0,
    hasChild: false,
    isForeign: false,
    dietaryRestriction: '',
    needRemoteStand: false,
    status: '待入住'
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑旅客'
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除旅客 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await passengerApi.delete(row.id)
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
      res = await passengerApi.update(formData.id, formData)
    } else {
      res = await passengerApi.add(formData)
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

const handleImport = () => {
  importVisible.value = true
}

const handleFileChange = (file) => {
  importFile.value = file.raw
}

const handleImportConfirm = async () => {
  if (!importFile.value) {
    ElMessage.warning('请选择文件')
    return
  }
  try {
    const text = await importFile.value.text()
    const data = JSON.parse(text)
    if (!Array.isArray(data)) {
      ElMessage.error('文件格式错误，必须是数组')
      return
    }
    const res = await passengerApi.batchImport(data)
    if (res.code === 200) {
      ElMessage.success(res.message)
      importVisible.value = false
      importFile.value = null
      loadData()
    }
  } catch (e) {
    ElMessage.error('文件解析失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.passenger-manage {
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
