package model;

import model.ExpressionException;

import java.util.*;

public class Calculator {
    public Calculator() {
    }

    public double evaluateExpression(String infixExpression) throws ExpressionException {
        // converting string to postfix notation
        String postfixExpression = infix2postfix(infixExpression);

        // split into tokens (e.g. "4.4 5 + 6 2 1 - * 3 / -")
        String[] tokens = postfixExpression.split(" ");

        // parse expression catching any thrown exception
        try{
            Stack<Double> computeStack = new Stack<>();
            for (String token : tokens) {
                if(isNumber(token)){ // token is an operand
                    double number = Double.parseDouble(token);
                    computeStack.push(number);
                }else{ // token is an operator
                    double numA = computeStack.pop();
                    double numB = computeStack.pop();
                    computeStack.push(operation(numA, numB, token));
                }
                
            }
            if(computeStack.size() != 1) throw new ExpressionException("Invalid Expression!");

            return computeStack.pop(); // final result

        }catch (EmptyStackException msgESE){
            throw new ExpressionException("Invalid Expression!");
        }catch (ArithmeticException msgAE){
            throw new ExpressionException("The expression results in division by zero.");
        }

    }
    public String infix2postfix(String infixExpr) { // TODO: change visibility to private
        // Sanitize expression before parsing
        infixExpr = sanitize(infixExpr);

        // Break expression into tokens
        String[] tokens = infixExpr.split(" ");

        // Instantiate auxiliary collections
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        // Shunting Yard Algorithm - Edsger Dijkstra
        for (String e : tokens) {
            if(isNumber(e)) {
                postfix.add(e);
            } else if(isLeftParenthesis(e)) {
                stack.push(e);
            } else if(isRightParenthesis(e)) {
                while (!stack.isEmpty() && !isLeftParenthesis(stack.peek())) {
                    postfix.add(stack.pop());
                }
                stack.pop();  // Discard the "("
            } else { // isOperator
                while ( !stack.isEmpty() && precedence(e) <= precedence(stack.peek()) ) {
                    postfix.add(stack.pop());
                }
                stack.push(e);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        StringBuilder postfixExpr = new StringBuilder();
        for (String token : postfix) {
            postfixExpr.append( token + " " );
        }

        return postfixExpr.toString().trim();
    }

    private double operation(double firstOperand, double secondOperand, String operator) {
        switch (operator) {
            case "/": if(secondOperand == 0) throw new ArithmeticException();
                return firstOperand / secondOperand;
            case "*": return firstOperand * secondOperand;
            case "+": return firstOperand + secondOperand;
            case "-": return firstOperand - secondOperand;
            default: return 0;
        }
    }
    private String sanitize(String expr) {
        // Sanitizes an expression. Makes it so every token has a space around it.
        String sanitized = "";
        for(int i=0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if(Character.isDigit(c) || c == '.') {
                sanitized += c;
            } else {
                sanitized += " " + c + " "; // possibly excess white space will be removed below
            }
        }

        // returns the sanitized string, removing white space in excess, if present.
        // If we receive white space in excess, this will keep only one space between tokens
        return sanitized.trim().replaceAll(" +", " ");
    }

    private int precedence(String operator) {
        switch (operator) {
            case "/":
            case "*": return 2;
            case "+":
            case "-": return 1;
            default: return 0;
        }
    }

    private boolean isOperator(String token) {
        switch (token) {
            case "/":
            case "*":
            case "+":
            case "-": return true;
            default: return false;
        }
    }

    private boolean isLeftParenthesis(String token) {
        return token.equalsIgnoreCase("(");
    }

    private boolean isRightParenthesis(String token) {
        return token.equalsIgnoreCase(")");
    }

    private boolean isNumber(String token) {
        return !isOperator(token) && !isLeftParenthesis(token) && !isRightParenthesis(token);
    }
}
