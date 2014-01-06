/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph_indicate_Make;

import dataArchive.Job;
import dataArchive.Machine;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.List;
import javaapplication32.PlotPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartTheme;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.LevelRenderer;
import org.jfree.chart.util.ParamChecks;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.ui.TextAnchor;
import result.newpackage.I_AnswerReportContainer;

/**
 *
 * @author 117
 */
public class GraphPloter extends ChartFactory {

    private I_AnswerReportContainer reportContainer;
    private PlotPanel plotwindow;
    private JFreeChart chart;
    private static ChartTheme currentTheme = new StandardChartTheme("JFree");

    public GraphPloter(PlotPanel plotwindow) {
        super();
        this.plotwindow = plotwindow;
    }

    public boolean setGraph() {
        plotwindow.setGlaph(chart);
        return true;
    }

    public JFreeChart explessChart() {
        return chart;
    }

    public boolean drawgraph(I_AnswerReportContainer container) {
        /*
         SwingWork1 grapher = new SwingWork1(container);
         grapher.execute();
         */



        //先ずはデータをこしらえる

        Integer jobs = container.getCountJob();
        Integer machines = container.getCountMachine();
        List<Job> joblist = container.getJobList();
        //List<Order> orderlist = container.getOrderList();
        List<Machine> machineList = container.getMachineList();

        String[] jobSeries = new String[jobs];
        String[] machineCategorys = new String[machines];


        double[][] ends = new double[jobs][machines];
        double[][] starts = new double[jobs][machines];

        //値の設定とレンジの決定

        for (int i = 0; i < jobs; i++) {


            for (int j = 0; j < machines; j++) {

                starts[i][j] = container.getStarttimeInteger(joblist.get(i), machineList.get(j));
                ends[i][j] = container.getEndtimeInteger(joblist.get(i), machineList.get(j));





            }

        }

        //レンジの決定。リードタイムより若干多めの値をとる。
        double range;
        double rangeBase;
        rangeBase = rangeBaseMaker(container, joblist);




        double plusseryInset = (double) rangeBase / 20;
        if (rangeBase - Math.floor(plusseryInset) != 0) {
            range = rangeBase + Math.ceil(plusseryInset);
        } else {
            range = rangeBase + plusseryInset;
        }


        //カテゴリ（縦軸）の決定。もちろんマシン


        for (int i = 0; i < machines; i++) {
            machineCategorys[i] = machineList.get(i).getViewName();
        }

        //タスクの決定。ジョブ。
        for (int i = 0; i < jobs; i++) {
            jobSeries[i] = joblist.get(i).getViewName();
        }




        DefaultIntervalCategoryDataset data = new DefaultIntervalCategoryDataset(starts, ends);
        data.setCategoryKeys(machineCategorys);
        data.setSeriesKeys(jobSeries);

        //次に納期の格納。別データに格納
        DefaultCategoryDataset data2 = new DefaultCategoryDataset();

        for (int i = 0; i < jobs; i++) {

            int deliver = joblist.get(i).getTheTimeOfDelivery();
            //machineCategorysはマジックナンバーで、統一されていればなんでもよい
            data2.addValue(deliver, jobSeries[i], machineCategorys[0]);


        }



        String name = container.getSolutionMeans();



        //ここまででこさえるべきデータはそろった。ここからはグラフの生成

        chart = createGraph(data, data2, range, name);



        return true;
    }

    private JFreeChart createGraph(DefaultIntervalCategoryDataset data, DefaultCategoryDataset data2, double range, String name) {



        Font labelFont = new Font("MSUIGothic", Font.PLAIN, 10);

        final String title = name;
        final String xTitle = "Machines";
        final String yTitle = "Time";



        JFreeChart chart;


        //グラフ軸　x,yAxisの設定。

        final CategoryAxis xAxis = new CategoryAxis(xTitle);



        //xAxis.setLabelFont(titleFont);

        xAxis.setTickLabelFont(labelFont);
        xAxis.setTickMarksVisible(false);



        final NumberAxis yAxis = new NumberAxis(yTitle);


        //yAxis.setLabelFont(titleFont);
        /*
         yAxis.setTickLabelFont(labelFont);
         double ets = 0.9931/20 ;*/



        yAxis.setRange(0, range);

        //final DecimalFormat formatter = new DecimalFormat("#.#par");
        //yAxis.setTickUnit(new NumberTickUnit(0.05, formatter));
        //System.out.println(yAxis.getAxisLineStroke());

        //グラフ軸　x,yAxisの設定ここまで

        //タスクを示すレンダラ１の設定。

        final IntervalBarRenderer renderer = new rendIntervalBar();

        CategoryItemLabelGenerator labelGenerator = new LabelGeneCustom();

        

        renderer.setShadowVisible(false);
        Font defaultfont = renderer.getBaseItemLabelFont();
        
        //ラベルフォント
        renderer.setBaseItemLabelFont(new Font(defaultfont.getFontName(),Font.BOLD,12));
        
        
        
        renderer.setBaseItemLabelGenerator(labelGenerator);
        renderer.setBaseItemLabelsVisible(true);
        
        
        ItemLabelPosition p = new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER);
        
        renderer.setBasePositiveItemLabelPosition(p);
        renderer.setBaseNegativeItemLabelPosition(p);
        
        //   renderer.setLabelGenerator(new IntervalCategoryLabelGenerator());
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelPaint(Color.BLACK);






        //日本語文字化け対策！！
        /*
         rendIntervalBar.setDefaultBarPainter(new StandardBarPainter());
         BarRenderer.setDefaultBarPainter(new StandardBarPainter());
         XYBarRenderer.setDefaultBarPainter(new StandardXYBarPainter());

         /*
         final ItemLabelPosition p = new ItemLabelPosition(
         ItemLabelAnchor.CENTER, TextAnchor.CENTER
         );
         renderer.setPositiveItemLabelPosition(p);
         */

        //設定ここまで

        //納期を示すレンダラ２の設定
        final LevelRenderer renderer2 = new rendSingleBar();
        
        
        //納期を示す棒を太くしよう。
        


        //設定ここまで


        //plotの設定。データ２つと、２つのレンダラ、xyAxisを設定する。

        final CategoryPlot plot = new CategoryPlot();



        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        plot.setDataset(0, data);
        plot.setRenderer(0, renderer);

        LegendItemCollection temp = plot.getLegendItems();

        plot.setDataset(1, data2);
        plot.setRenderer(1, renderer2);

        plot.setFixedLegendItems(temp);

        {//納期バーの色を合わせる。
            for (int i = 0; i < data2.getRowCount(); i++) {
                Paint aa = renderer.getSeriesPaint(i);
                renderer2.setSeriesPaint(i, aa);
                renderer2.setSeriesStroke(i, new BasicStroke(4.0f));
            }
        }

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

        plot.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.BLACK);
        plot.setOrientation(PlotOrientation.HORIZONTAL);


        //設定ここまで

        //チャートの設定
        chart = new JFreeChart(title, null, plot, true);
        chart.setBackgroundPaint(Color.white);
        //設定ここまで


        //this.setChartTheme(StandardChartTheme.createLegacyTheme());
        // 凡例（画面の下）
        //chart.getLegend().setItemFont(new Font("MSUIGothic", Font.PLAIN, 10));

        //currentTheme.apply(chart);




        return chart;
    }

    private double rangeBaseMaker(I_AnswerReportContainer container, List<Job> joblist) {

        //リードタイムか納期か、大きい方のレンジのベースとなる値を返す
        //レンジの値自体は別で計算してねー

        //もしリードタイム基準のレンジにしたいならこれ
        double readrangeBase = container.getReadTime();




        //もし納期も含めたグラフにしたいならこれ
        double deliverrangeBase = 0;
        for (int i = 0; i < joblist.size(); i++) {

            if (deliverrangeBase < joblist.get(i).getTheTimeOfDelivery()) {
                deliverrangeBase = joblist.get(i).getTheTimeOfDelivery();
            }
        }

        double rangeBase = Math.max(readrangeBase, deliverrangeBase);

        return rangeBase;
    }

    public class rendIntervalBar extends IntervalBarRenderer {

        @Override
        protected void calculateBarWidth(CategoryPlot plot,
                Rectangle2D dataArea,
                int rendererIndex,
                CategoryItemRendererState state) {

            CategoryAxis domainAxis = getDomainAxis(plot, rendererIndex);
            CategoryDataset dataset = plot.getDataset(rendererIndex);
            if (dataset != null) {
                int columns = dataset.getColumnCount();
                int rows = state.getVisibleSeriesCount() >= 0
                        ? state.getVisibleSeriesCount() : dataset.getRowCount();
                double space = 0.0;
                PlotOrientation orientation = plot.getOrientation();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    space = dataArea.getHeight();
                } else if (orientation == PlotOrientation.VERTICAL) {
                    space = dataArea.getWidth();
                }
                double maxWidth = space * getMaximumBarWidth();
                double categoryMargin = 0.0;
                double currentItemMargin = 0.0;
                if (columns > 1) {
                    categoryMargin = domainAxis.getCategoryMargin();
                }
                if (rows > 1) {
                    // currentItemMargin = getItemMargin();
                }
                double used = space * (1 - domainAxis.getLowerMargin()
                        - domainAxis.getUpperMargin()
                        - categoryMargin - currentItemMargin);
                if ((rows * columns) > 0) {
                    state.setBarWidth(Math.min(used / (/*rows*/1 * columns), maxWidth));
                } else {
                    state.setBarWidth(Math.min(used, maxWidth));
                }
            }
        }

        @Override
        protected double calculateBarW0(CategoryPlot plot,
                PlotOrientation orientation,
                Rectangle2D dataArea,
                CategoryAxis domainAxis,
                CategoryItemRendererState state,
                int row,
                int column) {
            // calculate bar width...
            double space = 0.0;
            if (orientation == PlotOrientation.HORIZONTAL) {
                space = dataArea.getHeight();
            } else {
                space = dataArea.getWidth();
            }
            double barW0 = domainAxis.getCategoryStart(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge());
            int seriesCount = state.getVisibleSeriesCount() >= 0
                    ? state.getVisibleSeriesCount() : getRowCount();
            int categoryCount = getColumnCount();
            /*if (seriesCount > 1) {
             double seriesGap = space * getItemMargin()
             / (categoryCount * (seriesCount - 1));
             double seriesW = calculateSeriesWidth(space, domainAxis,
             categoryCount, seriesCount);
             barW0 = barW0 + row * (seriesW + seriesGap)
             + (seriesW / 2.0) - (state.getBarWidth() / 2.0);
             }*/
            //else {
            barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge()) - state.getBarWidth()
                    / 2.0;
            //}
            return barW0;
        }
    }

    public class rendSingleBar extends LevelRenderer {

        @Override
        protected void calculateItemWidth(CategoryPlot plot, Rectangle2D dataArea, int rendererIndex, CategoryItemRendererState state) {

            CategoryAxis domainAxis = getDomainAxis(plot, rendererIndex);
            CategoryDataset dataset = plot.getDataset(rendererIndex);
            if (dataset != null) {
                int columns = dataset.getColumnCount();
                int rows = state.getVisibleSeriesCount() >= 0
                        ? state.getVisibleSeriesCount() : dataset.getRowCount();
                double space = 0.0;
                PlotOrientation orientation = plot.getOrientation();
                if (orientation == PlotOrientation.HORIZONTAL) {
                    space = dataArea.getHeight();
                } else if (orientation == PlotOrientation.VERTICAL) {
                    space = dataArea.getWidth();
                }
                double maxWidth = space * getMaximumItemWidth();
                double categoryMargin = 0.0;
                double currentItemMargin = 0.0;
                if (columns > 1) {
                    categoryMargin = domainAxis.getCategoryMargin();
                }
                if (rows > 1) {
                    currentItemMargin = getItemMargin();
                }
                double used = space * (1 - domainAxis.getLowerMargin()
                        - domainAxis.getUpperMargin()
                        - categoryMargin - currentItemMargin);
                if ((rows * columns) > 0) {
                    state.setBarWidth(/*Math.min(used / (rows  * columns), */maxWidth/*)*/);
                } else {
                    state.setBarWidth(/*Math.min(used,*/maxWidth/*)*/);
                }
            }
        }

        @Override
        protected double calculateBarW0(CategoryPlot plot, PlotOrientation orientation, Rectangle2D dataArea, CategoryAxis domainAxis, CategoryItemRendererState state, int row, int column) {
            // calculate bar width...
            double space = 0.0;
            if (orientation == PlotOrientation.HORIZONTAL) {
                space = dataArea.getHeight();
            } else {
                space = dataArea.getWidth();
            }
            double barW0 = domainAxis.getCategoryStart(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge());
            int seriesCount = state.getVisibleSeriesCount();
            if (seriesCount < 0) {
                seriesCount = getRowCount();
            }
            int categoryCount = getColumnCount();
            /*if (seriesCount > 1) {
             double seriesGap = space * getItemMargin()
             / (categoryCount * (seriesCount - 1));
             double seriesW = calculateSeriesWidth(space, domainAxis,
             categoryCount, seriesCount);
             barW0 = barW0 + row * (seriesW + seriesGap)
             + (seriesW / 2.0) - (state.getBarWidth() / 2.0);
             }
             else {*/
            barW0 = domainAxis.getCategoryMiddle(column, getColumnCount(),
                    dataArea, plot.getDomainAxisEdge()) - state.getBarWidth()
                    / 2.0;
            //}



            return barW0;
        }
    }

    /*
     class SwingWork1 extends SwingWorker<JFreeChart, Object> {

     I_AnswerReportContainer container;

     public SwingWork1(I_AnswerReportContainer container) {
     this.container = container;
     }

     @Override
     protected JFreeChart doInBackground() throws Exception {



     //先ずはデータをこしらえる

     Integer jobs = container.getCountJob();
     Integer machines = container.getCountMachine();
     List<Job> joblist = container.getJobList();
     //List<Order> orderlist = container.getOrderList();
     List<Machine> machineList = container.getMachineList();

     String[] jobSeries = new String[jobs];
     String[] machineCategorys = new String[machines];


     double[][] ends = new double[jobs][machines];
     double[][] starts = new double[jobs][machines];

     //値の設定とレンジの決定

     for (int i = 0; i < jobs; i++) {


     for (int j = 0; j < machines; j++) {

     starts[i][j] = container.getStarttimeInteger(joblist.get(i), machineList.get(j));
     ends[i][j] = container.getEndtimeInteger(joblist.get(i), machineList.get(j));





     }

     }

     //レンジの決定。リードタイムより若干多めの値をとる。
     double range;
     double rangeBase = container.getReadTime();

     double plusseryInset = (double) rangeBase / 20;
     if (rangeBase - Math.floor(plusseryInset) != 0) {
     range = rangeBase + Math.ceil(plusseryInset);
     } else {
     range = rangeBase + plusseryInset;
     }


     //カテゴリ（縦軸）の決定。もちろんマシン


     for (int i = 0; i < machines; i++) {
     machineCategorys[i] = machineList.get(i).getViewName();
     }

     //タスクの決定。ジョブ。
     for (int i = 0; i < jobs; i++) {
     jobSeries[i] = joblist.get(i).getViewName();
     }




     DefaultIntervalCategoryDataset data = new DefaultIntervalCategoryDataset(starts, ends);
     data.setCategoryKeys(machineCategorys);
     data.setSeriesKeys(jobSeries);

     //次に納期の格納。別データに格納
     DefaultCategoryDataset data2 = new DefaultCategoryDataset();

     for (int i = 0; i < jobs; i++) {

     int deliver = joblist.get(i).getTheTimeOfDelivery();
     //machineCategorysはマジックナンバーで、統一されていればなんでもよい
     data2.addValue(deliver, jobSeries[i], machineCategorys[0]);


     }






     String name = "aa";
     //ここまででこさえるべきデータはそろった。ここからはグラフの生成

     JFreeChart chart = createGraph(data, data2, range, name);
     return chart;
     }

     @Override
     protected void done() {
            
     plotwindow.repaintPanel();
            
     }
     }
     */
    private class LabelGeneCustom extends StandardCategoryItemLabelGenerator {

        private String labelFormat;
        /**
         * The string used to represent a null value.
         */
        private String nullValueString;
        /**
         * A number formatter used to preformat the value before it is passed to
         * the MessageFormat object.
         */
        private NumberFormat numberFormat;
        /**
         * A date formatter used to preformat the value before it is passed to
         * the MessageFormat object.
         */
        private DateFormat dateFormat;
        /**
         * A number formatter used to preformat the percentage value before it
         * is passed to the MessageFormat object.
         */
        private NumberFormat percentFormat;

        
        public LabelGeneCustom(){
            this(DEFAULT_LABEL_FORMAT_STRING,NumberFormat.getInstance());
        }
        
        protected LabelGeneCustom(String labelFormat,
                NumberFormat formatter) {
            this(labelFormat, formatter, NumberFormat.getPercentInstance());
        }

        /**
         * Creates a label generator with the specified number formatter.
         *
         * @param labelFormat the label format string (<code>null</code> not
         * permitted).
         * @param formatter the number formatter (<code>null</code> not
         * permitted).
         * @param percentFormatter the percent formatter (<code>null</code> not
         * permitted).
         *
         * @since 1.0.2
         */
        protected LabelGeneCustom(String labelFormat,
                NumberFormat formatter, NumberFormat percentFormatter) {
            ParamChecks.nullNotPermitted(labelFormat, "labelFormat");
            ParamChecks.nullNotPermitted(formatter, "formatter");
            ParamChecks.nullNotPermitted(percentFormatter, "percentFormatter");
            this.labelFormat = labelFormat;
            this.numberFormat = formatter;
            this.percentFormat = percentFormatter;
            this.dateFormat = null;
            this.nullValueString = "-";
        }

        @Override
        protected String generateLabelString(CategoryDataset dataset,
                int row, int column) {
            ParamChecks.nullNotPermitted(dataset, "dataset");
            String result;
            Object[] items = createItemArray(dataset, row, column);
            //result = MessageFormat.format(this.labelFormat, items);
            result = dataset.getRowKey(row).toString();
            return result;

        }
    }
}
