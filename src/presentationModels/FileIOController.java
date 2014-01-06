/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentationModels;

import FileChangeModel.FileChanger;
import dataArchive.JobsContainer;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication32.SettingPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author 117
 */
public class FileIOController {

    private final PresentationModelCore modelCore;
    private String stateString;
    private FileChanger fileChanger;
    private final JLabel fileInfoLabel;
    private final JLabel fileIOLabel;
    //private JButton ExcecuteButton;
    private File datafile;
    private final SettingPanel panelwindow;
    private final JButton fileButton ;
    
    

    /**
     *
     * @param modelCore
     * @param fileinfoLabel
     */
    public FileIOController(PresentationModelCore modelCore,SettingPanel panel) {
        this.modelCore = modelCore;
        
        this.panelwindow = panel;
        this.fileInfoLabel = panelwindow.getjLabel2();
        this.fileIOLabel = panelwindow.getjLabel1();
        //this.ExcecuteButton = excecuteButton;
        this.fileButton = panelwindow.getjButton1();



    }
    /*
     private void resetingButton(){
     SwingUtilities.invokeLater(new Runnable() {
     @Override
     public void run() {
     ActivateButton(Boolean.FALSE);
     }
     });
     }*/
    
     private void fileSelectTrigger() {
     try {
            stateString = "判定中";
           
            if (fileChanger.fileChangeResearch(datafile)) {
                stateString = fileChanger.getStateString();
                
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        changeLabel(Color.BLACK, null);
                        //ActivateButton(Boolean.TRUE);
                        changeFileLabel(Color.BLACK,null);
                        
                    }
                });
                
                this.setMain(fileChanger.fileChange());
            } else {
                stateString = fileChanger.getStateString();
                
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        changeLabel(Color.RED, null);
                        changeFileLabel(Color.BLACK,null);
                    }
                });



            }
            
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileIOController.class.getName()).log(Level.SEVERE, null, ex);
            changeFileLabel(Color.PINK, "FileNotFoundException");
            changeLabel(Color.BLACK, "???");
        } catch (IOException ex) {
            Logger.getLogger(FileIOController.class.getName()).log(Level.SEVERE, null, ex);
            changeFileLabel(Color.PINK, "IOException");
            changeLabel(Color.BLACK, "???");
        }


     }
     

    public void fileChange(File file) {
        this.datafile = file;
        
        this.fileChanger = new FileChanger();
        /*Thread thread = new Thre5ad(this.runner);
        thread.start();*/
        fileSelectTrigger();
    }

    /*    
     public void setFile(File file){
     this.datafile = file;
     }
     */
    private void changeFileLabel(Color color, String comment) {
        fileIOLabel.setForeground(color);
        if(comment == null){
            return;
        }
                fileIOLabel.setText(comment);
        
    }

    private void changeLabel(Color color, String comment) {
        fileInfoLabel.setForeground(color);
        if (comment == null) {
            fileInfoLabel.setText(stateString);
            System.out.println("yeu");
           
            return;
        }
        fileInfoLabel.setText(comment);
        System.out.println("yes");
        
    }

    /*private void ActivateButton(Boolean on){
     ExcecuteButton.setEnabled(on);
        
     }
     */
    private void setMain(JobsContainer fileChange) {
        modelCore.setJobContainer(fileChange);

    }

    
    
    
}
