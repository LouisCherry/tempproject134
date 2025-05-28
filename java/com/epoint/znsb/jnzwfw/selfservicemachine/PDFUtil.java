package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFUtil
{
    public static String addWatermarkToBase64(String base64Str, String watermark) {
        String result = "";
        try {
            // 获取Base64.Decoder对象
            Base64.Decoder decoder = Base64.getDecoder();
            // 解码base64编码的字符串为字节数组
            byte[] bytes = decoder.decode(base64Str);
            // 创建ByteArrayInputStream对象
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            // 创建PdfReader对象
            PdfReader reader = new PdfReader(inputStream);
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 创建PdfStamper对象，并添加水印
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content;
            PdfGState gs = new PdfGState();
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
            // 设置透明度
            gs.setFillOpacity(0.2f);
            gs.setStrokeOpacity(0.3f);
            for (int i = 1; i < total; i++) {
                content = stamper.getUnderContent(i);
                content.beginText();
                content.setFontAndSize(baseFont, 30);
                content.setGState(gs);
                String[] list = watermark.split("&&");
                int pppi = 0;
                for (String waString : list) {
                    content.showTextAligned(Element.ALIGN_BOTTOM, waString, 50 + pppi * 100, 50, 55);
                    pppi++;
                }

                content.endText();
            }
            stamper.close();
            // 获取添加水印后的字节数组
            byte[] resultBytes = outputStream.toByteArray();
            // 获取Base64.Encoder对象
            Base64.Encoder encoder = Base64.getEncoder();
            // 将添加水印后的字节数组转换为base64编码的字符串
            result = encoder.encodeToString(resultBytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
