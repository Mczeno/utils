package com.mczeno;

import com.google.zxing.WriterException;
import com.mczeno.utils.QRCodeUtil;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try {
            QRCodeUtil.qrCodeToFile("1234567890", new File("f:/log/test-qrcode.PNG"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
