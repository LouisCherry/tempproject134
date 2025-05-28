package com.epoint.util;

import com.epoint.core.utils.classpath.ClassPathUtil;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;

public class HtmlUtil {
    
    public static ByteArrayOutputStream htmlToOutputStream(String htmlString){
        try ( ByteArrayOutputStream outputStream  = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            // 直接使用 HTML 字符串作为输入源
            builder.withHtmlContent(htmlString, "");

            // 下面这个方法是要自己指定字体文件，不然转出的 pdf 文件中中文会变成####
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    try {
                        System.out.println("正在加载字体文件...");
                        // 指定字体文件
                        return new FileInputStream(ClassPathUtil.getClassesPath() + "font" + File.separator+"FZFangSong.ttf");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, "simhei", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.toStream(outputStream);
            builder.run();
            System.out.println("PDF created successfully.");
            return outputStream;
            // 这里可以对输出流进行处理，例如发送到网络、存储在内存中或进行其他操作
            // 示例：将输出流发送到网络或存储在内存中
            // sendPdfBytesToNetwork(pdfBytes); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}