package com.ruoyi.system.mapper;


import com.ruoyi.common.core.domain.entity.SysAnimal;
import com.ruoyi.common.core.domain.entity.SysComputed;

import java.util.List;

public interface SysAnimalMapper {
    List<SysAnimal> selectAnimalList(SysAnimal animal);

    int add(SysAnimal animal);

    int deleteAnimalByIds(Long id);

    SysAnimal getInfo(Long id);

    int update(SysAnimal animal);


}
