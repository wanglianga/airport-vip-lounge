import request from '@/utils/request'

const mockMeals = [
  { id: 1, passengerName: '张伟', passengerId: 1, flightNo: 'CA1234', departureTime: '2024-01-15 14:30:00', mealType: '正餐', quantity: 1, specialRequest: '无', status: '待准备' },
  { id: 2, passengerName: '李娜', passengerId: 2, flightNo: 'MU5678', departureTime: '2024-01-15 15:00:00', mealType: '素食餐', quantity: 3, specialRequest: '儿童餐一份', status: '准备中' },
  { id: 3, passengerName: '王强', passengerId: 3, flightNo: 'CZ9012', departureTime: '2024-01-15 10:30:00', mealType: '点心', quantity: 1, specialRequest: '无', status: '已送达' },
  { id: 4, passengerName: 'John Smith', passengerId: 4, flightNo: 'CX3456', departureTime: '2024-01-15 16:45:00', mealType: '清真餐', quantity: 2, specialRequest: '不要洋葱', status: '已备好' },
  { id: 5, passengerName: '陈静', passengerId: 6, flightNo: 'ZH2345', departureTime: '2024-01-15 17:30:00', mealType: '正餐', quantity: 1, specialRequest: '海鲜过敏', status: '待准备' },
  { id: 6, passengerName: '杨帆', passengerId: 7, flightNo: 'CA5678', departureTime: '2024-01-15 13:00:00', mealType: '正餐', quantity: 2, specialRequest: '儿童餐一份', status: '准备中' },
  { id: 7, passengerName: '赵敏', passengerId: 8, flightNo: 'MU1234', departureTime: '2024-01-15 14:00:00', mealType: '点心', quantity: 1, specialRequest: '无', status: '待准备' }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockMeals]
      if (params?.status) {
        list = list.filter(m => m.status === params.status)
      }
      if (params?.flightNo) {
        list = list.filter(m => m.flightNo.includes(params.flightNo))
      }
      list.sort((a, b) => new Date(a.departureTime) - new Date(b.departureTime))
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

  getStatistics() {
    return new Promise((resolve) => {
      const pending = mockMeals.filter(m => m.status === '待准备').length
      const preparing = mockMeals.filter(m => m.status === '准备中').length
      const ready = mockMeals.filter(m => m.status === '已备好').length
      const delivered = mockMeals.filter(m => m.status === '已送达').length
      setTimeout(() => {
        resolve({
          code: 200,
          data: { pending, preparing, ready, delivered }
        })
      }, 200)
    })
  },

  startPreparing(id) {
    return new Promise((resolve) => {
      const meal = mockMeals.find(m => m.id === id)
      if (meal) {
        meal.status = '准备中'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '已开始准备' })
      }, 300)
    })
  },

  markReady(id) {
    return new Promise((resolve) => {
      const meal = mockMeals.find(m => m.id === id)
      if (meal) {
        meal.status = '已备好'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '已标记备好' })
      }, 300)
    })
  },

  deliver(id) {
    return new Promise((resolve) => {
      const meal = mockMeals.find(m => m.id === id)
      if (meal) {
        meal.status = '已送达'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '已送出' })
      }, 300)
    })
  }
}
