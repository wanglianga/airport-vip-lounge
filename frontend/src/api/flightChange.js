import request from '@/utils/request'

export default {
  getList(params) {
    return request({
      url: '/api/flight-changes',
      method: 'get',
      params
    })
  },

  getById(id) {
    return request({
      url: `/api/flight-changes/${id}`,
      method: 'get'
    })
  },

  create(data) {
    return request({
      url: '/api/flight-changes',
      method: 'post',
      data
    })
  },

  getByFlightId(flightId) {
    return request({
      url: `/api/flight-changes/flight/${flightId}`,
      method: 'get'
    })
  }
}
