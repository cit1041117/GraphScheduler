/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentationModels;

import graph_indicate_Make.PloterModel;
import OutputMaker.MakeSheat;
import dataArchive.JobsContainer;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import result.newpackage.I_AnswerReportContainer;
import schejulingModel.I_SchejulingModel;
import schejulingModel.SchejulingModel;
import schejulingModel.Solutions;
import valuationUnit.DateValuation;

/**
 *
 * @author 117
 */
public class PresentationModelCore {

    private final I_SchejulingModel schejulingModel;
    
    private JobsContainer container;
    private I_AnswerReportContainer answerContainer;
    private PloterModel plotEngine;
    private AnswerButtonModelController answerButton;
    private SolutionPlugController plugController;
    private MakeSheat outputMaker;
    
    private Solutions solution;
    private boolean isWantRewritedAnswer;
    private boolean ableAnswer;
    private boolean canNextSolution;
    private boolean storeAnswer;

    public void setPlugController(SolutionPlugController plugController) {
        this.plugController = plugController;
    }
    


    public void setAnswerButton(AnswerButtonModelController answerButton) {
        this.answerButton = answerButton;
    }

    public PresentationModelCore() {
        

        this.schejulingModel = new SchejulingModel();

        ableAnswer = false;
        isWantRewritedAnswer = false;
        canNextSolution = true;
        storeAnswer = false;

        solution = null;
        container = null;

    }

    public Solutions[] getSolutions() {
        return schejulingModel.getSolutionEnum();
    }

    public boolean isAbleAnswer() {
        if (container != null && solution != null) {
            ableAnswer = true;
        }


        return ableAnswer;
    }

    public boolean isWantReAnswer() {
        return isWantRewritedAnswer;
    }
    
    public boolean iscanNextSolution(){
        return canNextSolution;
    }
    
    public boolean isstoreAnswer(){
        return storeAnswer;
    }

    public void setPlotEngine(PloterModel plotEngine) {
        this.plotEngine = plotEngine;
    }

    public void setJobContainer(JobsContainer ancontainer) {
        this.container = ancontainer;
        isWantRewritedAnswer = true;
        plugController.enableComboBox();
        answerButton.indicate();

    }

    public void setSolutions(Solutions ansolution) {
        this.solution = ansolution;
        isWantRewritedAnswer = true;
        answerButton.indicate();
    }
    
    public void setSolutions(){
        isWantRewritedAnswer = false;
        answerButton.indicate();
    }

    public boolean isSolutionable(Solutions solution) {
        return 
                schejulingModel.checkAbleSolution(solution, container);

    }
    
    boolean Explession(File file){
        this.outputMaker = new MakeSheat();
        canNextSolution = false;
        answerButton.indicate();
        answerButton.setStateLabelBusy(true);
        /*
        outputMaker.Create(file, answerContainer,plotEngine.explessChart());
               
        canNextSolution = true; 
            answerButton.indicate();
          
          */
          
        SwingWorkExpression work = new SwingWorkExpression(file);
        work.execute();
        
        return true;
    }

    void  Excecute() {
        
        canNextSolution = false;
        isWantRewritedAnswer = false;
        answerButton.indicate();
        answerButton.setStateLabelBusy(true);
        
        
        SwingWorkExcecute work = new SwingWorkExcecute();
        work.execute();
        
        
    }
    
    class SwingWorkExcecute extends SwingWorker<Object,Object> {
        

        
        

        @Override
        protected Boolean doInBackground() throws Exception {
            answerContainer =  schejulingModel.Excecute(container, solution);
            plotEngine.makePanelFactor(answerContainer,new DateValuation(answerContainer));  
            return null;
            
            
        }

        @Override
        protected void done() {
            plotEngine.draw();
            canNextSolution = true; 
            storeAnswer = true;
            
            
            answerButton.indicate();
            answerButton.setStateLabelBusy(false);
        }
        
        
    }
    
    class SwingWorkExpression extends SwingWorker<Boolean, Object>{
        private File file;
        private String falseReason;
        
        
        public SwingWorkExpression(File afile) {
            this.file = afile;
            
        }
        

        @Override
        protected Boolean doInBackground() throws Exception {
            
            Boolean bool = 
             outputMaker.Create(file, answerContainer,plotEngine.chartExpless());
            falseReason = outputMaker.getFalseReason();
            
            return bool;
        }

        @Override
        protected void done() {
            try {
                answerButton.optionAppear(falseReason, get());
            } catch (InterruptedException ex) {
                Logger.getLogger(PresentationModelCore.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(PresentationModelCore.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            canNextSolution = true; 
            answerButton.indicate();
            answerButton.setStateLabelBusy(false);
        }
        
        
        
    }
   
}
