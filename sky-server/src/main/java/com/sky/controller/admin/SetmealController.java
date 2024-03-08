package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.lettuce.core.ConnectionEvents;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐分类相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("添加套餐接口")
    public Result insert(@RequestBody SetmealDTO setmealDTO){
        log.info("开始插入套餐{}",setmealDTO);
        setmealService.insert(setmealDTO);
        return null;
    }

    @GetMapping("/page")
    @ApiOperation("分页查询套餐")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询套餐");
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result del(@RequestParam List<Long> ids){
        log.info("删除套餐{}",ids);
        setmealService.del(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id){
        log.info("根据id查询套餐{}",id);
        SetmealVO setmealVO = setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改菜品{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐启售停售")
    public Result status(@PathVariable Integer status,@RequestParam Long id){
        log.info("套餐启售停售");
        setmealService.status(status,id);
        return Result.success();
    }
}
