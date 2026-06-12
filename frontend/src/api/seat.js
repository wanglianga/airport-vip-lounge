import request from '@/utils/request'

const mockSeats = [
  { id: 1, seatNo: 'A-01', area: 'A区', type: '单人沙发', status: '占用', passengerName: '张伟', passengerId: 1 },
  { id: 2, seatNo: 'A-02', area: 'A区', type: '单人沙发', status: '可用', passengerName: null, passengerId: null },
  { id: 3, seatNo: 'A-03', area: 'A区', type: '单人沙发', status: '可用', passengerName: null, passengerId: null },
  { id: 4, seatNo: 'A-04', area: 'A区', type: '双人沙发', status: '维修', passengerName: null, passengerId: null },
  { id: 5, seatNo: 'A-05', area: 'A区', type: '单人沙发', status: '可用', passengerName: null, passengerId: null },
  { id: 6, seatNo: 'A-06', area: 'A区', type: '单人沙发', status: '可用', passengerName: null, passengerId: null },
  { id: 7, seatNo: 'A-07', area: 'A区', type: '双人沙发', status: '可用', passengerName: null, passengerId: null },
  { id: 8, seatNo: 'A-08', area: 'A区', type: '单人沙发', status: '占用', passengerName: '陈静', passengerId: 6 },
  { id: 9, seatNo: 'B-01', area: 'B区', type: '商务位', status: '占用', passengerName: '杨帆', passengerId: 7 },
  { id: 10, seatNo: 'B-02', area: 'B区', type: '商务位', status: '可用', passengerName: null, passengerId: null },
  { id: 11, seatNo: 'B-03', area: 'B区', type: '商务位', status: '可用', passengerName: null, passengerId: null },
  { id: 12, seatNo: 'B-04', area: 'B区', type: '商务位', status: '占用', passengerName: null, passengerId: null },
  { id: 13, seatNo: 'B-05', area: 'B区', type: '商务位', status: '占用', passengerName: '李娜', passengerId: 2 },
  { id: 14, seatNo: 'B-06', area: 'B区', type: '商务位', status: '可用', passengerName: null, passengerId: null },
  { id: 15, seatNo: 'C-01', area: 'C区', type: 'VIP包间', status: '可用', passengerName: null, passengerId: null },
  { id: 16, seatNo: 'C-02', area: 'C区', type: 'VIP包间', status: '占用', passengerName: 'John Smith', passengerId: 4 },
  { id: 17, seatNo: 'C-03', area: 'C区', type: 'VIP包间', status: '可用', passengerName: null, passengerId: null },
  { id: 18, seatNo: 'D-01', area: 'D区', type: '休息区', status: '可用', passengerName: null, passengerId: null },
  { id: 19, seatNo: 'D-02', area: 'D区', type: '休息区', status: '可用', passengerName: null, passengerId: null },
  { id: 20, seatNo: 'D-03', area: 'D区', type: '休息区', status: '占用', passengerName: '赵敏', passengerId: 8 }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockSeats]
      if (params?.area) {
        list = list.filter(s => s.area === params.area)
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
      }, 200)
    })
  },

  getByArea(area) {
    return new Promise((resolve) => {
      const list = mockSeats.filter(s => s.area === area)
      setTimeout(() => {
        resolve({ code: 200, data: list })
      }, 200)
    })
  },

  getStatistics() {
    return new Promise((resolve) => {
      const total = mockSeats.length
      const available = mockSeats.filter(s => s.status === '可用').length
      const occupied = mockSeats.filter(s => s.status === '占用').length
      const maintenance = mockSeats.filter(s => s.status === '维修').length
      setTimeout(() => {
        resolve({
          code: 200,
          data: { total, available, occupied, maintenance }
        })
      }, 200)
    })
  },

  assign(seatId, passengerId) {
    return new Promise((resolve) => {
      const seat = mockSeats.find(s => s.id === seatId)
      if (seat) {
        seat.status = '占用'
        seat.passengerId = passengerId
      }
      setTimeout(() => {
        resolve({ code: 200, message: '分配成功' })
      }, 300)
    })
  },

  release(seatId) {
    return new Promise((resolve) => {
      const seat = mockSeats.find(s => s.id === seatId)
      if (seat) {
        seat.status = '可用'
        seat.passengerId = null
        seat.passengerName = null
      }
      setTimeout(() => {
        resolve({ code: 200, message: '释放成功' })
      }, 300)
    })
  },

  setMaintenance(seatId) {
    return new Promise((resolve) => {
      const seat = mockSeats.find(s => s.id === seatId)
      if (seat) {
        seat.status = '维修'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '设置成功' })
      }, 300)
    })
  }
}
