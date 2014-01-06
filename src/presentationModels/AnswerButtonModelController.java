/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentationModels;

/**
 *
 * @author 117
 */
public interface AnswerButtonModelController {

    public void indicate();

    public void optionAppear(String error, Boolean bool);
    
    public void setStateLabelBusy(Boolean bool);
}
