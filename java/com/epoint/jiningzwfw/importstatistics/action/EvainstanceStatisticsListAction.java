package com.epoint.jiningzwfw.importstatistics.action;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.jiningzwfw.importstatistics.api.IImportService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 好差评导入量统计list页面对应的后台
 *
 * @author Jiem
 * @version [版本号, 2019-06-06 16:10:26]
 */
@RestController("evainstancestatisticslistaction")
@Scope("request")
public class EvainstanceStatisticsListAction extends BaseController
{

    private static final long serialVersionUID = 3509582475405607625L;

    @Autowired
    private IImportService service;

    @Autowired
    private IOuService iOuService;

    @Autowired
    private IRoleService iRoleService;

    private String areaGuid;

    private String ouGuid;

    private TreeModel areaTreeModel;

    private TreeModel ouTreeModel;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    private Date startdate;
    
    private Date enddate;

    private boolean flag = false;

    private String[] tables = {"evainstance", "evainstancetwo", "evainstancethree", "evainstancefour", "evainstancefive", "evainstancesix", "evainstanceseven", "evainstanceeight", "evainstancenine", "evainstanceten", "evainstance_2"};
//    private String[] tables = {"evainstance", "evainstancetwo", "evainstanceseven", "evainstance_2"};

    @Override
    public void pageLoad()
    {
        String userRoles = userSession.getUserRoles();
        FrameRole role = iRoleService.getRoleByRoleField("ROLENAME", "统计分析管理员");
        if (role != null){
            String roleGuid = role.getRoleGuid();
            if (StringUtil.isNotBlank(userRoles) && userRoles.contains(roleGuid)){
                flag = true;
            }
        }

        addCallbackParam("ouGuid", userSession.getOuGuid());
        addCallbackParam("ouName", userSession.getOuName());
        addCallbackParam("flag", flag);
    }

    
    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @SneakyThrows
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    Record[] records = new Record[tables.length];

                    List<Record>  list = new ArrayList<>();
                    try {
                        EpointFrameDsManager.begin(request);
                        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 5, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
                        ArrayList<CompletableFuture> futureList = new ArrayList<>();
                        for (int i = 0; i < tables.length; i++) {

                            // 存放查询的参数
                            HashMap<String, String> map = new HashMap<>();
                            String startStr = EpointDateUtil.convertDate2String(startdate, EpointDateUtil.DATE_FORMAT);
                            String endStr = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);
                            if (StringUtil.isNotBlank(startStr)) {
                                map.put("start", startStr);
                                if (StringUtil.isBlank(endStr)) {
                                    map.put("end", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT));
                                } else {
                                    map.put("end", endStr);
                                }
                            }
                            String ouguid = ouGuid;
                            if (!flag) {
                                ouguid = userSession.getOuGuid();
                            }
                            if (StringUtil.isNotBlank(ouguid)) {
                                String areacode = "('";
                                String[] split = ouguid.split(",");
                                HashSet<String> codeSet = new HashSet<>();
                                for (String s : split) {
                                    FrameOuExtendInfo frameOuExtendInfo = iOuService.getFrameOuExtendInfo(s);
                                    codeSet.add(frameOuExtendInfo.getStr("AREACODE"));
                                }
                                areacode += StringUtil.join(new ArrayList<>(codeSet), "','");
                                areacode += "')";
                                map.put("areacode", areacode);
                            }
                            map.put("table", tables[i]);
                            Integer index = i;
                            futureList.add(CompletableFuture.runAsync(() -> {

                                Record record = service.findEvaInstanceStatistics(map);
                                EpointFrameDsManager.commit();
                                record.set("tablename", tables[index]);
                                records[index] = record;

                            }, executor));

                        }

                        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
                    }catch (Exception e){
                        EpointFrameDsManager.rollback();
                        e.printStackTrace();
                    }finally {
                        EpointFrameDsManager.close();
                    }

                    //system.out.println(Arrays.asList(records));
                    list.addAll(Arrays.asList(records));

                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return model;
    }

    public TreeModel getOuTreeModel(){
        if (ouTreeModel == null) {
            ouTreeModel = new TreeModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeNode == null) {
                        TreeNode root = new TreeNode();

                        root.setText("区县下的部门");
                        root.setId("");
                        root.setPid("-1");
                        root.getColumns().put("level", 1);
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }

                    // 每次点击树节点前的加号，进行加载
                    else {
                        String pAreaGuid = treeNode.getId();
                        if (StringUtil.isNotBlank(areaGuid)){
                            pAreaGuid = areaGuid;
                            List<FrameOu> listRootOu = iOuService.listOUByGuid(pAreaGuid, 2);

                            // 部门的绑定
                            for (int i = 0; i < listRootOu.size(); i++) {
                                if ((listRootOu.get(i).getParentOuguid() == null && pAreaGuid.equals(""))
                                        || (listRootOu.get(i).getParentOuguid().equals(pAreaGuid))) {
                                    TreeNode node = new TreeNode();
                                    node.setId(listRootOu.get(i).getOuguid());
                                    node.setText(listRootOu.get(i).getOuname());
                                    node.setPid(listRootOu.get(i).getParentOuguid());
                                    node.setLeaf(true);

                                    for (int j = 0; j < listRootOu.size(); j++) {
                                        if (listRootOu.get(i).getOuguid().equals(listRootOu.get(j).getParentOuguid())) {
                                            node.setLeaf(false);
                                            break;
                                        }
                                    }
                                    list.add(node);
                                }
                            }
                        }else {
                            return list;
                        }
                    }
                    return list;
                }
            };
        }
        return ouTreeModel;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel =new ExportModel(
                    "tablename,success,fail,rate",
                    "名称,成功次数,失败次数,主动评价率");
        }
        return exportModel;
    }

    public TreeModel getAreaTreeModel() {

        if (areaTreeModel == null) {
            areaTreeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeNode == null) {
                        TreeNode root = new TreeNode();

                        root.setText("济宁市");
                        root.setId("");
                        root.setPid("-1");
                        root.getColumns().put("level", 1);
                        root.setExpanded(true);// 展开下一层节点
                        list.add(root);

                        String pId = "";
                        String XiaQuCode = "";
                        Integer level = 2;

                        List<AuditOrgaArea> areaList = service.getAreaWithLevel(level, XiaQuCode);

                        for (AuditOrgaArea auditOrgaArea : areaList) {
                            TreeNode node = new TreeNode();
                            node.setId(auditOrgaArea.getOuguid());
                            node.setText(auditOrgaArea.getOuname());
                            node.setPid(pId);
                            node.getColumns().put("xiaqucode", auditOrgaArea.getXiaqucode());
                            node.getColumns().put("level", level);
                            node.setLeaf(false);
                            list.add(node);
                        }
                    }

                    // 每次点击树节点前的加号，进行加载
                    else {
                        String pId = treeNode.getId();
                        String XiaQuCode = (String) treeNode.getColumns().get("xiaqucode");
                        Integer pLevel = (Integer) treeNode.getColumns().get("level");
                        if (pLevel == null){
                            pLevel = 2;
                        }
                        int level = pLevel + 1;

                        List<AuditOrgaArea> areaList = service.getAreaWithLevel(level, XiaQuCode);

                        for (AuditOrgaArea auditOrgaArea : areaList) {
                                TreeNode node = new TreeNode();
                                node.setId(auditOrgaArea.getOuguid());
                                node.setText(auditOrgaArea.getOuname());
                                node.setPid(pId);
                                node.getColumns().put("xiaqucode", auditOrgaArea.getXiaqucode());
                                node.getColumns().put("level", level);
                                node.setLeaf(false);
                                list.add(node);
                        }
                    }
                    return list;
                }
            };
        }


        return areaTreeModel;
    }

    public Date getStartdate() {
        return startdate;
    }



    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getAreaGuid() {
        return areaGuid;
    }

    public void setAreaGuid(String areaGuid) {
        this.areaGuid = areaGuid;
    }

    public String getOuGuid() {
        return ouGuid;
    }

    public void setOuGuid(String ouGuid) {
        this.ouGuid = ouGuid;
    }
}
