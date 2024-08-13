import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


export function listComputed(query) {
  return request({
    url: '/computed/list',
    method: 'get',
    params: query
  })
}

export function exportExcel() {
  return request({
    url: '/computed/export',
    method: 'post'
  })
}

