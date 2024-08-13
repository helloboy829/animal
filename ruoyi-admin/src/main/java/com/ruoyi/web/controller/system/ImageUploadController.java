
package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAnimal;
import com.ruoyi.common.core.domain.entity.SysComputed;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysAnimalService;
import com.ruoyi.web.controller.service.ImageProcessingService;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/computed")
public class ImageUploadController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    @Autowired
    private ImageProcessingService imageProcessingService;

    @Autowired
    private ISysAnimalService animalService;

    @GetMapping("/list")
    public TableDataInfo list(SysComputed computed) {
        startPage();
        List<SysComputed> list = imageProcessingService.lsit(computed);
        return getDataTable(list);
    }

    @Anonymous
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        imageProcessingService.export(response);
    }

    @PostMapping("/{animalId}")
    public AjaxResult uploadImage(@PathVariable("animalId") Long id) {
        try {
            SysAnimal info = animalService.getInfo(id);
            String img = info.getImg();
            String s = RuoYiConfig.getProfile() + img;
            s = s.replace("/profile", "");

            File file = new File(s);
            // 图像预处理
//            ImagePlus processedImage = imageProcessingService.preprocessImage(file);

            ImagePlus processedImage = IJ.openImage(file.getAbsolutePath());
            ImageProcessor processor = processedImage.getProcessor();
            processor.medianFilter();
            processor.findEdges();
            processedImage.setProcessor(processor);
            // 估算重量
            double estimatedWeight = imageProcessingService.estimateWeight(processedImage);

            BigDecimal bigDecimal = new BigDecimal(estimatedWeight);
            bigDecimal = bigDecimal.setScale(2, 2);
            info.setWeight(bigDecimal);
            animalService.update(info);
            SysComputed sysComputed = new SysComputed();
            sysComputed.setAnimalId(info.getId());
            sysComputed.setAnimalName(info.getName());
            sysComputed.setImg(info.getImg());
            sysComputed.setWeight(bigDecimal);
            imageProcessingService.add(sysComputed);
            return AjaxResult.success();
        } catch (Exception e) {
            logger.error("Error processing the image", e);
            return AjaxResult.error();
        }
    }


}



