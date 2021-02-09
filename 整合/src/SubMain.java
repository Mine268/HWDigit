import structure.*;
import tool.DataProcessor;
import tool.Pair;

public class SubMain {
    public static void main(String[] args) throws Throwable {
        int N = 10000;
        NNetwork ns = new NNetwork("D:\\study\\手写数字识别\\argu\\", "hw.net");
        DataProcessor dp = new DataProcessor("D:\\study\\手写数字识别\\数据\\t10k-labels.idx1-ubyte",
                "D:\\study\\手写数字识别\\数据\\t10k-images.idx3-ubyte", N);

        int success = 0;
        while (N-- > 0) {
            Pair pair = dp.readNext();
            ns.forward_propa(pair.image);
            if (foo1(ns.getOutput()) == foo1(pair.label))
                ++success;
            else {
                System.out.printf("[%d, %d] ", foo1(pair.label), foo1(ns.getOutput()));
                ns.printOutput();
            }
        }

        System.out.println(success);
    }

    private static int foo1(double[] d) {
        double t = Double.MIN_VALUE;
        int ind = 0, i = 0;
        while (i < 10) {
            if (d[i] > t) {
                t = d[i];
                ind = i;
            }
            i++;
        }
        return ind;
    }
}
