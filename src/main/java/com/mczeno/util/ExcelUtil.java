package com.mczeno.util;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Excel导出工具类-基于Apache poi-ooxml
 * @author Chongming Zhou
 * @date 2017-07-27
 */
public class ExcelUtil {

    /**
     * @param headNames excel中sheet的表头
     * @param list      需要导出的列表
     * @param out       输出流
     * @throws Exception
     */
    public static <T> void exportAsExcel(String[] headNames, List<T> list, OutputStream out) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        SXSSFSheet sheet = workbook.createSheet();

        // 第三步，在sheet中添加表头第0行
        SXSSFRow row = sheet.createRow(0);

        // 设置居中样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        row.setRowStyle(style);

        // 第四步，创建单元格
        // 设置表头
        for (int i = 0; i < headNames.length; i++) {
            row.createCell(i).setCellValue(headNames[i]);
        }

        // 设置表内容
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = list.get(i);
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            int nullCount = 0;
            for (int j = 0; j < fields.length; j++) {
                char[] charArray = fields[j].getName().toCharArray();
                charArray[0] -= 32;
                String methodName = "get" + String.valueOf(charArray);
                try {
                    Method method = clazz.getMethod(methodName, (Class<?>[]) null);
                    String str = method.invoke(t, (Object[]) null).toString();
                    row.createCell(j - nullCount).setCellValue(str);
                } catch (Exception e) {
                    nullCount++;
                    // e.printStackTrace();
                }
            }
        }

        // 第五步，将文件输出
        workbook.write(out);

        if (workbook != null) {
            workbook.close();
        }
    }

}
