package com.jc666.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * mybatis plus code generator
 */


public class CodeGenerator {

    public static void main(String[] args) {
        generate();
    }

    private static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/jc", "root", "root123")
                .globalConfig(builder -> {
                    builder.author("L0veRamen") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\chenq\\OneDrive\\Desktop\\Spring_Vue_Project\\springboot\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.jc666.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\chenq\\OneDrive\\Desktop\\Spring_Vue_Project\\springboot\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
//                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle().enableRestStyle();
                    builder.addInclude("sys_menu") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
