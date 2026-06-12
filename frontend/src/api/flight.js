import request from '@/utils/request'

const mockFlights = [
  { id: 1, flightNo: 'CA1234', airline: '中国国航', destination: '上海虹桥', plannedDeparture: '2024-01-15 14:30:00', actualDeparture: null, gate: 'A12', isRemoteStand: false, status: '计划中', passengerCount: 156 },
  { id: 2, flightNo: 'MU5678', airline: '东方航空', destination: '广州白云', plannedDeparture: '2024-01-15 15:00:00', actualDeparture: null, gate: 'B08', isRemoteStand: true, status: '延误', passengerCount: 203 },
  { id: 3, flightNo: 'CZ9012', airline: '南方航空', destination: '深圳宝安', plannedDeparture: '2024-01-15 10:30:00', actualDeparture: '2024-01-15 10:35:00', gate: 'C15', isRemoteStand: false, status: '已起飞', passengerCount: 178 },
  { id: 4, flightNo: 'CX3456', airline: '国泰航空', destination: '香港赤鱲角', plannedDeparture: '2024-01-15 16:45:00', actualDeparture: null, gate: 'D20', isRemoteStand: false, status: '计划中', passengerCount: 245 },
  { id: 5, flightNo: 'HU7890', airline: '海南航空', destination: '成都双流', plannedDeparture: '2024-01-15 11:20:00', actualDeparture: '2024-01-15 11:25:00', gate: 'A05', isRemoteStand: true, status: '登机中', passengerCount: 189 },
  { id: 6, flightNo: 'ZH2345', airline: '深圳航空', destination: '西安咸阳', plannedDeparture: '2024-01-15 17:30:00', actualDeparture: null, gate: 'B12', isRemoteStand: false, status: '计划中', passengerCount: 145 },
  { id: 7, flightNo: 'CA5678', airline: '中国国航', destination: '杭州萧山', plannedDeparture: '2024-01-15 13:00:00', actualDeparture: null, gate: 'C03', isRemoteStand: false, status: '延误', passengerCount: 167 },
  { id: 8, flightNo: 'MU1234', airline: '东方航空', destination: '南京禄口', plannedDeparture: '2024-01-15 14:00:00', actualDeparture: null, gate: 'D08', isRemoteStand: true, status: '计划中', passengerCount: 132 }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockFlights]
      if (params?.flightNo) {
        list = list.filter(f => f.flightNo.includes(params.flightNo))
      }
      if (params?.airline) {
        list = list.filter(f => f.airline.includes(params.airline))
      }
      if (params?.status) {
        list = list.filter(f => f.status === params.status)
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
      const flight = mockFlights.find(f => f.id === id)
      setTimeout(() => {
        resolve({ code: 200, data: flight })
      }, 200)
    })
  },

  add(data) {
    return new Promise((resolve) => {
      mockFlights.push({ ...data, id: Date.now() })
      setTimeout(() => {
        resolve({ code: 200, message: '添加成功' })
      }, 300)
    })
  },

  update(id, data) {
    return new Promise((resolve) => {
      const index = mockFlights.findIndex(f => f.id === id)
      if (index > -1) {
        mockFlights[index] = { ...mockFlights[index], ...data }
      }
      setTimeout(() => {
        resolve({ code: 200, message: '更新成功' })
      }, 300)
    })
  },

  updateStatus(id, status) {
    return new Promise((resolve) => {
      const flight = mockFlights.find(f => f.id === id)
      if (flight) {
        flight.status = status
      }
      setTimeout(() => {
        resolve({ code: 200, message: '状态更新成功' })
      }, 300)
    })
  },

  delete(id) {
    return new Promise((resolve) => {
      const index = mockFlights.findIndex(f => f.id === id)
      if (index > -1) {
        mockFlights.splice(index, 1)
      }
      setTimeout(() => {
        resolve({ code: 200, message: '删除成功' })
      }, 300)
    })
  }
}
