package sample;


import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;


public class Bisection implements SeachTime {
    CountDownLatch latch;
    String function;
    BigDecimal a;
    BigDecimal b;
    BigDecimal tol;
    BigDecimal aver;
    BigDecimal functionA;
    BigDecimal functionB;
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
        aver = new Expression("(a+b)/2").with("a", a).with("b", b).eval();
        functionA = new Expression(function).with(perem, a).eval();
        functionAver = new Expression(function).with(perem, aver).eval();
        if (functionA.signum() == functionAver.signum()) {
            a = aver;
        } else {
            b = aver;
        }
        iter++;
    }
}

