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
    String perem;
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
        functionLeft = new Expression(function).with(perem, left).eval();
        functionAver = new Expression(function).with(perem, aver).eval();
        if (functionLeft.signum() == functionAver.signum()) {
            left = aver;
        } else {
            right = aver;
        }
        iter++;
    }
}

