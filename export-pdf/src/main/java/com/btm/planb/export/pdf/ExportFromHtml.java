package com.btm.planb.export.pdf;

import com.btm.planb.export.pdf.Font.ChinaFontProvide;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExportFromHtml {

    public void createPdfFromHtml(String targetFile, String html) throws IOException, DocumentException {
        createPdfFromHtml(new FileOutputStream(targetFile), html);
    }

    public void createPdfFromHtml(OutputStream outputStream, String html) throws IOException, DocumentException {
        // 设置纸张大小
        Document document = new Document(PageSize.A4);
        // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8, new ChinaFontProvide());
        document.close();
    }
}
