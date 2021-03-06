package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


public class Controller {
    static boolean err = false;
    List list;
    @FXML
    private TextField functionForm;
    @FXML
    private TextField leftEndForm;
    @FXML
    private TextField rightEndForm;
    @FXML
    private TextField tolForm;
    @FXML
    private TextField iterlimForm;
    @FXML
    private TextField timelimForm;
    @FXML
    private TextField resultXForm;
    @FXML
    private TextField resultFunctionXForm;
    @FXML
    private TextField resultAbsForm;
    @FXML
    private TextField resultIterForm;
    @FXML
    private TextField resultTimeForm;
    @FXML
    private ProgressIndicator progressIndicatorForm;
    @FXML
    private Label resultLabelForm;

    @FXML
    private void buttonSearch() {
        Task task = new Task() {
            @Override
            protected Object call() {
                try {
                    Bisection bisection = new Bisection();
                    bisection.startTime = System.currentTimeMillis();
                    if (functionForm.getText().isEmpty()) {
                        throw new Exception("Function empty !");
                    }
                    if (leftEndForm.getText().isEmpty()) {
                        throw new Exception("Left End empty !");
                    }
                    if (rightEndForm.getText().isEmpty()) {
                        throw new Exception("Right End empty !");
                    }
                    if (tolForm.getText().isEmpty()) {
                        throw new Exception("Tolerance empty !");
                    }
                    if (iterlimForm.getText().isEmpty()) {
                        throw new Exception("Iteration limit empty !");
                    }
                    if (timelimForm.getText().isEmpty()) {
                        throw new Exception("Time limit empty !");
                    }
                    if (new Integer(iterlimForm.getText()) <= 0) {
                        throw new Exception("Iteration can not be 0 or negative !");
                    }
                    if (new Double(timelimForm.getText()) <= 0) {
                        throw new Exception("Time can not be 0 or negative !");
                    }
                    progressIndicatorForm.setVisible(true);

                    bisection.function = functionForm.getText().toLowerCase();
                    bisection.a = new BigDecimal(Double.valueOf(leftEndForm.getText().replace(',','.')));
                    bisection.b = new BigDecimal(Double.valueOf(rightEndForm.getText().replace(',','.')));
                    bisection.tol = new BigDecimal(Double.valueOf(tolForm.getText().replace(',','.')));
                    bisection.iteration = Integer.valueOf(iterlimForm.getText());
                    bisection.itermax = Integer.valueOf(iterlimForm.getText());
                    bisection.timeLimit = Long.parseLong(timelimForm.getText());
                    bisection.timeMax = Long.parseLong(timelimForm.getText());
                    list = new Expression(bisection.function).getUsedVariables();
                    if(list.get(0)!=null){
                        bisection.perem = String.valueOf(list.get(0));
                    }
                    else{
                        throw new Exception("Error entering operator!");
                    }
                    bisection.functionA = new Expression(bisection.function).with(bisection.perem, bisection.a).eval();
                    bisection.functionB = new Expression(bisection.function).with(bisection.perem, bisection.b).eval();
                    if (bisection.functionA.signum() == bisection.functionB.signum()) {
                        throw new Exception("Sign of F(a) and F(b) must be opposite! Check end-points of the interval [a,b]!");
                    } else {
                        bisection.latch = new CountDownLatch(1);
                        while (bisection.a.subtract(bisection.b).abs().compareTo(bisection.tol) > 0 && bisection.cond == 0) {
                            if (bisection.latch.getCount() != 1) {
                                bisection.latch = new CountDownLatch(1);
                            }
                            bisection.Method();
                            if (bisection.iter >= bisection.iteration) {
                                Platform.runLater(() -> {
                                            bisection.StartPause();
                                            progressIndicatorForm.setVisible(false);
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Confirmation Dialog");
                                            alert.setHeaderText("The number of iterations is exceeded !");
                                            alert.setContentText("You want to add iteration ?");
                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK) {
                                                bisection.iteration = bisection.iteration + bisection.itermax;
                                                iterlimForm.setText(String.valueOf(bisection.iteration));
                                                progressIndicatorForm.setVisible(true);
                                            } else {
                                                bisection.cond = 1;
                                                resultLabelForm.setText("Solution not found, number of iterations exceeded!");
                                            }
                                            bisection.latch.countDown();
                                            bisection.StopPause();
                                        }
                                );
                                bisection.latch.await();
                            }
                            if ((System.currentTimeMillis() - bisection.startTime) >= bisection.timeLimit) {
                                Platform.runLater(() -> {
                                            bisection.StartPause();
                                            progressIndicatorForm.setVisible(false);
                                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                            alert.setTitle("Confirmation Dialog");
                                            alert.setHeaderText("The time exceeded !");
                                            alert.setContentText("You want to add time ?");
                                            Optional<ButtonType> result = alert.showAndWait();
                                            if (result.get() == ButtonType.OK) {
                                                bisection.timeLimit = bisection.timeLimit + bisection.timeMax;
                                                timelimForm.setText(String.valueOf(bisection.timeLimit));
                                                progressIndicatorForm.setVisible(true);
                                            } else {
                                                bisection.cond = 1;
                                                resultLabelForm.setText("Solution not found, time exceeded !");
                                            }
                                            bisection.latch.countDown();
                                            bisection.StopPause();
                                        }

                                );
                                bisection.latch.await();
                            }
                            Thread.sleep(15);
                        }
                    }

                    Platform.runLater(() -> {
                        if (bisection.cond == 0) {
                            resultLabelForm.setText("Solution found!");
                        }
                        bisection.resultTime = System.currentTimeMillis() - bisection.startTime;
                        resultXForm.setText(String.valueOf(bisection.aver));
                        resultFunctionXForm.setText(String.valueOf(bisection.functionAver.setScale(34, RoundingMode.UP)));
                        resultAbsForm.setText(String.valueOf(bisection.b.subtract(bisection.a).abs().setScale(34, RoundingMode.UP)));
                        resultIterForm.setText(String.valueOf(bisection.iter));
                        resultTimeForm.setText(String.valueOf(bisection.resultTime));
                        progressIndicatorForm.setVisible(false);
                    });
                } catch (Exception ex) {
                    try{
                    Platform.runLater(() -> {
                                resultXForm.setText("");
                                resultFunctionXForm.setText("");
                                resultAbsForm.setText("");
                                resultIterForm.setText("");
                                resultTimeForm.setText("");
                                progressIndicatorForm.setVisible(false);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error Dialog");
                                alert.setHeaderText("Error !!! ");
                                alert.setContentText(String.valueOf(ex.getMessage()));
                                alert.showAndWait();
                                resultLabelForm.setText(String.valueOf(ex.getMessage()));
                            });
                    }
                    catch (Exception err){

                    }
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        err = false;
        thread.start();
        Platform.runLater(() -> buttonClean()
        );
    }

    @FXML
    public void buttonClean() {
        resultXForm.setText("");
        resultFunctionXForm.setText("");
        resultAbsForm.setText("");
        resultIterForm.setText("");
        resultTimeForm.setText("");
        resultLabelForm.setText("");
    }

}
