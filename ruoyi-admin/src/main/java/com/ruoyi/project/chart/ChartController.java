package com.ruoyi.project.chart;

import org.jfree.chart.ChartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private DataFetcher dataFetcher;

    @Autowired
    private ChartGenerator chartGenerator;

    @GetMapping(value = "/weightDistribution", produces = MediaType.IMAGE_PNG_VALUE)
    public void getWeightDistributionChart(HttpServletResponse response) throws IOException {
        List<Double> weights = dataFetcher.fetchWeights();
        BufferedImage chartImage = chartGenerator.generateChart(weights);
        response.setContentType("image/png");
        ImageIO.write(chartImage, "PNG", response.getOutputStream());
    }
}
