import request from '@/utils/request'

const mockPassengers = [
  { id: 1, name: '张伟', idCard: '110101199001011234', level: '白金卡', airline: '中国国航', flightNo: 'CA1234', companionCount: 1, hasChild: false, isForeign: false, dietaryRestriction: '无', needRemoteStand: false, status: '在室', seatNo: 'A-01', checkInTime: '2024-01-15 08:30:00' },
  { id: 2, name: '李娜', idCard: '310101199203045678', level: '金卡', airline: '东方航空', flightNo: 'MU5678', companionCount: 2, hasChild: true, isForeign: false, dietaryRestriction: '素食', needRemoteStand: true, status: '在室', seatNo: 'B-05', checkInTime: '2024-01-15 09:15:00' },
  { id: 3, name: '王强', idCard: '440101198505069012', level: '银卡', airline: '南方航空', flightNo: 'CZ9012', companionCount: 0, hasChild: false, isForeign: false, dietaryRestriction: '无', needRemoteStand: false, status: '已离港', seatNo: null, checkInTime: '2024-01-15 07:00:00' },
  { id: 4, name: 'John Smith', idCard: 'AB123456', level: '白金卡', airline: '国泰航空', flightNo: 'CX3456', companionCount: 1, hasChild: false, isForeign: true, dietaryRestriction: '清真', needRemoteStand: false, status: '在室', seatNo: 'C-02', checkInTime: '2024-01-15 10:00:00' },
  { id: 5, name: '刘洋', idCard: '510101199108083456', level: '金卡', airline: '海南航空', flightNo: 'HU7890', companionCount: 3, hasChild: false, isForeign: false, dietaryRestriction: '无', needRemoteStand: true, status: '待入住', seatNo: null, checkInTime: null },
  { id: 6, name: '陈静', idCard: '330101199412127890', level: '普卡', airline: '深圳航空', flightNo: 'ZH2345', companionCount: 0, hasChild: false, isForeign: false, dietaryRestriction: '海鲜过敏', needRemoteStand: false, status: '在室', seatNo: 'A-08', checkInTime: '2024-01-15 11:30:00' },
  { id: 7, name: '杨帆', idCard: '210101198707071234', level: '白金卡', airline: '中国国航', flightNo: 'CA5678', companionCount: 1, hasChild: true, isForeign: false, dietaryRestriction: '无', needRemoteStand: false, status: '在室', seatNo: 'B-01', checkInTime: '2024-01-15 09:45:00' },
  { id: 8, name: '赵敏', idCard: '610101199606065678', level: '金卡', airline: '东方航空', flightNo: 'MU1234', companionCount: 0, hasChild: false, isForeign: false, dietaryRestriction: '无', needRemoteStand: true, status: '在室', seatNo: 'D-03', checkInTime: '2024-01-15 10:30:00' }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockPassengers]
      if (params?.name) {
        list = list.filter(p => p.name.includes(params.name))
      }
      if (params?.flightNo) {
        list = list.filter(p => p.flightNo.includes(params.flightNo))
      }
      if (params?.level) {
        list = list.filter(p => p.level === params.level)
      }
      if (params?.status) {
        list = list.filter(p => p.status === params.status)
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

  getById(id) {
    return new Promise((resolve) => {
      const passenger = mockPassengers.find(p => p.id === id)
      setTimeout(() => {
        resolve({ code: 200, data: passenger })
      }, 200)
    })
  },

  search(keyword) {
    return new Promise((resolve) => {
      let list = mockPassengers.filter(p => 
        p.name.includes(keyword) || p.idCard.includes(keyword)
      )
      setTimeout(() => {
        resolve({ code: 200, data: list })
      }, 200)
    })
  },

  add(data) {
    return new Promise((resolve) => {
      const newPassenger = { ...data, id: Date.now() }
      mockPassengers.push(newPassenger)
      setTimeout(() => {
        resolve({ code: 200, message: '添加成功' })
      }, 300)
    })
  },

  update(id, data) {
    return new Promise((resolve) => {
      const index = mockPassengers.findIndex(p => p.id === id)
      if (index > -1) {
        mockPassengers[index] = { ...mockPassengers[index], ...data }
      }
      setTimeout(() => {
        resolve({ code: 200, message: '更新成功' })
      }, 300)
    })
  },

  delete(id) {
    return new Promise((resolve) => {
      const index = mockPassengers.findIndex(p => p.id === id)
      if (index > -1) {
        mockPassengers.splice(index, 1)
      }
      setTimeout(() => {
        resolve({ code: 200, message: '删除成功' })
      }, 300)
    })
  },

  batchImport(list) {
    return new Promise((resolve) => {
      list.forEach((item, index) => {
        mockPassengers.push({ ...item, id: Date.now() + index })
      })
      setTimeout(() => {
        resolve({ code: 200, message: `成功导入 ${list.length} 条数据` })
      }, 500)
    })
  },

  checkIn(id, seatNo) {
    return new Promise((resolve) => {
      const passenger = mockPassengers.find(p => p.id === id)
      if (passenger) {
        passenger.status = '在室'
        passenger.seatNo = seatNo
        passenger.checkInTime = new Date().toLocaleString()
      }
      setTimeout(() => {
        resolve({ code: 200, message: '入住成功' })
      }, 300)
    })
  }
}
