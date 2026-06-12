import request from '@/utils/request'

const mockShowers = [
  { id: 1, roomNo: 'S-01', status: '可用', passengerName: null, startTime: null, duration: null },
  { id: 2, roomNo: 'S-02', status: '使用中', passengerName: '张伟', startTime: '10:30', duration: 30 },
  { id: 3, roomNo: 'S-03', status: '可用', passengerName: null, startTime: null, duration: null },
  { id: 4, roomNo: 'S-04', status: '清洁中', passengerName: null, startTime: null, duration: null },
  { id: 5, roomNo: 'S-05', status: '可用', passengerName: null, startTime: null, duration: null },
  { id: 6, roomNo: 'S-06', status: '维修', passengerName: null, startTime: null, duration: null }
]

export default {
  getList() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ code: 200, data: [...mockShowers] })
      }, 200)
    })
  },

  getStatistics() {
    return new Promise((resolve) => {
      const total = mockShowers.length
      const available = mockShowers.filter(s => s.status === '可用').length
      const inUse = mockShowers.filter(s => s.status === '使用中').length
      const cleaning = mockShowers.filter(s => s.status === '清洁中').length
      const maintenance = mockShowers.filter(s => s.status === '维修').length
      setTimeout(() => {
        resolve({
          code: 200,
          data: { total, available, inUse, cleaning, maintenance }
        })
      }, 200)
    })
  },

  assign(roomId, passengerId, duration) {
    return new Promise((resolve) => {
      const room = mockShowers.find(s => s.id === roomId)
      if (room) {
        room.status = '使用中'
        room.startTime = new Date().toLocaleTimeString()
        room.duration = duration
      }
      setTimeout(() => {
        resolve({ code: 200, message: '分配成功' })
      }, 300)
    })
  },

  finish(roomId) {
    return new Promise((resolve) => {
      const room = mockShowers.find(s => s.id === roomId)
      if (room) {
        room.status = '清洁中'
        room.passengerName = null
        room.startTime = null
        room.duration = null
      }
      setTimeout(() => {
        resolve({ code: 200, message: '已完成' })
      }, 300)
    })
  }
}
