package com.ruoyi.project.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@Service
public class ChartGenerator {
    public BufferedImage generateChart(List<Double> weights) {
        // Create Histogram Dataset
        HistogramDataset histogramDataset = new HistogramDataset();
        histogramDataset.setType(HistogramType.FREQUENCY);
        double[] weightArray = weights.stream().mapToDouble(Double::doubleValue).toArray();
        histogramDataset.addSeries("Weight Distribution", weightArray, 20); // 20 bins

        // Create Histogram Chart
        JFreeChart histogramChart = ChartFactory.createHistogram(
                "Weight Distribution",
                "Weight",
                "Frequency",
                histogramDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Create Cumulative Dataset
        XYSeries cumulativeSeries = new XYSeries("Cumulative Frequency");
        double cumulative = 0;
        for (double weight : weightArray) {
            cumulative += weight;
            cumulativeSeries.add(weight, cumulative);
        }
        XYSeriesCollection cumulativeDataset = new XYSeriesCollection(cumulativeSeries);

        // Create Line Renderer
        XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer();
        lineRenderer.setSeriesPaint(0, Color.RED);
        lineRenderer.setSeriesStroke(0, new BasicStroke(2.0f));

        // Get the plot from the histogram chart and add the cumulative dataset
        XYPlot plot = histogramChart.getXYPlot();
        plot.setDataset(1, cumulativeDataset);
        plot.setRenderer(1, lineRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Set axis properties
        NumberAxis axis2 = new NumberAxis("Cumulative Frequency");
        plot.setRangeAxis(1, axis2);
        plot.mapDatasetToRangeAxis(1, 1);

        // Convert the chart to a BufferedImage
        BufferedImage bufferedImage = histogramChart.createBufferedImage(800, 600);
        return bufferedImage;
    }
}
