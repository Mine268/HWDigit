import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataProcessor {
    private InputStream image;
    private InputStream label;
    private int size;
    private int lengthOfUnit;

    /**
     * 初始化数据读取
     * @param labelFile 标签文件所在
     * @param imageFile 图像文件所在
     * @param N 读取数量
     */
    public DataProcessor(String labelFile, String imageFile, int N) throws Throwable {
        this.label = new FileInputStream(labelFile);
        this.image = new FileInputStream(imageFile);

        byte[] buffer1 = new byte[4], buffer2 = new byte[4];
        int s1 = 0, s2 = 0;

        this.label.read(buffer1, 0, 4);
        this.image.read(buffer2, 0, 4);
        this.label.read(buffer1, 0, 4);
        this.image.read(buffer2, 0, 4);

        for (int k = 0; k < 4; k++) {
            s1 <<= 8; s2 <<= 8;
            s1 += buffer1[k] & 0xff; s2 += buffer2[k] & 0xff;
        }

        if (s1 != s2 || s1 <= 0 || s2 <= 0)
            System.out.println("Length un-match!");
        else {
            this.size = s1;

            int t1 = 0, t2 = 0;
            this.image.read(buffer2, 0, 4);
            for (int k = 0; k < 4; k++) {
                t1 <<= 8;
                t1 += buffer2[k] & 0xff;
            }
            this.image.read(buffer2, 0, 4);
            for (int k = 0; k < 4; k++) {
                t2 <<= 8;
                t2 += buffer2[k] & 0xff;
            }

            this.lengthOfUnit = t1 * t2;
        }
    }

    private Pair tmp = new Pair();
    /**
     * 逐个读取
     * @return 返回pair
     */
    public Pair readNext() throws IOException {
        byte[] tBuffer = new byte[this.lengthOfUnit];

        // 从文件读取一个标签
        double nextLabel;
        this.label.read(tBuffer, 0, 1);
        nextLabel = (double) ((int)tBuffer[0] & 0xff);

        // 从文件读取下一幅图像
        double[] nextImage = new double[this.lengthOfUnit];
        this.image.read(tBuffer, 0, this.lengthOfUnit);
        for (int k = 0; k < this.lengthOfUnit; k++)
            nextImage[k] = ((int) tBuffer[k] & 0xff) / 255.;

        this.tmp.image = nextImage;
        this.tmp.label = nextLabel;

        return tmp;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.image.close();
        this.label.close();
    }

    public int getSize() {
        return this.size;
    }
}
