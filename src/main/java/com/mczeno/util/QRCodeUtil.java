package com.mczeno.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码生成工具-基于Google Zxing
 * @author Chongming Zhou
 * @date 2018-06-25
 */
public class QRCodeUtil {
    private static final int COLOR_BLACK = 0xFF000000;
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private static final String DEFAULT_FORMAT = "PNG";

    private static BufferedImage generateBufferedImage(String content, int width, int height) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 指定纠错等级,纠错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? COLOR_BLACK : COLOR_WHITE));
            }
        }
        return image;
    }

    /**
     * 生成二维码到指定文件 （默认二维码宽/高度： 430， 默认生成图片文件格式： PNG）
     *
     * @param content 二维码中的内容
     * @param file    目的文件
     * @throws IOException
     * @throws WriterException
     */
    public static void qrCodeToFile(String content, File file) throws IOException, WriterException {
        qrCodeToFile(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FORMAT, file);
    }

    /**
     * 生成二维码到指定文件 （默认二维码宽/高度： 430）
     *
     * @param content 二维码中内容
     * @param format  生成图片文件格式
     * @param file    目的文件
     * @throws IOException
     * @throws WriterException
     */
    public static void qrCodeToFile(String content, String format, File file) throws IOException, WriterException {
        qrCodeToFile(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, format, file);
    }

    /**
     * 生成二维码到指定文件
     *
     * @param content 二维码中内容
     * @param width   二维码的宽度
     * @param height  二维码的高度
     * @param format  生成图片文件格式
     * @param file    目的文件
     * @throws IOException
     * @throws WriterException
     */
    public static void qrCodeToFile(String content, int width, int height, String format, File file)
            throws IOException, WriterException {
        BufferedImage image = generateBufferedImage(content, width, height);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        } else {
            System.out.println("图片生成成功！");
        }
    }

    /**
     * 生成二维码到指定输出流 （默认二维码宽/高度： 430， 默认生成图片文件格式： PNG）
     *
     * @param content      二维码中的内容
     * @param outputStream 输出流
     * @throws WriterException
     * @throws IOException
     */
    public static void qrCodeToStream(String content, OutputStream outputStream) throws WriterException, IOException {
        qrCodeToStream(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FORMAT, outputStream);
    }

    /**
     * 生成二维码到指定输出流 （默认二维码宽/高度： 430）
     *
     * @param content      二维码中的内容
     * @param format       生成图片文件格式
     * @param outputStream 输出流
     * @throws WriterException
     * @throws IOException
     */
    public static void qrCodeToStream(String content, String format, OutputStream outputStream)
            throws WriterException, IOException {
        qrCodeToStream(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, format, outputStream);
    }

    /**
     * 生成二维码到指定输出流
     *
     * @param content      二维码中的内容
     * @param width        二维码的宽度
     * @param height       二维码的高度
     * @param format       生成图片文件格式
     * @param outputStream 输出流
     * @throws WriterException
     * @throws IOException
     */
    public static void qrCodeToStream(String content, int width, int height, String format, OutputStream outputStream)
            throws WriterException, IOException {
        BufferedImage image = generateBufferedImage(content, width, height);
        if (!ImageIO.write(image, format, outputStream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
}
