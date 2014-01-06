/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication32;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;


/**
 *
 * @author 117
 */
public class PlotPanel extends javax.swing.JPanel {

    private JFreeChart jFreeChart;
    ChartPanel graphPanel;
    JSplitPane contentPanel;
    JPanel answeredPanel;
    JButton infoAuthorButton;
    JPanel infoAuthor; 

    /**
     * Creates new form PlotPanel
     */
    public PlotPanel() {
        initComponents();


        graphPanel = new ChartPanel(null);
        
        JPanel first = new JPanel(new BorderLayout());
        
        JPanel doUse = new JPanel();
        doUse.setLayout(new BoxLayout(doUse, BoxLayout.Y_AXIS));
        
        
        JLabel testLabel = new JLabel("スケジューリング問題を解き、図示化するアプリケーションです。", JLabel.CENTER);
        
        
        
        JLabel testLabel2 = new JLabel("使い方");
        Font testemp = testLabel2.getFont();
        testLabel2.setFont(new Font(testemp.getFontName(),Font.BOLD,testemp.getSize()));
        JLabel testLabel3 = new JLabel("①左上の'Open Data'より入力するデータを選択する。");
        JLabel testLabel4 = new JLabel("②右上の'解法選択'から解法を選ぶ。");
        JLabel testLabel5 = new JLabel("③もし解く事ができれば'EXCECUTE'ボタンが押せるようになるので押す。");
        JLabel testLabel6 = new JLabel("④グラフが出力される。");
        JLabel testLabel7 = new JLabel("⑤もしExcelで出力したいなら'Excelで出力'をクリック。");
        
        doUse.add(testLabel);
        doUse.add(testLabel2);
        doUse.add(testLabel3);
        doUse.add(testLabel4);
        doUse.add(testLabel5);
        doUse.add(testLabel6);
        doUse.add(testLabel7);
        
        
        first.add(doUse, BorderLayout.CENTER);
        
        

        JPanel  portPanel = new JPanel();
        
        
        infoAuthor = new JPanel();
        infoAuthor.setLayout(new BoxLayout(infoAuthor, BoxLayout.Y_AXIS));
        
        
        JLabel label1 = new JLabel("YSGraphScheduler");
        JLabel label2 = new JLabel("開発");
        Font fonta = label2.getFont();
        label2.setFont(new Font(fonta.getFontName(),Font.BOLD,fonta.getSize()));
        JLabel label3 = new JLabel("千葉工業大学");
        JLabel label4 = new JLabel("社会システム科学部 経営情報科学科");
        JLabel label5 = new JLabel("白井研究室 所属");
        JLabel label6 = new JLabel("1041117 山田 沙見");
        JLabel label7 = new JLabel("支えてくださった全ての人に感謝します.");
        
        
        infoAuthor.add(label1);
        infoAuthor.add(label2);
        infoAuthor.add(label3);
        infoAuthor.add(label4);
        infoAuthor.add(label5);
        infoAuthor.add(label6);
        infoAuthor.add(label7);
        
        
        
        
        infoAuthorButton = new JButton("クレジット");
        
        infoAuthorButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeActionPerformed(evt);
            }

            private void storeActionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(infoAuthorButton, infoAuthor, "クレジット", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        
        
        portPanel.add(Box.createHorizontalStrut(600));
        portPanel.add(infoAuthorButton);
        
        
        first.add(portPanel, BorderLayout.SOUTH);
        

        jScrollPane1.setViewportView(first);
        
        graphPanel.setMaximumSize(getSize());

    }
    
    public void setGlaph(JFreeChart achart){
        jFreeChart = achart;
    }
    
    public void setPanel(JPanel apanel){
        answeredPanel = apanel;
    }

    public void repaintPanel() {
        //jFreeChart = achart;

        /*
        SwingWork work = new SwingWork(achart);
        work.execute();
        */
        
        //下のSwingWorkerは現在使ってません。
         
        
        
        

       graphPanel = new ChartPanel(jFreeChart);
       
       /*
       JPanel graphPanelBase = new JPanel(new BorderLayout());
       graphPanelBase.add(graphPanel);
       */
       
       
       answeredPanel.setVisible(true);
       graphPanel.setVisible(true);
       
       
       JScrollPane leftScrollPane = new JScrollPane();
       JScrollPane rightScrollPane = new JScrollPane();
       
       
       
       leftScrollPane.setViewportView(graphPanel);
       rightScrollPane.setViewportView(answeredPanel);
       
       
       rightScrollPane.setMaximumSize(new Dimension(200, 300));
       rightScrollPane.setPreferredSize(new Dimension(200, 300));
       
       
       contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftScrollPane,rightScrollPane);
       
       
       
       contentPanel.setPreferredSize(null);
       contentPanel.setDividerSize(4);
       
       
       
       contentPanel.setDividerLocation(0.95);
       //contentPanel.setDividerLocation(600);
       
       contentPanel.setResizeWeight(0.95);
       
       SwingUtilities.invokeLater(new Runnable() {

       
       
       
           
            @Override
            public void run() {
                
               jScrollPane1.setViewportView(contentPanel);
               
                repaint();
            }
        });
       
        









        //this.setVisible(true);
       

    }

    class SwingWork extends SwingWorker<Object, Object> {

        JFreeChart chart;

        public SwingWork(JFreeChart chart) {
            this.chart = chart;
        }

        @Override
        protected Object doInBackground() throws Exception {
            graphPanel = new ChartPanel(jFreeChart);

            jScrollPane1.setViewportView(contentPanel);
            

            return null;
        }

        @Override
        protected void done() {
            repaint();
        }
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setPreferredSize(new java.awt.Dimension(800, 300));
        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
