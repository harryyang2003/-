package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解,用于标识公共字段自动填充
@Target(ElementType.METHOD)     //加载的位置,加在方法上METHOD
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //Update Insert
    OperationType value();  //操作类型
}
