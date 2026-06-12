import request from '@/utils/request'

const mockActivityStream = [
  { id: 1, time: '11:45:00', type: 'checkin', title: '旅客入住', content: '陈静 已入住，座位 A-08' },
  { id: 2, time: '11:30:00', type: 'meal', title: '餐食备好', content: 'John Smith 的清真餐已备好' },
  { id: 3, time: '11:15:00', type: 'vehicle', title: '车辆派出', content: '京B67890 出发前往 T2航站楼' },
  { id: 4, time: '10:45:00', type: 'checkin', title: '旅客入住', content: 'John Smith 已入住，座位 C-02' },
  { id: 5, time: '10:30:00', type: 'shower', title: '淋浴使用', content: '张伟 开始使用淋浴间 S-02' },
  { id: 6, time: '10:15:00', type: 'meeting', title: '会议开始', content: '中型会议室 M-02 会议开始' },
  { id: 7, time: '09:45:00', type: 'checkin', title: '旅客入住', content: '杨帆 已入住，座位 B-01' },
  { id: 8, time: '09:30:00', type: 'meal', title: '餐食送出', content: '王强 的点心已送达' },
  { id: 9, time: '09:15:00', type: 'checkin', title: '旅客入住', content: '李娜 已入住，座位 B-05' },
  { id: 10, time: '08:30:00', type: 'checkin', title: '旅客入住', content: '张伟 已入住，座位 A-01' }
]

export default {
  getOverview() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          code: 200,
          data: {
            inRoomPassengers: 6,
            pendingMeals: 3,
            availableShowers: 3,
            availableMeetings: 2,
            availableVehicles: 2,
            delayedFlights: 2
          }
        })
      }, 300)
    })
  },

  getInRoomPassengers() {
    return new Promise((resolve) => {
      const passengers = [
        { id: 1, name: '张伟', flightNo: 'CA1234', status: '在室', seatNo: 'A-01', services: ['淋浴', '餐食'] },
        { id: 2, name: '李娜', flightNo: 'MU5678', status: '在室', seatNo: 'B-05', services: ['餐食'] },
        { id: 4, name: 'John Smith', flightNo: 'CX3456', status: '在室', seatNo: 'C-02', services: ['餐食', '会议'] },
        { id: 6, name: '陈静', flightNo: 'ZH2345', status: '在室', seatNo: 'A-08', services: [] },
        { id: 7, name: '杨帆', flightNo: 'CA5678', status: '在室', seatNo: 'B-01', services: ['会议'] },
        { id: 8, name: '赵敏', flightNo: 'MU1234', status: '在室', seatNo: 'D-03', services: ['接送'] }
      ]
      setTimeout(() => {
        resolve({ code: 200, data: passengers })
      }, 300)
    })
  },

  getActivityStream() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ code: 200, data: [...mockActivityStream] })
      }, 300)
    })
  },

  getTodayFlights() {
    return new Promise((resolve) => {
      const flights = [
        { id: 5, flightNo: 'HU7890', destination: '成都双流', plannedDeparture: '11:20', actualDeparture: '11:25', gate: 'A05', status: '登机中', isRemoteStand: true },
        { id: 1, flightNo: 'CA1234', destination: '上海虹桥', plannedDeparture: '14:30', actualDeparture: null, gate: 'A12', status: '计划中', isRemoteStand: false },
        { id: 7, flightNo: 'CA5678', destination: '杭州萧山', plannedDeparture: '13:00', actualDeparture: null, gate: 'C03', status: '延误', isRemoteStand: false },
        { id: 2, flightNo: 'MU5678', destination: '广州白云', plannedDeparture: '15:00', actualDeparture: null, gate: 'B08', status: '延误', isRemoteStand: true },
        { id: 8, flightNo: 'MU1234', destination: '南京禄口', plannedDeparture: '14:00', actualDeparture: null, gate: 'D08', status: '计划中', isRemoteStand: true },
        { id: 4, flightNo: 'CX3456', destination: '香港赤鱲角', plannedDeparture: '16:45', actualDeparture: null, gate: 'D20', status: '计划中', isRemoteStand: false },
        { id: 6, flightNo: 'ZH2345', destination: '西安咸阳', plannedDeparture: '17:30', actualDeparture: null, gate: 'B12', status: '计划中', isRemoteStand: false },
        { id: 3, flightNo: 'CZ9012', destination: '深圳宝安', plannedDeparture: '10:30', actualDeparture: '10:35', gate: 'C15', status: '已起飞', isRemoteStand: false }
      ]
      setTimeout(() => {
        resolve({ code: 200, data: flights })
      }, 300)
    })
  },

  getStaffStatus() {
    return new Promise((resolve) => {
      const data = [
        { role: '前台接待', onDuty: 3, busy: 1 },
        { role: '餐饮服务', onDuty: 2, busy: 2 },
        { role: '保洁', onDuty: 2, busy: 1 },
        { role: '司机', onDuty: 3, busy: 2 }
      ]
      setTimeout(() => {
        resolve({ code: 200, data })
      }, 300)
    })
  }
}
