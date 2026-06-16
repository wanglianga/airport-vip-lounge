import request from '@/utils/request'

export default {
  getByFlightChangeId(flightChangeId) {
    return request({
      url: `/api/service-adjustments/flight-change/${flightChangeId}`,
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `/api/service-adjustments/${id}`,
      method: 'get'
    })
  },

  recalculate(flightChangeId) {
    return request({
      url: `/api/service-adjustments/recalculate/${flightChangeId}`,
      method: 'post'
    })
  }
}
