package com.btm.planb.export.pdf.html;

import com.itextpdf.text.Image;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OverrideImageProvider extends AbstractImageProvider {

    private final Map<String, Image> map;

    public OverrideImageProvider(Map<String, Image> map) {
        this.map = map;
    }

    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.pipeline.html.ImageProvider#retrieve(java.lang.String)
     */
    public Image retrieve(final String src) {
        return map.get(src);
    }


    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.pipeline.html.ImageProvider#store(java.lang.String, com.itextpdf.text.Image)
     */
    public void store(final String src, final Image img) {
        map.put(src, img);
    }

    /* (non-Javadoc)
     * @see com.itextpdf.tool.xml.pipeline.html.ImageProvider#reset()
     */
    public void reset() {
        this.map.clear();
    }

    @Override
    public String getImageRootPath() {
        return "/";
    }
}
