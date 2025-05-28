package com.epoint;

import com.aspose.words.*;
import com.epoint.auditclient.rabbitmqhandle.SpecialClientHandle;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

import java.io.*;

public class JNGenerateCertTest {

    public static void main(String[] args) {
        //输入模板
        File file = new File("C:\\Users\\pansh\\Downloads\\涉及饮用水电子证照 (1) (1) (1).docx");
        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
        License license = new License();
        try {
            license.setLicense(licenseName);
            FontSettings.setFontsFolder(ClassPathUtil.getClassesPath() + "font" + File.separator, true);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Document doc = new Document(inputStream);
            IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            DocumentBuilder builder = new DocumentBuilder(doc);
            // 填充word域值
            String[] fieldNames= new String[]{"产品名称"};
            String aa ="123®23";
            Object[] values = new String[]{aa};
            //处理特殊字符
            if(aa.contains("®")){
//                builder.moveToMergeField(fieldNames[0]);
//                builder.getFont().setName("Wingdings 2");
//                SpecialChar specialChar = new SpecialChar(doc,'®',)
//                builder.write("\u00AE");
                builder.moveToMergeField(fieldNames[0]);
                builder.getFont().setName("Arial");
                aa.replace("®","\u00AE");
                builder.write(aa);
            }

            doc.getMailMerge().execute(fieldNames, values);
            //输出文件

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.PDF);
            File file1 = new File("C:\\Users\\pansh\\Downloads\\");
            // 如果文件夹不存在则创建
            if (!file1.exists()) {
                file1.mkdirs();
            }
            String pdfFilepath = "C:\\Users\\pansh\\Downloads\\涉及饮用水电子证照 (1) (1) (1).pdf";
            doc.save(pdfFilepath, SaveFormat.PDF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
