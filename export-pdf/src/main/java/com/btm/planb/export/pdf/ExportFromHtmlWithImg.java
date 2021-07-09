package com.btm.planb.export.pdf;

import com.btm.planb.export.pdf.Font.ChinaFontProvide;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Image;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportFromHtmlWithImg {

    public void createPdfFromHtml(String targetFile, String htmlString) throws IOException, DocumentException {
        createPdfFromHtml(new FileOutputStream(targetFile), htmlString);
    }

    public void createPdfFromHtml(OutputStream outputStream, String htmlString) throws IOException, DocumentException {
        // 设置纸张大小
        Document document = new Document(PageSize.A4);
        // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();

        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        //custom tag processor
        TagProcessorFactory factory = Tags.getHtmlTagProcessorFactory();
        factory.addProcessor(
                new Image(){
                    @Override
                    public List<Element> end(WorkerContext ctx, Tag tag, List<Element> currentContent) {
                        currentContent = new ArrayList<>(1);
                        try {
                            currentContent.add(getImageChunk(ctx, tag.getAttributes()));
                        } catch (BadElementException | IOException e) {
                            e.printStackTrace();
                        }
                        return currentContent;
                    }
                },"img");
        htmlContext.setTagFactory(factory);
        htmlContext.autoBookmark(false);
        htmlContext.setCssApplier(new CssAppliersImpl(new ChinaFontProvide()));

        PdfWriterPipeline pdf = new PdfWriterPipeline(document, pdfWriter);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        //here employee is my entity's object which has byte[] property.
        p.parse(new ByteArrayInputStream(htmlString.getBytes(StandardCharsets.UTF_8)));
        document.close();
    }

    /**
     * 手动构建图片
     * @param ctx 标签处理的上下文
     * @param attributes 标签的属性
     * @return 图片块
     * @throws BadElementException
     * @throws IOException
     */
    private Chunk getImageChunk(WorkerContext ctx, Map<String, String> attributes) throws BadElementException, IOException {
        MapContext mc;
        try {
            mc = (MapContext)ctx.get("com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline");
        } catch (NoCustomContextException ex) {
            throw new ExceptionConverter(ex);
        }
        //employeeService is service layer of three tier architecture
        com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(File2byte(new File(String.valueOf(attributes.get("src")))));
        img.scaleAbsolute(80, 80);
        return new Chunk(img, 0, 0, true);
    }


    /**
     * 将文件转换成byte数组
     * @param file 文件
     * @return byte数组
     */
    private byte[] File2byte(File file){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}
