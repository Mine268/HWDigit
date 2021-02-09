package tool;

import structure.ActiceFunction;
import java.lang.Math;

public class SigmoidActivation implements ActiceFunction {
    @Override
    public double active_function(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double dactive_function(double x) {
        double sigma = this.active_function(x);
        return sigma * (1 - sigma);
    }
}
