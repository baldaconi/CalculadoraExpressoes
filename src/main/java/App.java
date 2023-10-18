import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Calculator;
import presenter.CalculatorPresenter;
import view.CalculatorView;

public class App extends Application {

    private int H_RES = 500;
    private int V_RES = 600;
    public static void main(String[] args) {
        launch(args);

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Arranque da aplicação JAVAFX
        CalculatorView view = new CalculatorView();
        Calculator model = new Calculator();
        CalculatorPresenter presenter = new CalculatorPresenter(view, model);

        Scene scene = new Scene(view,H_RES,V_RES);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Advanced Calculator");
        primaryStage.show();
    }
}