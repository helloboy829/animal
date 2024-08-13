import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


export function listAnimal(query) {
  return request({
    url: '/system/animal/list',
    method: 'get',
    params: query
  })
}


export function getAnimal(id) {
  return request({
    url: '/system/animal/' + parseStrEmpty(id),
    method: 'get'
  })
}


export function addAnimal(data) {
  return request({
    url: '/system/animal',
    method: 'post',
    data: data
  })
}

export function updateAnimal(data) {
  return request({
    url: '/system/animal',
    method: 'put',
    data: data
  })
}




export function delAnimal(id) {
  return request({
    url: '/system/animal/' + id,
    method: 'delete'
  })
}



export function uploadImg(data) {
  return request({
    url: '/system/animal/upload/img',
    method: 'post',
    data: data
  })
}

export function computedWeight1(id) {
  return request({
    url: '/computed/' + id,
    method: 'post'
  })
}


