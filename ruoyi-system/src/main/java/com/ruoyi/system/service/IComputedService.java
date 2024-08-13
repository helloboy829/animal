package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAnimal;

import java.util.List;


public interface IComputedService {
    List<SysAnimal> selectAnimalList(SysAnimal animal);

    int add(SysAnimal animal);

    int deleteAnimalByIds(Long ids);

    SysAnimal getInfo(Long id);

    int update(SysAnimal animal);
}
