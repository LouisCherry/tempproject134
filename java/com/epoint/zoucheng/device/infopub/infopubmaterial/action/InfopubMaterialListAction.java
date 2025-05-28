package com.epoint.zoucheng.device.infopub.infopubmaterial.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.device.infopub.infopubmaterial.api.IInfopubMaterialService;
import com.epoint.zoucheng.device.infopub.infopubmaterial.api.entity.InfopubMaterial;
import com.epoint.zoucheng.device.infopub.programstatistic.api.IProgramstatisticService;
import com.epoint.zoucheng.device.infopub.programstatistic.api.entity.Programstatistic;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 素材表list页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-14 16:30:56]
 */
@RestController("infopubmateriallistaction")
@Scope("request")
public class InfopubMaterialListAction extends BaseController
{
    private static final long serialVersionUID = 835334922018694774L;

    @Autowired
    private IInfopubMaterialService infopubMaterialService;
    @Autowired
    private IProgramstatisticService programstatisticService;
    @Autowired
    private ICodeItemsService codeService;
    @Autowired
    private IConfigService configService;
    /**
     * 素材表实体对象
     */
    private InfopubMaterial dataBean;
    /**
     * 上传文件标识
     */
    private String attachGuid;

    /**
     * 表格控件model
     */
    private DataGridModel<InfopubMaterial> model;

    private FileUploadModel9 fileUploadModel;

    private String course; // 保存节点信息，实现左树右表的动态刷新

    public void pageLoad() {

    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * 在确认添加时，将添加的素材类型存入节目统计库
     */
    public void saveSelect(String materialtype) {
        String[] programForm = materialtype.split(";");
        String rowguid = UUID.randomUUID().toString();
        Programstatistic pStatistic = new Programstatistic();
        pStatistic.setRowguid(rowguid);
        if ("图片".equals(programForm[0])) {
            pStatistic.setProgramform("1");// 代码项：1表示节目形式为图片
        }
        else {
            pStatistic.setProgramform("2");// 2=视频
        }
        pStatistic.setDatetime(new Date());
        programstatisticService.insert(pStatistic);
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            infopubMaterialService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<InfopubMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubMaterial>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -4220667530059070761L;

                @Override
                public List<InfopubMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isNotBlank(course) && !course.equals("f9root")) {
                        addViewData("course", course);
                        conditionSql = conditionSql + " and materialstyle like '" + course + "%'";
                    }
                    String type = getRequestParameter("type");
                    if ("picture".equals(type)) {
                        conditionSql += " and MaterialType='01'";// 素材分类代码项
                    }
                    else if ("video".equals(type)) {
                        conditionSql += " and MaterialType='02'";
                    }
                    List<InfopubMaterial> list = infopubMaterialService.findList(
                            ListGenerator.generateSql("InfoPub_Material", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = infopubMaterialService
                            .findList(ListGenerator.generateSql("InfoPub_Material", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    // 文件上传功能
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            attachGuid = UUID.randomUUID().toString();
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage storage) {
                    return false;

                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    FrameAttachStorage storage = (FrameAttachStorage) attach;
                    String name = storage.getAttachFileName();
                    int materialcount = infopubMaterialService.getMaterialCount(name, getViewData("course"));
                    if (materialcount == 0) {
                        int lastIndex = name.lastIndexOf(".");
                        String type = name.substring(lastIndex + 1);
                        String size = String.valueOf(storage.getSize() / 1024) + "Kb";
                        String reName = attachGuid + "." + type;
                        
                        String materialPath = addFrameAttach(storage, attachGuid, userSession.getUserGuid(),
                                userSession.getDisplayName());
                        add(name, type, size, materialPath, reName);
                        fileUploadModel.getExtraDatas().put("msg", "文件上传成功");
                    }
                    else {
                        fileUploadModel.getExtraDatas().put("msg", "此分类中已有同名文件，请重新上传");
                    }
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(attachGuid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    // 新增附件(上传到服务器本地路径)
    public String addFrameAttach(FrameAttachStorage attachStorage, String clientGuid, String uploadUserGuid,
            String uploadUserName) {
        String materialPath = ClassPathUtil.getDeployWarPath() + configService.getFrameConfigValue("materialPath");
        String fileName = attachStorage.getAttachFileName();
        int lastIndex = fileName.lastIndexOf(".");
        String fileType = fileName.substring(lastIndex + 1);
        // String materialPath =
        // "/epoint-web/src/main/webapp/frame/pages/device/infopub/uploadfile";
        FileManagerUtil.isExistFileDir(materialPath, true);
        FileManagerUtil.writeContentToFileByStream(materialPath, clientGuid + "." + fileType,
                attachStorage.getContent());
        return materialPath;
    }

    public void add(String name, String type, String size, String materialPath, String reName) {
        dataBean = new InfopubMaterial();
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setMaterialname(name);
        dataBean.setMaterialsize(size);
        dataBean.setMaterialstyle(getViewData("course"));
        if ("mp4".equals(type)) {
            dataBean.setMaterialtype("02");
        }
        else {
            dataBean.setMaterialtype("01");
            // 初始化轮播时间
            dataBean.setDelayTime(40);
        }
        dataBean.setPhysicalpath(materialPath + reName);
        dataBean.setVirtualpath("../uploadfile/" + reName);
        dataBean.setUploadtime(new Date());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        infopubMaterialService.insert(dataBean);
        dataBean = null;
    }

    public void saveAll() {
        List<InfopubMaterial> list = getDataGridData().getWrappedData();
        InfopubMaterial infopubMaterial = null;
        int materialcount = 0;
        boolean flag = false;
        for (InfopubMaterial material : list) {
            // 根据已修改的素材名称以及素材类型查找 素材的数量
            materialcount = infopubMaterialService.getMaterialCount(material.getMaterialname(),
                    material.getMaterialstyle());
            // 获取未修改前的素材信息
            infopubMaterial = infopubMaterialService.find(material.getRowguid());
            boolean modify = !material.getMaterialname().equals(infopubMaterial.getMaterialname());
            // 只要素材名称变了，并且还能找到该素材说明，名称修改重复了
            if (modify && materialcount == 1) {
                flag = true;
            }
            else {
                infopubMaterialService.update(material);
            }
        }
        if (flag) {
            addCallbackParam("msg", "文件名称重复，请重新修改");
        }
        else {
            addCallbackParam("msg", "保存成功！");
        }
    }

    public TreeModel getTreeModel() {
        return new TreeModel()
        {
            private static final long serialVersionUID = 8910715135343435523L;
            List<TreeNode> nodes = new ArrayList<>();

            @Override
            public List<TreeNode> fetch(TreeNode arg0) {
                TreeNode root = new TreeNode();
                root.setText("素材类型");
                root.setId("f9root");
                root.setPid("");
                nodes.add(root);
                root.setExpanded(false);
                // 找到所有的素材类型
                List<CodeItems> codeItemsList = codeService.listCodeItemsByCodeName("素材类型");
                for (CodeItems codeItems : codeItemsList) {
                    TreeNode NodeBuilding = new TreeNode();
                    NodeBuilding.setPid("f9root");
                    NodeBuilding.setId(codeItems.getItemValue());
                    NodeBuilding.setText(codeItems.getItemText());
                    NodeBuilding.setLeaf(true);
                    nodes.add(NodeBuilding);
                }

                return nodes;
            }
        };
    }

    public InfopubMaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new InfopubMaterial();
        }
        return dataBean;
    }

    public void setDataBean(InfopubMaterial dataBean) {
        this.dataBean = dataBean;
    }

}
