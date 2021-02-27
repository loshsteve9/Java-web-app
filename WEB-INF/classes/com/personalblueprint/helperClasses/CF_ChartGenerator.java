/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 */

/* ################################################
 *  For Wolf Consulting:
 *  classes in org.jfree that have been modified:
 *  org.jfree.JFreeChart
 *  org.jfree.chart.servlet.DisplayChart
 */

package com.personalblueprint.helperClasses;

import java.awt.Font;
import java.awt.Color;
import java.awt.Paint;
import java.text.DecimalFormat;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class CF_ChartGenerator extends Object implements java.io.Serializable {
    
    String[] subscaleNames;
    double[] subscaleResults;
    String sdNames[] = {"a"};
    String theTest = "";
    
    public JFreeChart buildChart(String[] ssNames, double[] ssResults, String whichTest) {
        
        theTest = whichTest;
        subscaleResults = ssResults;
        int arraySize = subscaleResults.length;
        subscaleNames = ssNames;
        final CategoryDataset dataset = createDataset(arraySize);
        
        final JFreeChart chart = createChart(dataset);
        
            return chart;
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private CategoryDataset createDataset(int size) {
        final double[][] data = new double[1][size];
        System.arraycopy(subscaleResults, 0, data[0], 0, size);
        
        return DatasetUtilities.createCategoryDataset(
            sdNames,
            subscaleNames,
            data
        );
    }
//    /**
//     * A custom renderer that returns a different color for each item in a single series.
//     */
//    class CustomRenderer extends BarRenderer {
//
//        /** The colors. */
//        private Paint[] colors;
//
//        /**
//         * Creates a new renderer.
//         *
//         * @param colors  the colors.
//         */
//        public CustomRenderer(final Paint[] colors) {
//            this.colors = colors;
//        }
//
//        /**
//         * Returns the paint for an item.  Overrides the default behaviour inherited from
//         * AbstractSeriesRenderer.
//         *
//         * @param row  the series.
//         * @param column  the category.
//         *
//         * @return The item color.
//         */
//        public Paint getItemPaint(final int row, final int column) {
//            return this.colors[column % this.colors.length];
//        }
//    }
//    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a sample chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createBarChart(
            "",                         // chart title
            "",                         // domain axis label (categoryAxisLabel)
            "",                         // range axis label (valueAxisLabel)
            dataset,                    // data
            PlotOrientation.HORIZONTAL, // the plot orientation
            false,                      // include legend
            true,                       // include tooltips
            false                       // include urls
        );
        
/*  
        int rMaxV = 0;
        int rMinV = 0;
        double tickUnit = 0.00;
        if("pbi".equals(theTest)) {
            rMaxV = 3;
            rMinV = -3;
            tickUnit = 0.25;
        }else if("sixComp".equals(theTest)) {
            rMaxV = 4;
            rMinV = -4;
            tickUnit = 0.50;
        }else if("threeCs".equals(theTest)) {
            rMaxV = 4;
            rMinV = -4;
            tickUnit = 0.50;
        }
*/
        int rMaxV = 6;
        int rMinV = -6;
        double tickUnit = 1;

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage("NO DATA!");
        
        javax.swing.ImageIcon image = new javax.swing.ImageIcon("d:/clientWork/wolfConsult/ROOT/images/bg_thumbprint.gif");
        plot.setBackgroundImage(image.getImage());        
                
//        This goes with the CustomRenderer up top to change all the bar colors
//        final CategoryItemRenderer renderer = new CustomRenderer(
//            new Paint[] {Color.blue}
//        );
        
        final CategoryItemRenderer renderer = new BarRenderer();
        //renderer.setLabelGenerator(new StandardCategoryItemLabelGenerator());
        //renderer.setItemLabelPaint(java.awt.Color.blue);
        //renderer.setItemLabelFont(new Font("Helvetica", Font.BOLD, 10));
        //renderer.setItemLabelsVisible(false);
        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.OUTSIDE3, TextAnchor.CENTER_LEFT);
        //renderer.setPositiveItemLabelPosition(p);
        final ItemLabelPosition n = new ItemLabelPosition(
            ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
        renderer.setBaseNegativeItemLabelPosition(n);
        renderer.setSeriesPaint(0, java.awt.Color.blue);
        plot.setRenderer(renderer);
        
        TickUnits units = new TickUnits();
        units.add(new NumberTickUnit(tickUnit, new DecimalFormat("0")));
        
        // set tick range of range (standard deviation) axis
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelPaint(java.awt.Color.blue);
        rangeAxis.setLabelFont(new Font("Helvetica", Font.BOLD, 10));        
        rangeAxis.setTickLabelPaint(java.awt.Color.blue);
        rangeAxis.setStandardTickUnits(units);
        rangeAxis.setRange(rMinV, rMaxV);
        
        // set category axis parameters
        final CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setLowerMargin(0.01); // percentage of space before first bar
        xAxis.setUpperMargin(0.01); // percentage of space after last bar
        xAxis.setCategoryMargin(0.18); // percentage of space between categories
        xAxis.setMaximumCategoryLabelWidthRatio(0.7f); // sets the ratio between chart and label spaces
        xAxis.setTickLabelPaint(java.awt.Color.blue);
        xAxis.setTickLabelFont(new Font("Helvetica", Font.PLAIN, 11));
        
        return chart;
    }
}
