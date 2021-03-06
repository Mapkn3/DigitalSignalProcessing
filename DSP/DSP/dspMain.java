package DSP;

import java.io.IOException;

public class dspMain {
    public static void main(String[] args) {
        try {
            String inputImagePath = "D:\\zzz\\help\\Second\\src\\DSP\\in.jpg";
            String outputImagePath = "D:\\zzz\\help\\Second\\src\\DSP\\out.png";
            FloatImage f = new DSP.FloatImage(inputImagePath);
            FloatImage h = new FloatImage(3, 3);
            for (int i = 0; i < h.getHeight(); i++) {
                for (int j = 0; j < h.getWidth(); j++) {
                    h.setValue(i, j, (float) (1.0/9.0));
                }
            }
            FloatImage res = conv(f, h);
            res.save(outputImagePath, "png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static FloatImage conv(FloatImage f, FloatImage h) {
        FloatImage g = new FloatImage(f.getHeight(), f.getWidth());
        for (int i = 0; i < g.getHeight(); i++) {
            for (int j = 0; j < g.getWidth(); j++) {
                float sum = 0;
                for (int m = 0; m < h.getHeight(); m++) {
                    for (int n = 0; n < h.getWidth(); n++) {
                        int hy = h.getHeight()-1-m;
                        int hx = h.getWidth()-1-n;
                        int fy = i-(h.getHeight()/2)+m;
                        int fx = j-(h.getWidth()/2)+n;
                        float hxy = h.getValue(hy, hx);
                        float fxy = f.getValue(fy, fx);
                        sum += hxy * fxy;
                    }
                }
                g.setValue(i, j, sum);
            }
        }
        return g;
    }
    static FloatImage getGaussFilter(int height, int width) {
        FloatImage filter = new FloatImage(height, width);
        double sigmai = filter.getHeight() / 6.0;
        double sigmaj = filter.getWidth() / 6.0;
        int mi = filter.getHeight() / 2;
        int mj = filter.getWidth() / 2;
        float sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                float value = (float) Math.exp(-0.5*(((1-mi)*(1-mi))/(sigmai*sigmai))-0.5*(((1-mj)*(1-mj))/(sigmaj*sigmaj)));
                filter.setValue(i, j, value);
                sum += value;
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                float value = filter.getValue(i, j);
                filter.setValue(i, j, (float) (value/sum*1.0));
            }
        }
        return filter;
    }

    static FloatImage sobel(FloatImage f) throws IOException {
        FloatImage g = new FloatImage(f.getHeight(), f.getWidth());

        FloatImage hg1 = new FloatImage(3, 3);
        hg1.setValue(0, 0, 1);
        hg1.setValue(0, 1, 0);
        hg1.setValue(0, 2, -1);
        hg1.setValue(1, 0, 2);
        hg1.setValue(1, 1, 0);
        hg1.setValue(1, 2, -2);
        hg1.setValue(2, 0, 1);
        hg1.setValue(2, 1, 0);
        hg1.setValue(2, 2, -1);
        FloatImage g1 = conv(f, hg1);

        FloatImage hg2 = new FloatImage(3, 3);
        hg2.setValue(0, 0, 1);
        hg2.setValue(0, 1, 2);
        hg2.setValue(0, 2, 1);
        hg2.setValue(1, 0, 0);
        hg2.setValue(1, 1, 0);
        hg2.setValue(1, 2, 0);
        hg2.setValue(2, 0, -1);
        hg2.setValue(2, 1, -2);
        hg2.setValue(2, 2, -1);
        FloatImage g2 = conv(f, hg2);

        for (int i = 0; i < g.getHeight(); i++) {
            for (int j = 0; j < g.getWidth(); j++) {
                float g1ij = g1.getValue(i, j);
                float g2ij = g2.getValue(i, j);
                g.setValue(i, j, (float) Math.sqrt(g1ij*g1ij+g2ij*g2ij));
            }
        }
        return g;
    }
}
