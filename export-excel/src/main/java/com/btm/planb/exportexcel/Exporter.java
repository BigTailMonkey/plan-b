package com.btm.planb.exportexcel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Exporter {

    private final String[] headers;

    public Exporter(String... headers) {
        this.headers = headers;
    }


    /**
     * 生成导出文件
     *
     * @param data 需要导出的数据
     * @param clz  数据对应的实体类
     * @param <T>  泛型
     * @return Excel文件
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public <T> HSSFWorkbook exportExcel(List<T> data, Class<T> clz) throws IllegalAccessException, InvocationTargetException {
        if (Objects.isNull(data)) {
            return null;
        }
        System.out.println("导出数据"+data.size()+"条");
        Method[] allMethods = clz.getDeclaredMethods();
        List<Method> annotationMethods = new ArrayList<>();

        // 创建工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();//这里也可以设置sheet的Name
        // 创建工作表对象
        HSSFSheet sheet = workbook.createSheet();
        // 创建表头
        Row rowHeader = sheet.createRow(0);

        for (int i = 0;i < headers.length;i++) {
            for (Method method : allMethods) {
                ExcelHeader annotation = method.getAnnotation(ExcelHeader.class);
                if (Objects.nonNull(annotation) && annotation.value().equals(headers[i])) {
                    rowHeader.createCell(i).setCellValue(annotation.value());
                    // 字段
                    annotationMethods.add(method);
                }
            }
        }

        // 数据处理
        for (int i = 0; i < data.size(); i++) {
            //创建工作表的行(表头占用1行, 这里从第二行开始)
            HSSFRow row = sheet.createRow(i + 1);
            // 获取一行数据
            T t = data.get(i);
            // 填充列数据
            for (int j = 0; j < annotationMethods.size(); j++) {
                Method method = annotationMethods.get(j);
                method.setAccessible(true);
                row.createCell(j).setCellValue(String.valueOf(method.invoke(t)));
            }
        }
        System.out.println("导出文件构建成功");
        return workbook;
    }
}
