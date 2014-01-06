/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph_indicate_Make;

import javaapplication32.PlotPanel;
import org.jfree.chart.JFreeChart;
import result.newpackage.AnswerReportContainer;
import result.newpackage.I_AnswerReportContainer;
import valuationUnit.DateValuation;

/**
 *
 * @author shirai-Lab
 */
public class PloterModel {

    GraphPloter graphPloter;
    InfoPanelPloter infoPanelPloter;
    PlotPanel plotPanel;
    
    

    public PloterModel(PlotPanel aplotPanel) {
        this.plotPanel = aplotPanel;
        this.graphPloter =  new GraphPloter(plotPanel);
        this.infoPanelPloter = new InfoPanelPloter(plotPanel);
    }
    
    public JFreeChart chartExpless(){
        return graphPloter.explessChart();
    }
    
    
    public boolean makePanelFactor(I_AnswerReportContainer container, DateValuation valuation){
        
        //パネルの要素を作って追加する
        
        
        //要素をここまでで作る
        if(!graphPloter.drawgraph(container)){
            return false;
        }
        
        if(!infoPanelPloter.drawPanel(container, valuation)){
            return false;
        }
        
        
        
        
        
        //要素をここでセットする。パネルはそれぞれが持っている。
        graphPloter.setGraph();
        infoPanelPloter.setPanel();
        
        return true;
        
    }
    
    public boolean draw(){
        //ペイント
        plotPanel.repaintPanel();
        
        return true;
    }
    
}
