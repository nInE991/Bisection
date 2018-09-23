package sample;


import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;


public class Bisection implements SeachTime {
    CountDownLatch latch;
    String function;
    BigDecimal left;
    BigDecimal right;
    BigDecimal tol;
    BigDecimal aver;
    BigDecimal functionLeft;
    BigDecimal functionRight;
    BigDecimal functionAver;
    int cond = 0;
    long iter = 0;
    int iteration;
    int itermax;
    long timeMax;
    long startTime;
    long timeLimit;
    long resultTime;
    long pauseTimeStart;

    @Override
    public void StartPause() {
        pauseTimeStart = System.currentTimeMillis();
    }

    @Override
    public void StopPause() {
        startTime = startTime + (System.currentTimeMillis() - pauseTimeStart);
    }

    public void Method() {
        aver = new Expression("(a+b)/2").with("a", left).with("b", right).eval();
        functionLeft = new Expression(function).with("x", left).eval();
        functionAver = new Expression(function).with("x", aver).eval();
        if (functionLeft.signum() == functionAver.signum()) {
            left = aver;
        } else {
            right = aver;
        }
        iter++;
//                if (System.currentTimeMillis() >= timeLimit) {
//                    cond = 1;
//                    Platform.runLater(() -> {
//                                Alert alert = new Alert(Alert.AlertType.ERROR);
//                                alert.setTitle("Error Dialog");
//                                alert.setHeaderText("Error!!! ");
//                                alert.setContentText("The search time has expired !");
//                                alert.showAndWait();
//                            }
//
//                    );
//
//                    result.setTime(String.valueOf(time));
//                }
//                try {
//                    Thread.sleep(15);
//                } catch (Exception ex) {
//
//                }
//            }
//
//        }
    }
}

