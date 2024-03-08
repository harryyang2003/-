package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);


    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    @Select("select * from setmeal where name = #{name}")
    Long getIdByName(String name);

    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getSetmealById(Long id);

    @Delete("delete from setmeal where id = #{id}")
    void del(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Update("update setmeal set status = #{status} where id = #{id} ")
    void status(Integer status, Long id);
}
