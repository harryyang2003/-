package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Override
    @Transactional
    public void insert(SetmealDTO setmealDTO) {
        //添加套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //添加套餐_菜品表
        List<SetmealDish> list = setmealDTO.getSetmealDishes();

        for (SetmealDish setmealDish : list) {
            //将套餐主键加入 - 获取套餐主键
            Long setmealId = setmealMapper.getIdByName(setmealDTO.getName());
            setmealDish.setSetmealId(setmealId);
            setmealDishMapper.insert(setmealDish);
        }

    }
}
