package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAnimal;

import java.util.List;

/**
 * @Description: TODO
 * @Author: jzy
 * @Date: 2024/6/14
 * @Version:v1.0
 */
public interface ISysAnimalService {
    List<SysAnimal> selectAnimalList(SysAnimal animal);

    int add(SysAnimal animal);

    int deleteAnimalByIds(Long ids);

    SysAnimal getInfo(Long id);

    int update(SysAnimal animal);
}
