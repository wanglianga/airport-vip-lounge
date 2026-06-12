import request from '@/utils/request'

const mockMeetings = [
  { id: 1, roomNo: 'M-01', name: '小型会议室', capacity: 6, status: '可用', currentMeeting: null },
  { id: 2, roomNo: 'M-02', name: '中型会议室', capacity: 12, status: '使用中', currentMeeting: '商务洽谈' },
  { id: 3, roomNo: 'M-03', name: '大型会议室', capacity: 20, status: '可用', currentMeeting: null },
  { id: 4, roomNo: 'M-04', name: 'VIP会议室', capacity: 8, status: '预约中', currentMeeting: '董事会议' }
]

export default {
  getList() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({ code: 200, data: [...mockMeetings] })
      }, 200)
    })
  },

  getStatistics() {
    return new Promise((resolve) => {
      const total = mockMeetings.length
      const available = mockMeetings.filter(m => m.status === '可用').length
      const inUse = mockMeetings.filter(m => m.status === '使用中').length
      const reserved = mockMeetings.filter(m => m.status === '预约中').length
      setTimeout(() => {
        resolve({
          code: 200,
          data: { total, available, inUse, reserved }
        })
      }, 200)
    })
  },

  reserve(roomId, meetingName, startTime, duration) {
    return new Promise((resolve) => {
      const room = mockMeetings.find(m => m.id === roomId)
      if (room) {
        room.status = '预约中'
        room.currentMeeting = meetingName
      }
      setTimeout(() => {
        resolve({ code: 200, message: '预约成功' })
      }, 300)
    })
  },

  startMeeting(roomId) {
    return new Promise((resolve) => {
      const room = mockMeetings.find(m => m.id === roomId)
      if (room) {
        room.status = '使用中'
      }
      setTimeout(() => {
        resolve({ code: 200, message: '会议已开始' })
      }, 300)
    })
  },

  endMeeting(roomId) {
    return new Promise((resolve) => {
      const room = mockMeetings.find(m => m.id === roomId)
      if (room) {
        room.status = '可用'
        room.currentMeeting = null
      }
      setTimeout(() => {
        resolve({ code: 200, message: '会议已结束' })
      }, 300)
    })
  }
}
