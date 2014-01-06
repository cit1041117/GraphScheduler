/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graph_indicate_Make;

import dataArchive.Job;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;
import javaapplication32.PlotPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import result.newpackage.AnswerReportContainer;
import result.newpackage.I_AnswerReportContainer;
import valuationUnit.DateValuation;

/**
 *
 * @author shirai-Lab
 */
public class InfoPanelPloter {
    private PlotPanel plotwindow;
    private JPanel storePanel;
    private int plotterWidth = 200;

    public InfoPanelPloter(PlotPanel plotPanel) {
        this.plotwindow = plotPanel;
    }
    
    public boolean drawPanel(I_AnswerReportContainer container, DateValuation valuationer){
        
        storePanel = createPanel();
        
        JPanel infoValuePanel = createValuePanel("情報");
        
        JPanel allendPanel = createValuePanel("最大完了時間");
        
        
        
        //ラベルの作成
        
        String[] titles = {"最大納期ずれ時間＝","平均納期ずれ時間＝","最大納期遅れ時間＝","平均納期遅れ時間＝"};
        
       
        JLabel maxLateTitle = createTitleLabel(titles[2]);
        JLabel averageLateTitle = createTitleLabel(titles[3]);
        
        JLabel maxGapTitle  = createTitleLabel(titles[0]);
        JLabel averageGapTitle = createTitleLabel(titles[1]);
        
        
        double maxLateValue = valuationer.maxDeliveryLate();
        double averageLateValue = valuationer.averageDeliveryLate();
        double maxGapValue = valuationer.maxDeliveryGap();
        double averageGapValue = valuationer.averageDeliveryGap();
        
        
        
        JLabel maxLateLabel = createValueLabel(maxLateValue);
        JLabel averageLateLabel = createValueLabel(averageLateValue);
        JLabel maxGapLabel = createValueLabel(maxGapValue);
        JLabel averageGapLabel = createValueLabel(averageGapValue);
        
        
        
        
        
        //追加
        
        
        //最大納期遅れ時間
        infoValuePanel.add(maxLateTitle);
        infoValuePanel.add(maxLateLabel);
        
        //平均納期遅れ時間
        infoValuePanel.add(averageLateTitle);
        infoValuePanel.add(averageLateLabel);
        
        //最大納期ずれ時間
        infoValuePanel.add(maxGapTitle);
        infoValuePanel.add(maxGapLabel);
        
        //平均納期ずれ時間
        infoValuePanel.add(averageGapTitle);
        infoValuePanel.add(averageGapLabel);
       
        
        
        
        
        //最大完了時間
        JLabel allReadTimeLabel = createValueLabel(container.getReadTime());
        allendPanel.add(allReadTimeLabel);
        
        
        
        //全体パネルへの追加。
        storePanel.add(allendPanel);
        storePanel.add(infoValuePanel);
        
        
        
        //ジョブ毎の情報
        for (int i = 0; i < container.getCountJob(); i++) {
            List<Job> joblist = container.getJobList();
            Job job = joblist.get(i);
            JPanel jobBoard = createValuePanel(job.getViewName() + "の結果");
            
            //リードタイム
            JLabel readTimeTitle = createTitleLabel("リードタイム");
            JLabel readTimeValue = createValueLabel(container.getJobReadtime(job));
            
            jobBoard.add(readTimeTitle);
            jobBoard.add(readTimeValue);
            
            //納期
            JLabel limitTitle = createTitleLabel("納期");
            JLabel limitValue = createValueLabel(job.getTheTimeOfDelivery());
            
            jobBoard.add(limitTitle);
            jobBoard.add(limitValue);
            
            
            storePanel.add(jobBoard);
            
        }
        
        
        
        
        
        
        
        
        
        
        
        //storePanel.setPreferredSize(new Dimension(200, 300));
        
        
        
        
        return true;
    }
    
    private JPanel createPanel(){
        JPanel temp = new JPanel();
        
        BoxLayout layout = new BoxLayout(temp, BoxLayout.Y_AXIS);
        temp.setLayout(layout);
        temp.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        
        
        return temp;
        
    }
    
    private JPanel createValuePanel(String title){
        JPanel temp = new JPanel();
        
        
        
        temp.setBorder(new TitledBorder(title));
        
        BoxLayout layout = new BoxLayout(temp, BoxLayout.Y_AXIS);
        temp.setLayout(layout);
        
        temp.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        
        
        
        //temp.setPreferredSize(new Dimension(plotterWidth, 100));
        //temp.add(        Box.createHorizontalStrut(plotterWidth));
        
        //temp.setMaximumSize(new Dimension(plotterWidth,1));
        
        
        
        return temp;
    }
    
    private JLabel createTitleLabel(String title){
        JLabel temp = new JLabel(title);
        //temp.setPreferredSize(new Dimension(plotterWidth, 10));
        temp.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        Dimension defaultsize = temp.getPreferredSize();
        
        
        Font font = temp.getFont();
        temp.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        
         
        temp.setMaximumSize(new Dimension(plotterWidth, defaultsize.height));
        
        return temp;
    }
    
    private JLabel createValueLabel(double value){
        JLabel temp = new JLabel(Double.toString(value));
        //temp.setPreferredSize(new Dimension(plotterWidth, 10));
        temp.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        
        Dimension defaultsize = temp.getPreferredSize();
        
        
        
        temp.setMaximumSize(new Dimension(plotterWidth, defaultsize.height));
        
        
        
        return temp;
    }
    
    public void setPanel(){
        this.plotwindow.setPanel(storePanel);
    }
    
    
    
}
