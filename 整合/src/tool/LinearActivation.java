package tool;

import structure.ActiceFunction;

public class LinearActivation implements ActiceFunction {
    final public String name = "Linear";

    @Override
    public double active_function(double x) {
        return x;
    }

    @Override
    public double dactive_function(double x) {
        return 1;
    }
}
