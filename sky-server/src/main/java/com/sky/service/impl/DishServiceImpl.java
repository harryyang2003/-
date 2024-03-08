package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表插入数据
        dishMapper.insert(dish);

        //获取菜品id
        long id = dish.getId();



        //向口味表插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //判断list是否为空
        if(flavors != null && flavors.size()>0){
            flavors.forEach(df -> df.setDishId(id));
            //向口味表插入
        dishFlavorMapper.insert(flavors);

        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //判断菜品是否能被删除 -- 是否启售中
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //正在启售
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断菜品是否能被删除 -- 是否被关联
        List<Long> setmealDishIdsByIds = setmealDishMapper.getSetmealDishIdsByIds(ids);
        if(setmealDishIdsByIds != null && setmealDishIdsByIds.size() > 0){
            //菜品被关联,不可删除
           throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        for (Long id : ids) {
            //删除菜品表中的菜品数据
            dishMapper.deleteById(id);
            //删除菜品关联口味
            dishFlavorMapper.deleteById(id);
        }





    }

    @Override
    @Transactional
    public DishVO getById(Long id) {
        //根据id查询菜品
        Dish dish = dishMapper.getById(id);
        //根据id查询口味
        List<DishFlavor> list = dishFlavorMapper.getByDishId(id);
        //联合
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(list);

        return dishVO;

    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表更新数据
        dishMapper.update(dish);
        log.info("1");

        //删除原有菜品口味
        dishFlavorMapper.deleteById(dishDTO.getId());
        log.info("2");

        //添加新口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //插入
            dishFlavorMapper.insert(flavors);
            log.info("3");
        }

    }

    @Override
    public List<Dish> getDishByCategoryId(String categoryId) {
        //根据分类id查询菜品
        List<Dish> dishList = dishMapper.getDishByCategoryId(categoryId);
        return dishList;
    }
}
