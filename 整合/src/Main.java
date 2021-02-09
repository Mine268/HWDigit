import structure.ActiceFunction;
import structure.NNetwork;
import tool.DataProcessor;
import tool.Pair;
import tool.SigmoidActivation;

import java.lang.Math;

public class Main {
    public static void main(String[] args) throws Throwable {
        if (args.length != 3)
            System.out.println("False arguments!");
        else {
            int N = Integer.parseInt(args[2]);
            DataProcessor dp = new DataProcessor(args[0], args[1], N);
            NNetwork ns = new NNetwork(new int[]{28 * 28, 14, 14, 10}, 1., new SigmoidActivation());

            while (N-- > 0) {
                Pair pair = dp.readNext();
                ns.forward_propa(pair.image);  // 前向传播
                System.out.printf("[%d] ", tool1(pair.label));
                ns.printOutput();
                ns.back_propa(pair.label); // 反向传播
            }

            ns.saveNetwork("./", "hw.net");
        }
    }

    private static int tool1(double[] d) {
        for (int i = 0; i < 10; i++)
            if (d[i] >= .9) return i;
        return -1;
    }
}
