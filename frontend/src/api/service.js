import request from '@/utils/request'

const mockServices = [
  { id: 1, type: '座位服务', passengerName: '张伟', status: '进行中', startTime: '08:30', operator: '前台小王' },
  { id: 2, type: '淋浴服务', passengerName: '李娜', status: '已完成', startTime: '09:00', endTime: '09:30', operator: '保洁张姐' },
  { id: 3, type: '餐食服务', passengerName: 'John Smith', status: '进行中', startTime: '10:15', operator: '餐饮部小李' },
  { id: 4, type: '会议室服务', passengerName: '杨帆', status: '进行中', startTime: '10:00', operator: '前台小张' },
  { id: 5, type: '接送服务', passengerName: '赵敏', status: '待服务', startTime: null, operator: null },
  { id: 6, type: '行李服务', passengerName: '陈静', status: '已完成', startTime: '11:00', endTime: '11:15', operator: '行李员老刘' }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockServices]
      if (params?.type) {
        list = list.filter(s => s.type === params.type)
      }
      if (params?.status) {
        list = list.filter(s => s.status === params.status)
      }
      setTimeout(() => {
        resolve({
          code: 200,
          data: {
            list,
            total: list.length
          }
        })
      }, 300)
    })
  },

  createService(passengerId, type, note) {
    return new Promise((resolve) => {
      const newService = {
        id: Date.now(),
        type,
        passengerId,
        status: '待服务',
        startTime: null,
        operator: null,
        note
      }
      mockServices.unshift(newService)
      setTimeout(() => {
        resolve({ code: 200, message: '服务创建成功' })
      }, 300)
    })
  },

  startService(serviceId, operator) {
    return new Promise((resolve) => {
      const service = mockServices.find(s => s.id === serviceId)
      if (service) {
        service.status = '进行中'
        service.startTime = new Date().toLocaleTimeString()
        service.operator = operator
      }
      setTimeout(() => {
        resolve({ code: 200, message: '服务已开始' })
      }, 300)
    })
  },

  completeService(serviceId) {
    return new Promise((resolve) => {
      const service = mockServices.find(s => s.id === serviceId)
      if (service) {
        service.status = '已完成'
        service.endTime = new Date().toLocaleTimeString()
      }
      setTimeout(() => {
        resolve({ code: 200, message: '服务已完成' })
      }, 300)
    })
  }
}
