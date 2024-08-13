package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysAnimal;
import com.ruoyi.system.mapper.SysAnimalMapper;
import com.ruoyi.system.service.ISysAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysAnimalServiceImpl implements ISysAnimalService {


    @Autowired
    private SysAnimalMapper animalServiceMapper;
    @Override
    public List<SysAnimal> selectAnimalList(SysAnimal animal) {
        return animalServiceMapper.selectAnimalList(animal);
    }

    @Override
    public int add(SysAnimal animal) {
        return animalServiceMapper.add(animal);
    }

    @Override
    public int deleteAnimalByIds(Long ids) {
        return animalServiceMapper.deleteAnimalByIds(ids);
    }

    @Override
    public SysAnimal getInfo(Long id) {
        return animalServiceMapper.getInfo(id);
    }

    @Override
    public int update(SysAnimal animal) {
        return animalServiceMapper.update(animal);
    }
}
