package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import presenter.CalculatorPresenter;

import java.util.HashMap;

public class CalculatorView extends BorderPane {

    private final int STD_PADDING = 30;
    private final int MAX_GRID_LINES = 5;
    private final int MAX_GRID_ROWS = 4;
    private final double STD_TEXT_HEIGHT = 50;
    private final double SZ_GAP = 5;
    private final double SZ_FONTS = 20;

    private TextField textExpression;
    private Label statusLabel;
    private HashMap<String, Button> mapOfButtons;
    public CalculatorView() {
        doLayout();
        getStylesheets().add("appStyleDark.css");
    }

    public void bindEvents(CalculatorPresenter presenter){
        this.mapOfButtons.get("=").setOnAction(event -> presenter.doEvaluate());
    }
    public String getExpression(){
        return this.textExpression.getText();
    }

    public void setExpressionResult(double computationResult){
        this.textExpression.setText(String.valueOf(computationResult));
    }

    public void showError(String errorMsg){
        this.statusLabel.setText(errorMsg);
    }

    public void clearError(){
        this.statusLabel.setText("Idle...");
    }

    private void doLayout(){

        textExpression = new TextField();
        textExpression.setPrefHeight(STD_TEXT_HEIGHT);
        textExpression.setFont(Font.font(SZ_FONTS));
        setTop(textExpression);
        //Aqui criamos o Layout deste painel
        setPadding(new Insets(STD_PADDING));

        //Layout TOP - caixa de texto para o calculo
        String[] buttonLabels = {"(", ")", "<<", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="};
        
        //Layout Bottom - simplesmente para uma mensagem de estado
        this.statusLabel = new Label("Idle...");
        setBottom(statusLabel);
        //Layout Center - Gridpane ou tabela com botoes que compôem a callculadora

        this.mapOfButtons = new HashMap<>();
        for (String label: buttonLabels) {
            mapOfButtons.put(label, new Button(label));
            mapOfButtons.get(label).setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            mapOfButtons.get(label).setOnAction(event -> addToExpression(label));
        }
        // Mudando o comportamento do botão "backspace" "<<"
        mapOfButtons.get("<<").setOnAction(event -> undoFromExpression());

        GridPane keyBoardGrid = new GridPane();
        int i = 0;
        for (int j = 0; j < MAX_GRID_LINES; j++){
            for (int k = 0; k < MAX_GRID_ROWS; k++){
                keyBoardGrid.add(mapOfButtons.get(buttonLabels[i]), k, j);
                i++;
            }
        }
        //keyBoardGrid.setPadding(new Insets(SZ_GAP,SZ_GAP,SZ_GAP,SZ_GAP));
        keyBoardGrid.setHgap(SZ_GAP);
        keyBoardGrid.setVgap(SZ_GAP);
        setCenter(keyBoardGrid);
        // Fazer com que os botoes preencham os espaços

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);
        keyBoardGrid.getColumnConstraints().addAll(cc,cc,cc,cc);

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        rc.setFillHeight(true);
        keyBoardGrid.getRowConstraints().addAll(rc,rc,rc,rc,rc);

        mapOfButtons.get("+/-").setDisable(true);
        mapOfButtons.get("=").getStyleClass().add("myEqual");
    }

    private void addToExpression(String newString){
        String actualString = this.textExpression.getText();
        actualString += newString;
        this.textExpression.setText(actualString);
    }

    private void undoFromExpression(){
        String actualString = this.textExpression.getText();
        if (actualString.isBlank()) return;

        String undoneString = actualString.substring(0,actualString.length()-1);
        this.textExpression.setText(undoneString);
    }


}