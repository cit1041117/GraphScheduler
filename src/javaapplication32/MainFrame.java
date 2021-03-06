/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication32;

import presentationModels.AnswerButtonButtonControll;
import presentationModels.AnswerButtonAndstateLabelController;
import presentationModels.AnswerButtonFileIOController;
import presentationModels.AnswerButtonModelController;
import presentationModels.FileIOController;
import graph_indicate_Make.GraphPloter;
import graph_indicate_Make.PloterModel;
import presentationModels.PresentationModelCore;
import presentationModels.SolutionPlugController;

/**
 *
 * @author 117
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel1 = new javaapplication32.MainPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("YSGraphSchejuler");

        mainPanel1.setName("GraphScheduler"); // NOI18N
        getContentPane().add(mainPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    private void instanceFactory(){
        //モデルをつくる
        //mainPanel1 = new MainPanel();
        
        setTitle("GraphScheduler");
        SettingPanel componentPanel = mainPanel1.getSettingPanel1();
        PlotPanel plotter = mainPanel1.getPlotPanel2();
        
        PresentationModelCore modelCore = new PresentationModelCore();
        FileIOController iOController = new FileIOController(modelCore,componentPanel);
        AnswerButtonAndstateLabelController answerButtonController = new AnswerButtonAndstateLabelController(modelCore, componentPanel);
        SolutionPlugController plugController = new SolutionPlugController(modelCore,componentPanel.getjTextArea1(),componentPanel.getComboBox());
        PloterModel graphPloter = new PloterModel(plotter);
        
        
        modelCore.setAnswerButton((AnswerButtonModelController)answerButtonController);
        modelCore.setPlotEngine(graphPloter);
        modelCore.setPlugController(plugController);
        
             //パネルにモデルをつめる
        componentPanel.setFileIOController(iOController);
        componentPanel.setAnswerButtonController((AnswerButtonButtonControll)answerButtonController);
        componentPanel.setFileOutButtonController((AnswerButtonFileIOController)answerButtonController);
        componentPanel.setSolutionPlug(plugController);
        
    }
    
   
        
    
    
    
    public static void main(String args[]) {
        
        
        
        
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        final MainFrame frame = new MainFrame();
        frame.instanceFactory();
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javaapplication32.MainPanel mainPanel1;
    // End of variables declaration//GEN-END:variables
    //private MainPanel newJPanel1;
}
