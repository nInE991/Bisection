package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


public class Controller {
    static boolean err = false;
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
                    bisection.left = new BigDecimal(Double.valueOf(leftEndForm.getText()));
                    bisection.right = new BigDecimal(Double.valueOf(rightEndForm.getText()));
                    bisection.tol = new BigDecimal(Double.valueOf(tolForm.getText()));
                    bisection.iteration = Integer.valueOf(iterlimForm.getText());
                    bisection.itermax = Integer.valueOf(iterlimForm.getText());
                    bisection.timeLimit = Long.parseLong(timelimForm.getText());
                    bisection.timeMax = Long.parseLong(timelimForm.getText());
                    bisection.functionLeft = new Expression(bisection.function).with("x", bisection.left).eval();
                    bisection.functionRight = new Expression(bisection.function).with("x", bisection.right).eval();

                    if (bisection.functionLeft.signum() == bisection.functionRight.signum()) {
                        throw new Exception("Sign of F(a) and F(b) must be opposite! Check end-points of the interval [a,b]!");
                    } else {
                        bisection.latch = new CountDownLatch(1);
                        while (bisection.left.subtract(bisection.right).abs().compareTo(bisection.tol) > 0 && bisection.cond == 0) {
                            if (bisection.latch.getCount() != 1) {
                                bisection.latch = new CountDownLatch(1);
                            }
                            bisection.Method();
                            if (bisection.iter >= bisection.iteration) {
                                bisection.cond = 1;
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
                                                bisection.cond = 0;
                                                iterlimForm.setText(String.valueOf(bisection.iteration));
                                                progressIndicatorForm.setVisible(true);
                                                bisection.StopPause();
                                            } else {
                                                bisection.cond = 1;
                                                resultLabelForm.setText("Solution not found, number of iterations exceeded!");
                                            }
                                            bisection.latch.countDown();
                                        }

                                );
                                bisection.latch.await();
                            }
                            if ((System.currentTimeMillis() - bisection.startTime) >= bisection.timeLimit) {
                                bisection.cond = 1;
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
                                                bisection.cond = 0;
                                                timelimForm.setText(String.valueOf(bisection.timeLimit));
                                                progressIndicatorForm.setVisible(true);
                                                bisection.StopPause();
                                            } else {
                                                bisection.cond = 1;
                                                resultLabelForm.setText("Solution not found, time exceeded !");
                                            }
                                            bisection.latch.countDown();
                                        }

                                );
                                bisection.latch.await();
                            }
                            Thread.sleep(15);
                        }
                    }
                    bisection.resultTime = (System.currentTimeMillis() - bisection.startTime);
                    Platform.runLater(() -> {
                        if (bisection.cond == 0) {
                            resultLabelForm.setText("Solution found!");
                        }
                        resultXForm.setText(String.valueOf(bisection.left));
                        resultFunctionXForm.setText(String.valueOf(bisection.functionLeft.setScale(34, RoundingMode.UP)));
                        resultAbsForm.setText(String.valueOf(bisection.right.subtract(bisection.left).abs().setScale(34, RoundingMode.UP)));
                        resultIterForm.setText(String.valueOf(bisection.iter));
                        resultTimeForm.setText(String.valueOf(bisection.resultTime));
                        progressIndicatorForm.setVisible(false);
                    });
                } catch (Exception ex) {
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
                            }
                    );
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
