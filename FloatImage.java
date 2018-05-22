package DSP;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Полутоновое изображение.
 */
public final class FloatImage {
    private float[] data;
    private int height;
    private int width;

    /**
     * Создаёт пустое изображение указанных размеров.
     *
     * @param height Количество строк (высота).
     * @param width  Количество столбцов (ширина).
     */
    public FloatImage(int height, int width) {
        data = new float[height * width];
        this.height = height;
        this.width = width;
    }

    /**
     * Загружает изображение из указанного файла.
     *
     * @param filename Путь к файлу с изображением.
     */
    public FloatImage(String filename) throws IOException {
        BufferedImage image;
        try (FileInputStream fis = new FileInputStream(filename)) {
            image = ImageIO.read(fis);
        }
        height = image.getHeight();
        width = image.getWidth();
        data = new float[height * width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int rgb = image.getRGB(j, i);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
                setValue(i, j, 0.3f * r + 0.6f * g + 0.1f * b);
            }
        }
    }

    /**
     * Записывает изображение в файл.
     *
     * @param filename Путь к файлу для записи.
     * @param format   Формат, в котором будет сохранено изображение.
     * @throws IOException Если не удалось записать изображение.
     */
    @Override
    public void save(String filename, String format) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int value = clamp(getValue(i, j));
                int rgb = 0;
                rgb = rgb | (value << 16);
                rgb = rgb | (value << 8);
                rgb = rgb | value;
                image.setRGB(j, i, rgb);
            }
        }
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            ImageIO.write(image, format, fos);
        }
    }

    /**
     * Возвращает количество строк.
     *
     * @return Количество строк.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает количество столбцов.
     *
     * @return Количество столбцов.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает яркость отсчёта.
     *
     * @param i Номер строки.
     * @param j Номер столбца.
     * @return Яркость отсчёта по указанным координатам.
     */
    public float getValue(int i, int j) {
        if (i < 0) {
            i = 0;
        }
        if (j < 0) {
            j = 0;
        }
        if (i > height-1) {
            i = height-1;
        }
        if (j > width-1) {
            j = width-1;
        }
        return data[i * width + j];
    }

    /**
     * Устанавливает яркость отсчёта по указанным координатам.
     *
     * @param i     Номер строки.
     * @param j     Номер столбца.
     * @param value Новое значение яркости.
     */
    public void setValue(int i, int j, float value) {
        data[i * width + j] = value;
    }

    private int clamp(float value) {
        if (value < 0) {
            return 0;
        }
        if (value > 255) {
            return 255;
        }
        return Math.round(value);
    }
}
