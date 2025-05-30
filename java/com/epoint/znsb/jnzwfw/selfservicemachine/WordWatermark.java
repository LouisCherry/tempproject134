package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.epoint.core.utils.code.Base64Util;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.HeaderFooter;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ShapeLineStyle;
import com.spire.doc.documents.ShapeType;
import com.spire.doc.fields.ShapeObject;

public class WordWatermark
{
    public static String changeWordWatermark(String base64, String keyword) {
        // 加载示例文档
        Document doc = new Document();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Util.decodeBuffer(base64));
        doc.loadFromStream(inputStream, FileFormat.Doc);
        // 添加艺术字并设置大小
        ShapeObject shape = new ShapeObject(doc, ShapeType.Text_Plain_Text);
        shape.setWidth(600);
        shape.setHeight(20);
        // 设置艺术字文本内容、位置及样式
        shape.setVerticalPosition(30);
        shape.setHorizontalPosition(2);
        shape.setRotation(315);
        shape.getWordArt().setFontFamily("宋体");
        shape.getWordArt().setText(keyword);
        shape.setFillColor(Color.gray);
        shape.setLineStyle(ShapeLineStyle.Single);
        shape.setStrokeColor(new Color(192, 192, 192, 255));
        shape.setStrokeWeight(1);

        Section section;
        HeaderFooter header;
        for (int n = 0; n < doc.getSections().getCount(); n++) {
            section = doc.getSections().get(n);
            // 获取section的页眉
            header = section.getHeadersFooters().getHeader();
            Paragraph paragraph;

            if (header.getParagraphs().getCount() > 0) {
                // 如果页眉有段落，取它第一个段落
                paragraph = header.getParagraphs().get(0);
            }
            else {
                // 否则新增加一个段落到页眉
                paragraph = header.addParagraph();
            }
            for (int i = 0; i < 4; i++) {
                // 复制艺术字并设置多行多列位置
                shape = (ShapeObject) shape.deepClone();
                shape.setVerticalPosition(50 + 150 * i);
                shape.setHorizontalPosition(-10);
                paragraph.getChildObjects().add(shape);

            }
        }
        // 保存文档
        // doc.saveToFile("result.docx", FileFormat.Docx_2013);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.saveToStream(os, FileFormat.Docx);

        base64 = Base64Util.encode(os.toByteArray());
        try {
            inputStream.close();
            os.close();
            doc.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return base64;
    }
}
