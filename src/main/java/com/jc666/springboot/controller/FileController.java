package com.jc666.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jc666.springboot.common.Result;
import com.jc666.springboot.entity.Files;
import com.jc666.springboot.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * File upload interface
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;


    /**
     * @param file get data from frontend
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // define file unique id
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        System.out.println("fileUUID: " + fileUUID);

        // add type to show real file
        File uploadFile = new File(fileUploadPath + fileUUID);
        System.out.println("uploadFile :"+ uploadFile);

        // check if directory exist
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        // get file and upload to disk
        file.transferTo(uploadFile);
        // get file md5 id
        String md5 = SecureUtil.md5(uploadFile);
        System.out.println("md5: "+ md5);
        //find file from database
        Files dbFiles = getFileByMd5(md5);
        System.out.println("File from database: "+dbFiles);
        //get file url
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            // deleted duplicated file
            uploadFile.delete();
        } else {
            // database does not exist duplicated file
            url = "http://localhost:9090/file/" + fileUUID;
        }

        //save file to database
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024); // size kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        System.out.println("saveFile: "+saveFile);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * File download interface
     * http://localhost:9090/file/{fileUUID}
     */

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }

    /**
     * use md5 id to check if files md5 exist
     *
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5) {
        // check if file md5 exist
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    // new or update
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * page search interface
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // find not deleted file
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }


}
