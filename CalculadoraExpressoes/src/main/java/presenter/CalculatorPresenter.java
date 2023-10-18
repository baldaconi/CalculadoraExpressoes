package presenter;

import model.Calculator;
import model.ExpressionException;
import view.CalculatorView;

public class CalculatorPresenter {
    private CalculatorView viewOfCalculator;
    private Calculator modelOfCalculator;

    public CalculatorPresenter(CalculatorView viewOfCalculator,
                               Calculator modelOfCalculator){
        this.viewOfCalculator = viewOfCalculator;
        this.modelOfCalculator = modelOfCalculator;
        this.viewOfCalculator.bindEvents(this);
    }

    public void doEvaluate(){
        viewOfCalculator.clearError();
        String expression = viewOfCalculator.getExpression();
        if(expression.isBlank()){
            viewOfCalculator.showError("Empty Expression...");
            return;
        }
        try {
            double computationResult = modelOfCalculator.evaluateExpression(expression);
            viewOfCalculator.setExpressionResult(computationResult);
        }catch(ExpressionException msgEE){
            viewOfCalculator.showError(msgEE.getMessage());
        }
    }
}
