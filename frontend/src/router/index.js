import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'CommandCenter',
        component: () => import('@/views/dashboard/CommandCenter.vue'),
        meta: { title: '指挥台总览' }
      },
      {
        path: 'passenger',
        name: 'PassengerManage',
        component: () => import('@/views/airline/PassengerManage.vue'),
        meta: { title: '旅客管理' }
      },
      {
        path: 'flight',
        name: 'FlightManage',
        component: () => import('@/views/airline/FlightManage.vue'),
        meta: { title: '航班管理' }
      },
      {
        path: 'flight-change',
        name: 'FlightChangeManage',
        component: () => import('@/views/airline/FlightChangeManage.vue'),
        meta: { title: '航班变更联动' }
      },
      {
        path: 'checkin',
        name: 'CheckIn',
        component: () => import('@/views/reception/CheckIn.vue'),
        meta: { title: '入住登记' }
      },
      {
        path: 'service-dispatch',
        name: 'ServiceDispatch',
        component: () => import('@/views/reception/ServiceDispatch.vue'),
        meta: { title: '服务调度' }
      },
      {
        path: 'meal-prepare',
        name: 'MealPrepare',
        component: () => import('@/views/catering/MealPrepare.vue'),
        meta: { title: '餐食准备' }
      },
      {
        path: 'vehicle-dispatch',
        name: 'VehicleDispatch',
        component: () => import('@/views/fleet/VehicleDispatch.vue'),
        meta: { title: '车辆调度' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
