public class Main {
    public static void main(String[] args) throws Throwable {
        DataProcessor dp = new DataProcessor(
                "D:\\study\\手写数字识别\\data\\t10k-labels.idx1-ubyte",
                "D:\\study\\手写数字识别\\data\\t10k-images.idx3-ubyte",
                100);

        while (true) {
            Pair t = dp.readNext();
            System.out.println(t.label);
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++)
                    System.out.print(t.image[i * 28 + j] > .7 ? '.' : '#');
                System.out.println();
            }
        }
    }
}
