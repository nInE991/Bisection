package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
//         final BigDecimal e = new BigDecimal(
//                "-1");
//
//        BigDecimal remainderOf2 = e.remainder(BigDecimal.ONE);
//        System.out.println(remainderOf2);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Bisection method");
        primaryStage.setScene(new Scene(root, 381, 495));
        primaryStage.show();

    }
}
