package structure;

import java.io.Serializable;

public interface ActiceFunction extends Serializable {
    public String name = "";  // 激活函数名
    abstract public double active_function(double x);  // 激活函数
    abstract public double dactive_function(double x);  // 激活函数的导数
}
