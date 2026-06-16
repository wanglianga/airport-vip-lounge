import request from '@/utils/request'

export default {
  getList(params) {
    return request({
      url: '/api/vehicle-tasks',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/vehicle-tasks/${id}`,
      method: 'get'
    })
  },

  getByFlightChangeId(flightChangeId) {
    return request({
      url: `/api/vehicle-tasks/flight-change/${flightChangeId}`,
      method: 'get'
    })
  },

  getByFlightId(flightId) {
    return request({
      url: `/api/vehicle-tasks/flight/${flightId}`,
      method: 'get'
    })
  },

  update(id, data) {
    return request({
      url: `/api/vehicle-tasks/${id}`,
      method: 'put',
      data
    })
  },

  recalculate(flightChangeId) {
    return request({
      url: `/api/vehicle-tasks/recalculate/${flightChangeId}`,
      method: 'post'
    })
  }
}
