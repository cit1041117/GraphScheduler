/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentationModels;

import java.io.File;
import javaapplication32.SettingPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author 117
 */
public class AnswerButtonAndstateLabelController implements AnswerButtonButtonControll ,AnswerButtonModelController,AnswerButtonFileIOController{
    PresentationModelCore modelCore;
    SettingPanel panel;
    JButton calledButton ;
    JButton fileoutButton;
    String state;

    public AnswerButtonAndstateLabelController(PresentationModelCore modelCore,SettingPanel panel) {
        this.modelCore = modelCore;
        this.panel = panel;
        this.calledButton = panel.getjButton2();
        this.fileoutButton = panel.getjButton3();
        setController();
    }
    
    private void setController(){
        modelCore.setAnswerButton(this);
    }
    
    
    
    @Override
    public void Excecute(){
        if(modelCore.isAbleAnswer() && modelCore.isWantReAnswer() && modelCore.iscanNextSolution()){
            calledButton.setEnabled(false);
            
            modelCore.Excecute();
            
            return;
        }
        
        assert false;
    }
    
    @Override
    public synchronized void indicate(){
        if (modelCore.isstoreAnswer() && modelCore.iscanNextSolution()) {
            fileoutButton.setEnabled(true);
            
            
            
        }else{
            fileoutButton.setEnabled(false);
        }
        
        
        if(modelCore.isAbleAnswer() && modelCore.isWantReAnswer() && modelCore.iscanNextSolution()){
            
            
            calledButton.setEnabled(true);
            return;
        }
        calledButton.setEnabled(false);
        
        
    }
    
    @Override
    public void setStateLabelBusy(Boolean bool){
        JLabel label = panel.getjLabel2();
        
        if(bool){
            state = label.getText();
            label.setText("処理中です。");
        }else{
            label.setText(state);
        }
        
    }
    
    @Override
    public void Expression(File file){
        //ボタンの状態
        if (modelCore.isstoreAnswer() && modelCore.iscanNextSolution()){
            
                modelCore.Explession(file);
            
            return;
        }
        
        assert false;
        
    }
    
    @Override
    public void optionAppear(String error,Boolean bool){
        panel.apearOptionWindow(error, bool);
    }
    
    
    
}
