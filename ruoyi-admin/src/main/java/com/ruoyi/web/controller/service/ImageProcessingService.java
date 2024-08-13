/*
package com.example.fishweightestimation.service;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ImageProcessingService {

    public ImagePlus preprocessImage(MultipartFile file) throws IOException {
        // 将上传的文件保存到临时文件
        File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
        file.transferTo(tempFile);

        // 使用ImageJ打开临时文件
        ImagePlus image = IJ.openImage(tempFile.getAbsolutePath());

        // 图像预处理（校正、去噪、增强、裁剪等）
        ImageProcessor processor = image.getProcessor();
        processor.medianFilter();
        processor.findEdges();
        image.setProcessor(processor);

        // 删除临时文件
        tempFile.delete();

        return image;
    }

    public double estimateWeight(ImagePlus processedImage) {
        // 提取特征并估算重量
        ImageProcessor processor = processedImage.getProcessor();
        int width = processor.getWidth();
        int height = processor.getHeight();

        // 简单线性回归模型，假设体积与重量成比例
        double estimatedWeight = width * height * 0.01; // 假设系数为0.01
        return estimatedWeight;
    }
}*/

package com.ruoyi.web.controller.service;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysAnimal;
import com.ruoyi.common.core.domain.entity.SysComputed;
import com.ruoyi.common.utils.ExcelUtil;
import com.ruoyi.system.mapper.SysAnimalMapper;
import com.ruoyi.system.mapper.SysComputedMapper;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class ImageProcessingService {

    @Autowired
    SysComputedMapper sysComputedMapper;

    public ImagePlus preprocessImage(MultipartFile file) throws IOException {
        // 将上传的文件保存到临时文件
        File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
        file.transferTo(tempFile);

        // 使用ImageJ打开临时文件
        ImagePlus image = IJ.openImage(tempFile.getAbsolutePath());

        // 图像预处理（校正、去噪、增强、裁剪等）
        ImageProcessor processor = image.getProcessor();
        processor.medianFilter();
        processor.findEdges();
        image.setProcessor(processor);

        // 删除临时文件
        tempFile.delete();

        return image;
    }

    public double estimateWeight(ImagePlus processedImage) {
        // 提取特征并估算重量
        ImageProcessor processor = processedImage.getProcessor();
        int width = processor.getWidth();
        int height = processor.getHeight();

        // 简单线性回归模型，假设体积与重量成比例
        double estimatedWeight = width * height * 0.01; // 假设系数为0.01
        return estimatedWeight/1010;
    }

    public void add (SysComputed computed){
        sysComputedMapper.add(computed);
    }

    public  List<SysComputed> lsit (SysComputed computed){
       List<SysComputed> sysComputeds = sysComputedMapper.lsit(computed);
       return sysComputeds;
    }

    public void export(HttpServletResponse response) throws Exception {
        List<SysComputed> lsit = sysComputedMapper.lsit(null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] title = {"编号","鱼类名称","图片","预估重量","预估时间"};
        List<String> titleList = Arrays.asList(title);
        List<List<Object>> datas = new ArrayList<>();
        for (SysComputed sysComputed : lsit) {
            List<Object> data = new ArrayList<>();
            data.add(sysComputed.getAnimalName());
            data.add(sysComputed.getImg());
            data.add(sysComputed.getWeight());
            data.add(simpleDateFormat.format(sysComputed.getCreateTime()));
            datas.add(data);
        }
        String fileName = "test";
        String sheetName = "sheet1";

        ExcelUtil.export(response,titleList,datas,fileName,sheetName,null,null , RuoYiConfig.getProfile());

    }
}
