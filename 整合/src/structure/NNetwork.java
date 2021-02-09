package structure;

import java.io.*;
import java.util.ArrayList;

public class NNetwork implements Serializable {
    private int layerNumber;  // 层数
    private Layer[] layers;  // 层

    /**
     * 提供输入进行前向传播计算
     * @param input 输入向量
     */
    public void forward_propa(double[] input) {
        layers[0].setOutput(input);
        for (int i = 1; i < this.layerNumber; i++)
            layers[i].forward_propa();
    }

    /**
     * 反向传播梯度下降
     * @param desire 目标输出
     */
    public void back_propa(double[] desire) {
        double[] tmpDelta = new double[desire.length];
        double[] output = this.layers[this.layerNumber - 1].getOutput();
        double[] integration = this.layers[this.layerNumber - 1].getIntegration();
        for (int i = 0; i < tmpDelta.length; i++)
            tmpDelta[i] = (output[i] - desire[i]) *
                    this.layers[this.layerNumber - 1].function.dactive_function(integration[i]);

        this.layers[this.layerNumber - 1].setDelta(tmpDelta);
        for (int i = this.layerNumber - 1; i >= 1; i--)
            this.layers[i].back_propa();

        for (int i = this.layerNumber - 1; i >= 1; i--)
            this.layers[i].apply_propa();
    }

    /**
     * 传入若干层，根据这些层构建神经网络
     * @param ls 层数组
     */
    private void organiseNetwork(Layer[] ls) {
        this.layerNumber = ls.length;
        this.layers = new Layer[ls.length];
        System.arraycopy(ls, 0, this.layers, 0, ls.length);
        for (int i = 0; i + 1 < ls.length; i++) {
            this.layers[i].setPostLayer(this.layers[i + 1]);
            this.layers[i + 1].setPreLayer(this.layers[i]);
        }
    }

    /**
     * 依据层数关系随机化奖励网络
     * @param lsn 每一层的节点数量
     * @param learningRate 网络的学习率
     * @param af 网络的激活函数
     */
    public NNetwork(int[] lsn, double learningRate, ActiceFunction af) {
        Layer[] ls = new Layer[lsn.length];
        ls[0] = new Layer(null, null, 1, lsn[0], learningRate, af);
        for (int i = 1; i < ls.length; i++)
            ls[i] = new Layer(null, null, lsn[i - 1], lsn[i], learningRate, af);

        this.organiseNetwork(ls);
    }

    /**
     * 读取网络文件
     * @param path 网络文件的路径，以\结尾
     * @param filename 文件名
     */
    public NNetwork(String path, String filename) throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(path + filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String line = reader.readLine();
        ArrayList<Layer> ls = new ArrayList<>();

        while (line != null) {
            ls.add(new Layer(null, null, new FileInputStream(path + line)));
            line = reader.readLine();
        }

        Layer[] tmp = new Layer[ls.size()];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = ls.get(i);
        this.organiseNetwork(tmp);
        reader.close();
        file.close();
    }

    /**
     * 写入储存文件
     * @param path 网络文件的路径，以\结尾
     * @param filename 文件名
     */
    public void saveNetwork(String path, String filename) throws IOException {
        OutputStream out = new FileOutputStream(path + filename);
        String lyrName = null;

        for (int i = 0; i < this.layerNumber; i++) {
            lyrName = String.format("%sl%d.lyr", path, i);
            this.layers[i].saveLayer(new FileOutputStream(lyrName));
            out.write(String.format("l%d.lyr\n", i).getBytes());
        }

        out.close();
    }

    /**
     * 输出网络参数
     */
    public void printNetwork() {
        System.out.println("---");
        for (int i = 1; i < this.layerNumber; i++) {
            System.out.println("Layer " + i + ":");
            this.layers[i].printArgu();

        }
    }

    /**
     * 打印网络输出
     */
    public void printOutput() {
        this.printLayerAt(this.layerNumber - 1);
    }

    /**
     * 打印第i层的节点
     * @param i 序号
     */
    public void printLayerAt(int i) {
        int len = layers[i].getLength();
        for (int j = 0; j < len; j++)
            System.out.printf("%+.6f ", layers[i].getOutput()[j]);
        System.out.println();
    }

    public void setLearningRate(double x) {
        for (Layer t : this.layers)
            t.setLearningRate(x);
    }

    public void setActivationFunc(ActiceFunction af) {
        for (Layer x : this.layers)
            x.function = af;
    }

    public double[] getOutput() {
        return this.layers[layerNumber - 1].getOutput();
    }
}
