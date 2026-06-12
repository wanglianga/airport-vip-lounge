import request from '@/utils/request'

const mockStaff = [
  { id: 1, name: '小王', role: '前台接待', status: '在岗', statusDetail: '空闲' },
  { id: 2, name: '小张', role: '前台接待', status: '在岗', statusDetail: '忙碌' },
  { id: 3, name: '李姐', role: '前台接待', status: '在岗', statusDetail: '空闲' },
  { id: 4, name: '小李', role: '餐饮服务', status: '在岗', statusDetail: '忙碌' },
  { id: 5, name: '小陈', role: '餐饮服务', status: '在岗', statusDetail: '忙碌' },
  { id: 6, name: '小周', role: '餐饮服务', status: '休息', statusDetail: null },
  { id: 7, name: '张姐', role: '保洁', status: '在岗', statusDetail: '空闲' },
  { id: 8, name: '刘姐', role: '保洁', status: '在岗', statusDetail: '忙碌' },
  { id: 9, name: '张师傅', role: '司机', status: '在岗', statusDetail: '空闲' },
  { id: 10, name: '李师傅', role: '司机', status: '在岗', statusDetail: '忙碌' },
  { id: 11, name: '王师傅', role: '司机', status: '在岗', statusDetail: '空闲' },
  { id: 12, name: '赵师傅', role: '司机', status: '休假', statusDetail: null }
]

export default {
  getList(params) {
    return new Promise((resolve) => {
      let list = [...mockStaff]
      if (params?.role) {
        list = list.filter(s => s.role === params.role)
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

  getStatistics() {
    return new Promise((resolve) => {
      const roles = ['前台接待', '餐饮服务', '保洁', '司机']
      const result = roles.map(role => {
        const roleStaff = mockStaff.filter(s => s.role === role)
        const onDuty = roleStaff.filter(s => s.status === '在岗').length
        const busy = roleStaff.filter(s => s.statusDetail === '忙碌').length
        return { role, total: roleStaff.length, onDuty, busy }
      })
      setTimeout(() => {
        resolve({ code: 200, data: result })
      }, 200)
    })
  }
}
