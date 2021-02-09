package structure;

import java.io.*;
import java.util.Random;
import tool.*;

/**
 * 深度神经网络中每一层作为一个类
 */
public class Layer {
    private Layer preLayer;  // 前一层
    private Layer postLayer;  // 后一层

    public ActiceFunction function;  // 激活函数接口
    private double learningRate;  // 学习率

    private int m,  // 输入向量长度
                n;  // 输出向量长度

    private double[] integration;  // 整合向量
    private double[] output;  // 输出向量

    private double[][] weights;  // 权重矩阵
    private double[][] dweights;  // 权重矩阵调整量
    private double[] bias;  // 偏置矩阵
    private double[] dbias;  // 偏置矩阵调整量

    private double[] delta;  // 传播参数

    /**
     * 反向传播，根据后一层的传播参数计算本层的传播参数
     */
    public void back_propa() {
        if (this.postLayer != null)
            for (int i = 0; i < this.n; i++) {
                this.delta[i] = 0.;
                for (int j = 0; j < this.postLayer.n; j++)
                    this.delta[i] += this.postLayer.delta[j] * this.postLayer.weights[j][i];
                this.delta[i] *= this.function.dactive_function(this.integration[i]);
            }

        // 计算 dweights
        for (int j = 0; j < this.n; j++)
            for (int i = 0; i < this.m; i++)
                this.dweights[j][i] = this.learningRate * this.delta[j] * this.preLayer.output[i];

        // 计算 dbias
        for (int j = 0; j < this.n; j++)
            this.dbias[j] = this.learningRate * this.delta[j];
    }

    /**
     * 把计算的梯度应用到参数中
     */
    public void apply_propa() {
        for (int i = 0; i < this.n; i++) {
            this.bias[i] -= this.learningRate * this.dbias[i];
            for (int j = 0; j < this.m; j++)
                this.weights[i][j] -= this.learningRate * this.dweights[i][j];
        }
    }

    /**
     * 前向传播
     */
    public void forward_propa() {
        // 计算整合向量
        for (int i = 0; i < this.n; i++) {
            this.integration[i] = 0.;
            for (int j = 0; j < this.m; j++)
                this.integration[i] += this.weights[i][j] * this.preLayer.output[j];
        }
        for (int i = 0; i < this.n; i++)
            this.integration[i] += this.bias[i];

        // 计算得到输出向量
        for (int i = 0; i < this.n; i++)
            this.output[i] = this.function.active_function(this.integration[i]);
    }

    /**
     * 初始化参数空间
     * @param m 输入向量长度
     * @param n 输出向量长度
     */
    private void buildLayer(Layer pre, Layer post, int m, int n) {
        this.preLayer = pre;
        this.postLayer = post;

        this.m = m;
        this.n = n;

        this.integration = new double[n];
        this.output = new double[n];

        this.weights = new double[n][m];
        this.dweights = new double[n][m];
        this.bias = new double[n];
        this.dbias = new double[n];

        this.delta = new double[n];
    }

    /**
     * 写入参数到文件
     * @param file 文件流
     */
    public void saveLayer(FileOutputStream file) throws IOException {
        BufferedOutputStream bo = new BufferedOutputStream(file);
        DataOutputStream out = new DataOutputStream(bo);

        out.writeInt(this.m);
        out.writeInt(this.n);
        for (int i = 0; i < this.n; i++)
            for (int j = 0; j < this.m; j++)
                out.writeDouble(this.weights[i][j]);
        for (int i = 0; i < this.n; i++)
            out.writeDouble(this.bias[i]);
        out.writeDouble(this.learningRate);
        ObjectWR.writeLY(this.function, out);

        bo.close();
        out.close();
    }

    /**
     * 从文件载入参数构成层，约定文件的储存方式如下
     * 头两个为整数int m,n，表示输入和输出层的长度，然后是n*m个浮点数double表示权重矩阵，
     * 然后是n个浮点数double表示偏置，最后一个数表示学习率，最后是激活函数对象
     * @param pre 前一层引用
     * @param post 后一层引用
     * @param file 对应文件流
     */
    public Layer(Layer pre, Layer post, FileInputStream file) throws IOException, ClassNotFoundException {
        BufferedInputStream bi = new BufferedInputStream(file);
        DataInputStream in = new DataInputStream(bi);

        this.buildLayer(pre, post, in.readInt(), in.readInt());
        for (int i = 0; i < this.n; i++)
            for (int j = 0; j < this.m; j++)
                this.weights[i][j] = in.readDouble();
        for (int i = 0; i < this.n; i++)
            this.bias[i] = in.readDouble();
        this.learningRate = in.readDouble();
        this.function = (ActiceFunction) ObjectWR.readLY(in);

        in.close();
        bi.close();
    }

    /**
     * 随机初始化
     * @param m 输入向量长度
     * @param n 输出向量参数
     * @param rate 学习率
     * @param func 激活函数
     */
    public Layer(Layer pre, Layer post, int m, int n, double rate, ActiceFunction func) {
        buildLayer(pre, post, m, n);
        this.learningRate = rate;
        this.function = func;

        // 下面进行随机初始化
        Random r = new Random();
        for (int i = 0; i < this.m; i++)
            for (int j = 0; j < this.n; j++)
                this.weights[j][i] = r.nextDouble() * 2. - 1.;
        for (int i = 0; i < this.n; i++)
            this.bias[i] = r.nextDouble() * 2. - 1.;
    }

    /**
     * 打印层参数
     */
    public void printArgu() {
        System.out.println("<Weights>");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++)
                System.out.printf("%+.6f ", this.weights[i][j]);
            System.out.println();
        }
        System.out.println("<Biases>");
        for (int i = 0; i < this.n; i++)
            System.out.printf("%+.6f ", this.bias[i]);
        System.out.println("\n<Output>");
        for (int i = 0; i < this.n; i++)
            System.out.printf("%+.6f ", this.output[i]);
        System.out.println("\n<Delta>");
        for (int i = 0; i < this.n; i++)
            System.out.printf("%+.6f ", this.delta[i]);
        System.out.println();
    }

    public void setOutput(double[] e) {
        System.arraycopy(e, 0, this.output, 0, e.length);
    }

    public double[] getOutput() {
        return this.output;
    }

    public double getLearningRate() {
        return this.learningRate;
    }

    public void setLearningRate(double v) {
        this.learningRate = v;
    }

    public double[][] getWeights() {
        return this.weights;
    }

    public double[] getBias() {
        return this.bias;
    }

    public void setDelta(double[] e) {
        System.arraycopy(e, 0, this.delta, 0, e.length);
    }

    public void setPreLayer(Layer e) {
        this.preLayer = e;
    }

    public void setPostLayer(Layer e) {
        this.postLayer = e;
    }

    public int getLength() {
        return this.n;
    }

    public double[] getIntegration() {
        return this.integration;
    }
}
