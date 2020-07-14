package com.jxf.svc.font;

import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;

public  class MyFontsProvider extends XMLWorkerFontProvider {
	 
    public MyFontsProvider(){
        super(null, null);
    }

    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        String fntname = fontname;
        if (fntname == null) {
            fntname = "宋体";
        }
        if (size == 0) {
            size = 4;
        }
        return super.getFont(fntname, encoding, size, style);
    }
}
    

