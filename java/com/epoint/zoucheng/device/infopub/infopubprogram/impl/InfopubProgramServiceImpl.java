package com.epoint.zoucheng.device.infopub.infopubprogram.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.epoint.basic.bizlogic.sysconf.code.service.CodeItemsService9;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.zip.ZipUtil;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.ProgramMaterial;
/**
 * 节目表对应的后台service实现类
 * 
 * @author why
 * @version [版本号, 2019-09-23 10:52:48]
 */
@Component
@Service
public class InfopubProgramServiceImpl implements IInfopubProgramService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(InfopubProgram record) {
        return new InfopubProgramService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new InfopubProgramService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(InfopubProgram record) {
        return new InfopubProgramService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public InfopubProgram find(Object primaryKey) {
       return new InfopubProgramService().find(primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public InfopubProgram find(String sql, Object... args) {
        return new InfopubProgramService().find(args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<InfopubProgram> findList(String sql, Object... args) {
       return new InfopubProgramService().findList(sql,args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<InfopubProgram> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new InfopubProgramService().findList(sql,pageNumber,pageSize,args);
    }
    /**
     * 
     *  [获取节目数量] 
     *  @param programguid 节目标识
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int getProgramCount(String programguid) {
        return new InfopubProgramService().getProgramCount(programguid);
    }

    @Override
    public void createProgramFile(InfopubProgram infopubProgram, String host) {
        boolean hasVideo = false;
        String dir = ClassPathUtil.getDeployWarPath() + "jiningzwfw/zoucheng/";
        String programDir = dir + "infopub/file/";
        FileManagerUtil.isExistFileDir(programDir, true);
        programDir += "/" + infopubProgram.getRowguid() + "/";
        // 创建节目文件夹
        if (FileManagerUtil.isExistFileDir(programDir, true)) {
            FileManagerUtil.deleteFile(programDir);
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 拷贝js和css
        FileManagerUtil.copyFile(dir + "infopub/js/jquery-1.11.0.min.js", programDir, "jquery-1.11.0.min.js", false);
        FileManagerUtil.copyFile(dir + "infopub/js/unslider/unslider.css", programDir, "unslider.css", false);
        FileManagerUtil.copyFile(dir + "infopub/js/unslider/unslider-min.js", programDir, "unslider-min.js", false);
        FileManagerUtil.copyFile(dir + "infopub/js/MSClass.js", programDir, "MSClass.js", false);
        FileManagerUtil.copyFile(dir + "infopub/js/scroll.js", programDir, "scroll.js", false);
        List<ProgramMaterial> list = JSON.parseArray(infopubProgram.getContent(), ProgramMaterial.class);
        //head
        StringBuilder head = new StringBuilder();
        //加上编码，不然安卓的webview中文资源不能加载
        head.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title></title>");

        //style
        StringBuilder sbStyle = new StringBuilder();
        sbStyle.append("<link href='unslider.css' rel='stylesheet' />");
        sbStyle.append("<style type='text/css'>");
        sbStyle.append("* {margin: 0;padding: 0;}");
        sbStyle.append("#demo table {width: 100%;border: 0;}");
        sbStyle.append("#drap-wrap {position: relative;}");

        StringBuilder sbHtml = new StringBuilder();
        String[] resolution = new CodeItemsService9().getItemTextByCodeName("分辨率", infopubProgram.getResolution())
                .split("\\*");
        sbHtml.append("<div id='demo' style='position: relative;' data-width='" + resolution[0] + "' data-height='"
                + resolution[1] + "'>");

        for (int i = 0; i < list.size(); i++) {
            ProgramMaterial programMaterial = list.get(i);
            switch (programMaterial.getType()) {
                case "image":
                    sbHtml.append("<div id='dragItem" + i + "' class='slider'>");
                    sbHtml.append("<ul>");
                    String[] imageArray = programMaterial.getContent().split(";");
                    for (int j = 0; j < imageArray.length; j++) {
                        String imgName = imageArray[j].substring(imageArray[j].lastIndexOf('/') + 1);
                        sbHtml.append("<li><img width='" + programMaterial.getWidth() + "' height='"
                                + programMaterial.getHeight() + "' src='" + imgName + "' /></li>");
                        FileManagerUtil.copyFile(dir + "infopub/uploadfile/" + imgName, programDir, imgName, false);
                    }
                    sbHtml.append("</ul>");
                    sbHtml.append("</div>");
                    break;
                case "video":
                    hasVideo = true;
                    String fileName = programMaterial.getContent()
                            .substring(programMaterial.getContent().lastIndexOf('/') + 1);
                    sbHtml.append("<div id='dragItem" + i + "'  class='dragDiv'>");
                    sbHtml.append(" <video width='100%' height='100%' id='video0' src='" + fileName + "'></video>");
                    sbHtml.append("</div>");
                    //复制视频
                    FileManagerUtil.copyFile(dir + "infopub/uploadfile/" + fileName, programDir, fileName, false);
                    break;
                case "text":
                    sbHtml.append("<div id='dragItem" + i + "'  class='dragDiv'>");
                    sbHtml.append("<div style='width:100%; height:100%;' id='txt" + i + "'>"
                            + EncodeUtil.decode(programMaterial.getContent()) + "</div>");
                    sbHtml.append("</div>");
                    break;
                case "page":
                    sbHtml.append("<div id='dragItem" + i + "'  class='dragDiv'>");
                    sbHtml.append("<iframe id='iframe" + i + "' src='" + programMaterial.getContent()
                            + "' width='100%' height='100%' frameborder='no' ></iframe>");
                    sbHtml.append("</div>");
                    break;
            }
            sbStyle.append("#dragItem" + i + " {width:" + programMaterial.getWidth() + "; height:"
                    + programMaterial.getHeight() + "; position: absolute;top:" + programMaterial.getY() + "; left:"
                    + programMaterial.getX() + ";z-index:" + programMaterial.getZindex() + ";}");
        }
        sbStyle.append("</style>");
        sbHtml.append("</div>");

        //script
        StringBuilder script = new StringBuilder();
        script.append("<script type='text/javascript' src='jquery-1.11.0.min.js'></script>");
        script.append("<script type='text/javascript' src='unslider-min.js'></script>");
        script.append("<script type='text/javascript'>");
        if (!hasVideo) {
            //设置滚动速度
            script.append("var speed = " + infopubProgram.getScrollspeed() + ";");
        }
        //视频播放脚本
        script.append(
                "var video = document.getElementById('video0'); if(video!=null) {video.play(); video.loop = false; video.addEventListener('ended',function () {video.load(); /* video.currentTime = 0.1; */ video.play();}, false); video.addEventListener('loadedmetadata', function() { video.currentTime = 0.1;}, false);} function onPageFinished() { var video = document.getElementById('video0'); if(video!=null) {video.play(); video.loop = false; video.addEventListener('ended',function () {    /*  video.currentTime = 0.1; */ video.play();}, false);             video.addEventListener('loadedmetadata', function() {video.currentTime = 0.1;}, false);}}");
        //图片轮播效果
        script.append("$('.slider').unslider({delay: " + infopubProgram.getDelaytime() * 1000
                + ",autoplay: true, animation: 'fade', keys: false, arrows: false, nav: false})");
        script.append("</script>");
        if (!hasVideo) {
            //引用滚动JS
            script.append("<script type='text/javascript' src='MSClass.js'></script>");
            script.append("<script type='text/javascript' src='scroll.js'></script>");
        }
        if (hasVideo) {
            FileManagerUtil.writeContentToFile(programDir, infopubProgram.getRowguid() + ".html",
                    "<html><head>" + head.toString() + sbStyle.toString()
                            + "</head><body style='background-color:black'>" + sbHtml.toString() + script.toString()
                            + "</body></html>");
        }
        else {
            FileManagerUtil.writeContentToFile(programDir, infopubProgram.getRowguid() + ".html",
                    "<html><head>" + head.toString() + sbStyle.toString() + "</head><body>" + sbHtml.toString()
                            + script.toString() + "</body></html>");
        }

        ZipUtil.doZip(dir + "infopub/file/" + infopubProgram.getRowguid(),
                dir + "infopub/file/" + infopubProgram.getRowguid() + ".zip");
        infopubProgram.setPath(host + "jiningzwfw/zoucheng/infopub/file/" + infopubProgram.getRowguid() + ".zip");
        try {
            String md5 = MD5Util.getMD5(dir + "infopub/file/" + infopubProgram.getRowguid() + ".zip");
            infopubProgram.setMd5(md5);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    /**
     * 
     *  [获取guid和名称] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<InfopubProgram> getGuidAndName() {
        return new InfopubProgramService().getGuidAndName();
    }
    /**
     * 
     *  [获取节目名称] 
     *  @param programguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String getProgramName(String programguid) {
        // TODO Auto-generated method stub
        return new InfopubProgramService().getProgramName(programguid);
    }

}
