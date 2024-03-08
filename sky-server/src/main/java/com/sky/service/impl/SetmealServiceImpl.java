package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void del(List<Long> ids) {
        for (Long id : ids) {
            //查看套餐是否在启售
            Setmeal setmeal = setmealMapper.getSetmealById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
            //未在启售 - 可以删除
            setmealMapper.del(id);

        }
    }

    @Override
    @Transactional
    public SetmealVO getSetmealById(Long id) {
        //根据id查询套餐
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        //根据套餐id查询相对应菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishesById(id);
        //将菜品加入
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //更改套餐
        setmealMapper.update(setmeal);
        //删除原关联菜品
        setmealDishMapper.delById(setmealDTO.getId());
        //添加新关联菜品
        List<SetmealDish> setmealDish = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish1 : setmealDish){
            setmealDishMapper.insert(setmealDish1);
        }

    }

    @Override
    public void status(Integer status, Long id) {
        //根据id修改状态
        setmealMapper.status(status,id);
    }
}
