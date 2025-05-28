package com.epoint.auditproject.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 旋转图片工具类
 * @author 大脑补丁
 *
 */
public class RotateImageUtil {

    /**
     * 
     * @param bufferedImage
     *            图片
     * @param angel
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(BufferedImage bufferedImage, int angel) {
        if (bufferedImage == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = bufferedImage.getWidth(null);
        int imageHeight = bufferedImage.getHeight(null);
        // 计算重新绘制图片的尺寸
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        // 获取原始图片的透明度
        int type = bufferedImage.getColorModel().getTransparency();
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, type);
        Graphics2D graphics = newImage.createGraphics();
        // 平移位置
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        // 旋转角度
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        // 绘图
        graphics.drawImage(bufferedImage, null, null);
        return newImage;
    }

    /**
     * 旋转图片
     * 
     * @param image
     *            图片
     * @param angel
     *            旋转角度
     * @return
     */
    public static BufferedImage rotateImage(Image image, int angel) {
        if (image == null) {
            return null;
        }
        if (angel < 0) {
            // 将负数角度，纠正为正数角度
            angel = angel + 360;
        }
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        Rectangle rectangle = calculatorRotatedSize(new Rectangle(new Dimension(imageWidth, imageHeight)), angel);
        BufferedImage newImage = null;
        newImage = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = newImage.createGraphics();
        // transform
        graphics.translate((rectangle.width - imageWidth) / 2, (rectangle.height - imageHeight) / 2);
        graphics.rotate(Math.toRadians(angel), imageWidth / 2, imageHeight / 2);
        graphics.drawImage(image, null, null);
        return newImage;
    }

    /**
     * 计算旋转后的尺寸
     * 
     * @param src
     * @param angel
     * @return
     */
    private static Rectangle calculatorRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }
    /**
     * 获取网络图片流（附加）
     * 
     * @param url
     * @return
     */
    public static InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
