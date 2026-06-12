import request from '@/utils/request'

const mockVehicles = [
  { id: 1, plateNo: '京A12345', model: '别克GL8', driver: '张师傅', status: '空闲', currentTask: null, location: '停车场A区' },
  { id: 2, plateNo: '京B67890', model: '奔驰威霆', driver: '李师傅', status: '任务中', currentTask: '接送CA1234航班旅客', location: 'T2航站楼' },
  { id: 3, plateNo: '京C11111', model: '丰田考斯特', driver: '王师傅', status: '空闲', currentTask: null, location: '停车场B区' },
  { id: 4, plateNo: '京D22222', model: '别克GL8', driver: '赵师傅', status: '维护中', currentTask: null, location: '维修站' },
  { id: 5, plateNo: '京E33333', model: '奔驰V级', driver: '刘师傅', status: '任务中', currentTask: '接送MU5678航班旅客', location: '远机位' },
  { id: 6, plateNo: '京F44444', model: '别克GL8', driver: '陈师傅', status: '返回中', currentTask: '送机返程', location: '高速路上' }
]

const mockTasks = [
  { id: 1, passengerName: '周杰', flightNo: 'CA1234', gate: 'A12', isRemoteStand: false, deadline: '2024-01-15 13:30:00', status: '待派单' },
  { id: 2, passengerName: '吴芳', flightNo: 'CZ9012', gate: 'C15', isRemoteStand: true, deadline: '2024-01-15 10:00:00', status: '已完成' },
  { id: 3, passengerName: '郑明', flightNo: 'HU7890', gate: 'A05', isRemoteStand: true, deadline: '2024-01-15 10:50:00', status: '进行中' },
  { id: 4, passengerName: '孙丽', flightNo: 'CX3456', gate: 'D20', isRemoteStand: false, deadline: '2024-01-15 16:00:00', status: '待派单' }
]

export default {
  getVehicleList() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ code: 200, data: [...mockVehicles] })
      }, 200)
    })
  },

  getTaskList() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ code: 200, data: [...mockTasks] })
      }, 200)
    })
  },

  getStatistics() {
    return new Promise((resolve) => {
      const total = mockVehicles.length
      const idle = mockVehicles.filter(v => v.status === '空闲').length
      const busy = mockVehicles.filter(v => v.status === '任务中' || v.status === '返回中').length
      const maintenance = mockVehicles.filter(v => v.status === '维护中').length
      const pendingTasks = mockTasks.filter(t => t.status === '待派单').length
      setTimeout(() => {
        resolve({
          code: 200,
          data: { total, idle, busy, maintenance, pendingTasks }
        })
      }, 200)
    })
  },

  assignTask(vehicleId, taskId) {
    return new Promise((resolve) => {
      const vehicle = mockVehicles.find(v => v.id === vehicleId)
      const task = mockTasks.find(t => t.id === taskId)
      if (vehicle && task) {
        vehicle.status = '任务中'
        vehicle.currentTask = `接送${task.flightNo}航班旅客`
        task.status = '进行中'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '派单成功' })
      }, 300)
    })
  },

  updateTaskStatus(taskId, status) {
    return new Promise((resolve) => {
      const task = mockTasks.find(t => t.id === taskId)
      if (task) {
        task.status = status
      }
      setTimeout(() => {
        resolve({ code: 200, message: '状态更新成功' })
      }, 300)
    })
  },

  updateVehicleStatus(vehicleId, status, location) {
    return new Promise((resolve) => {
      const vehicle = mockVehicles.find(v => v.id === vehicleId)
      if (vehicle) {
        vehicle.status = status
        if (location) {
          vehicle.location = location
        }
        if (status === '空闲') {
          vehicle.currentTask = null
        }
      }
      setTimeout(() => {
        resolve({ code: 200, message: '状态更新成功' })
      }, 300)
    })
  }
}
