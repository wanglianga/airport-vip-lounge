import request from '@/utils/request'

export default {
  getByFlightChangeId(flightChangeId) {
    return request({
      url: `/api/passenger-confirmations/flight-change/${flightChangeId}`,
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `/api/passenger-confirmations/${id}`,
      method: 'get'
    })
  },

  confirm(id, data) {
    return request({
      url: `/api/passenger-confirmations/${id}/confirm`,
      method: 'put',
      data
    })
  },

  getSummary(flightChangeId) {
    return request({
      url: `/api/passenger-confirmations/summary/${flightChangeId}`,
      method: 'get'
    })
  }
}
