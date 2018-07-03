package com.mczeno.util;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import java.util.Set;

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
	 * 导出为 Excel
	 * @param headerPropertyMap Excel 表格中的表头和对象属性的组合键值对（LinkedHashMap 保证存取顺序）
	 * @param objects 需要到处的对象列表
	 * @param outputStream 输出流
	 * @throws Exception 异常
	 * @see 附web项目下载设置：{
	 * 	   HttpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8"); 
	 * 	   HttpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + Excel名 + ".xlsx");
	 * }
	 */
    public static <T> void exportAsExcel(LinkedHashMap<String, String> headerPropertyMap, List<T> objects, OutputStream outputStream) throws Exception {
        if (headerPropertyMap == null || headerPropertyMap.isEmpty()) {
			throw new Exception("Empty map of header and property!");
		}
        
    	// 第一步，创建一个webbook，对应一个Excel文件
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        SXSSFSheet sheet = workbook.createSheet("sheet1");

        // 第三步，在sheet中添加表头第0行
        SXSSFRow row = sheet.createRow(0);

        // 设置居中样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        row.setRowStyle(style);

        // 第四步，创建单元格
        Set<Entry<String, String>> entrySet = headerPropertyMap.entrySet();
        
        // 设置表头
        List<String> propertyList = new ArrayList<>();
        int columnIndex = 0;
        for (Entry<String, String> entry : entrySet) {
        	row.createCell(columnIndex++).setCellValue(entry.getKey());
        	propertyList.add(entry.getValue());
        }
        
        // 设置表内容
        for (int i = 0; i < objects.size(); i++) {
            row = sheet.createRow(i + 1);
            T t = objects.get(i);
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) t.getClass();
            for (int j = 0; j < propertyList.size(); j++) {
                Field field = clazz.getDeclaredField(propertyList.get(j));
                char[] charArray = field.getName().toCharArray();
                charArray[0] -= 32;
                Method method = clazz.getMethod("get" + String.valueOf(charArray), (Class<?>[]) null);
                Object result =	method.invoke(t, (Object[]) null);
                row.createCell(j).setCellValue(result != null ? result.toString() : "");
            }
        }

        // 第五步，将文件输出
        workbook.write(outputStream);

        if (workbook != null) {
            workbook.close();
        }
    }

}
