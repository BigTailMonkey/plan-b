package com.btm.planb.worklogstatistic;

import com.btm.planb.exportexcel.Exporter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Start {

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/houdawei/Desktop/workLog.txt";
        WorkLog workLog = new WorkLog();
        List<WorkLogStandLine> standLines = workLog.analysis(filePath);
        standLines = standLines.stream().filter(standLine -> Objects.isNull(standLine.getErrorInfo())).collect(Collectors.toList());
        String basePath =  "/Users/houdawei/Desktop";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() +".xls";

        // 将Excel写入文件
        Exporter exporter = new Exporter("内容","时间","节点");
        HSSFWorkbook workbook = exporter.exportExcel(standLines, WorkLogStandLine.class);
        workbook.setSheetName(0,"sheetName");//设置sheet的Name

        // 无论是通过HttpServletResponse导出还是导出到本地磁盘,本质都是IO操作，所以这里将IO操作提取到外层。
        workbook.write(new File(basePath + File.separator + fileName));
    }
}
