package com.btm.planb.worklogstatistic;

import com.btm.planb.exportexcel.Exporter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Start {

    public static void main(String[] args) throws Exception {
        String filePath = "/Users/houdawei/Desktop/workLog.txt";
        WorkLog workLog = new WorkLog();
        List<WorkLogStandLine> standLines = workLog.analysis(filePath);
        String basePath =  "/Users/houdawei/Desktop";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() +".xls";

        // 将Excel写入文件
        Exporter exporter = new Exporter("工作内容","状态","备注","异常信息","TB号");
        HSSFWorkbook workbook = exporter.exportExcel(standLines, WorkLogStandLine.class);
        workbook.setSheetName(0,"sheetName");//设置sheet的Name

        // 无论是通过HttpServletResponse导出还是导出到本地磁盘,本质都是IO操作，所以这里将IO操作提取到外层。
        workbook.write(new File(basePath + File.separator + fileName));
    }
}
