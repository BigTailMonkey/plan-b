package com.btm.planb.demo;

import com.btm.planb.export.pdf.ExportFromHtmlWithImg;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class ExportPdfFromHtmlDemo {

    public static void main(String[] args) throws DocumentException, IOException {
        ExportFromHtmlWithImg exporter = new ExportFromHtmlWithImg();
        exporter.createPdfFromHtml("/tmp/java_space/demo6.pdf",getHtmlWithImg());
    }

    public static String getHtml() {
        StringBuffer html = new StringBuffer();
        html.append("<div style='color:green;font-size:20px;'>你好世界！hello world !</div>");
        html.append("<span style='color:red'>what are you 弄啥咧!</span>");
        html.append("<h1>标题</h1>");
        html.append("<table>");
        html.append("<tr>");
        html.append("<th>序号</th>");
        html.append("<th>用户名</th>");
        html.append("<th>性别</th>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td>1</td>");
        html.append("<td>fengxing</td>");
        html.append("<td>男</td>");
        html.append("</tr>");
        html.append("<tr>");
        html.append("<td>2</td>");
        html.append("<td>admin</td>");
        html.append("<td>女</td>");
        html.append("</tr>");
        html.append("</table>");
        return html.toString();
    }

    public static String getHtmlWithImg() {
        return "<p>text后面有个图片</p><br/>" +
                "<img src=\"/tmp/java_space/640-2.jpg\" alt=\"\" width=\"1054\" height=\"614\" /><br/>" +
                "<p>结尾文字</p>";
    }
}
