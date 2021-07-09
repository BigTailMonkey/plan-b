package com.btm.planb.demo;

import com.btm.planb.export.pdf.Exporter;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class ExportPdfDemo {

    public static void main(String[] args) {
        Exporter word = new Exporter();
        String file = "/tmp/java_space/demo1.pdf";
        try {
            word.createDocContext(file);
        } catch (DocumentException | IOException documentException) {
            documentException.printStackTrace();
        }
    }
}
