/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentationModels;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import schejulingModel.Solutions;

/**
 *
 * @author 117
 */
public class SolutionPlugController {
    PresentationModelCore modelcore;
    JTextArea textArea;
    JComboBox<Solutions> comboBox;

    public SolutionPlugController(PresentationModelCore modelcore, JTextArea textArea,JComboBox<Solutions> comboBox) {
        this.modelcore = modelcore;
        this.textArea = textArea;
        this.comboBox = comboBox;
    }
    
    
    
    
        public Solutions[] getSolutions(){
        return modelcore.getSolutions();
    };
    
    protected void enableComboBox(){
        comboBox.setEnabled(true);
    }
        
    private void setSolutions(Solutions anSolutions){
        modelcore.setSolutions(anSolutions);
    };
    
        public void indicateJadge(Solutions jadged){
        if(modelcore.isSolutionable(jadged)){
            textArea.setText(jadged.getExplain());
            setSolutions(jadged);
        }else{
            textArea.setText("その解法は利用できません");
            modelcore.setSolutions();
        }
            
        
        
    }
    
    
    
    
    
}
