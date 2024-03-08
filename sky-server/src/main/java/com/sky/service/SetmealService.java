package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void insert(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void del(List<Long> ids);

    SetmealVO getSetmealById(Long id);

    void update(SetmealDTO setmealDTO);

    void status(Integer status, Long id);
}
