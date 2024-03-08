package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getSetmealDishIdsByIds(List<Long> dishIds);


    void insert(SetmealDish setmealDish);

    @Select("select * from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishesById(Long id);

    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id = #{id}")
    void delById(Long id);
}
