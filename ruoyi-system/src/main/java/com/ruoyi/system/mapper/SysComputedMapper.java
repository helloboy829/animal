package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysComputed;

import java.util.List;

/**
 * @Description: TODO
 * @Author: jzy
 * @Date: 2024/6/14
 * @Version:v1.0
 */
public interface SysComputedMapper {
    void add(SysComputed computed);

    List<SysComputed> lsit(SysComputed computed);
}
