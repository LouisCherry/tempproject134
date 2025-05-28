package com.epoint.action.auditznsbselfservicemachineregion.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
//import com.epoint.common.znsb.util.HttpUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.domain.JnAuditZnsbSelfmachineregion;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.inter.IJnAuditZnsbSelfmachineregion;


/**
 * 智能化一体机区域配置list页面对应的后台
 * 
 * @author 54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
@RestController("jnauditznsbselfservicemachineregionlistaction")
@Scope("request")
public class JnAuditZnsbSelfservicemachineRegionListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = -7937688067132446759L;

    @Autowired
    private IJnAuditZnsbSelfmachineregion service;
    
    @Autowired
    private IConfigService configservice;
    
    private TreeModel treeModel;
    
    /**
     * 智能化一体机区域配置实体对象
     */
    private JnAuditZnsbSelfmachineregion dataBean;
    
    
  
    /**
     * 表格控件model
     */
    private DataGridModel<JnAuditZnsbSelfmachineregion> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    

    public void pageLoad()
    {
    }

	

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<JnAuditZnsbSelfmachineregion> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnAuditZnsbSelfmachineregion>()
            {

                @Override
                public List<JnAuditZnsbSelfmachineregion> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(dataBean.getRegionlevel())) {
                        sql.eq("Regionlevel", dataBean.getRegionlevel());
                    }
                    if(StringUtil.isNotBlank(dataBean.getParentguid())) {
                        sql.eq("parentguid", dataBean.getParentguid());
                    }
                    if (StringUtil.isBlank(sortField)) {
                        sortField=" ordernum ";
                        sortOrder=" desc ";
                    }
                    PageData<JnAuditZnsbSelfmachineregion> pageData = service
                            .getRegionByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    List<JnAuditZnsbSelfmachineregion> list=pageData.getList();
                    for (JnAuditZnsbSelfmachineregion region : list) {
                        if (StringUtil.isNotBlank(region.getParentguid())) {
                        	JnAuditZnsbSelfmachineregion parentregion=service.find(region.getParentguid());
                            if (StringUtil.isNotBlank(parentregion)) {
                                region.setParentguid(parentregion.getRegionname());
                            }
                            
                        }
                    }
                    
                    this.setRowCount(pageData.getRowCount());
                    
                    return list;
                }

            };
        }
        return model;
    }

    
    public JnAuditZnsbSelfmachineregion getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new JnAuditZnsbSelfmachineregion();
    	}
        return dataBean;
    }

    public void setDataBean(JnAuditZnsbSelfmachineregion dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    
    public List<JnAuditZnsbSelfmachineregion> getAllList()
    {
        List<JnAuditZnsbSelfmachineregion> regionlist=service.getAllCommonRegionList().getResult();
        
        return regionlist;
    }
    
    public String getRegionFromRestful(){
        //长三角区域配置数据获取接口
        String regionurl = configservice.getFrameConfigValue("AS_ZNSB_CSJ_REGIONURL");
        if (StringUtil.isBlank(regionurl)) {
            return "系统参数AS_ZNSB_CSJ_REGIONURL未配置,无法获取长三角区域配置数据获取接口";
        }else{
            JSONObject dataJson = new JSONObject();
            JSONObject sendJson = new JSONObject();
            sendJson.put("token", "");
            sendJson.put("params", dataJson);
//            String returnmsg = HttpUtil.sendpost(regionurl, sendJson);
            String returnmsg =HttpUtil.doPostJson(regionurl, sendJson.toJSONString());
            JSONObject json = JSON.parseObject(returnmsg);
            JSONObject obj = (JSONObject) json.get("custom");
            JSONArray  regionlist=obj.getJSONArray("regionlist");
            JSONObject regionjson;
            JnAuditZnsbSelfmachineregion region;
            for (Object object : regionlist) {
                regionjson=(JSONObject)object;
                region=service.getRegionByRowguid(regionjson.getString("rowguid")).getResult();
                if (StringUtil.isNotBlank(region)&&(!"1".equals(region.getIsindividuation()))) {                                       
                    region.setRegionlevel(regionjson.getString("regionlevel"));
                    region.setParentguid(regionjson.getString("parentguid"));
                    region.setClickurl(regionjson.getString("clickurl"));
                    region.setIsenable(regionjson.getString("isenable"));
                    region.setRegionname(regionjson.getString("regionname"));
                    region.setPicattachguid(regionjson.getString("picattachguid"));
                    region.setAreacode(regionjson.getString("areacode").split("\\.")[0]);
                    region.setCreatedate(new Date());
                    region.setIsindividuation(StringUtil.isNotBlank(regionjson.getString("isindividuation"))?regionjson.getString("isindividuation"):"0");
                    region.setIstobeupdated("1");
                    region.setPixel(regionjson.getString("pixel"));
                    region.setOrdernum(regionjson.getString("ordernum"));
                    service.update(region);
                }else  if (StringUtil.isBlank(region)){
                    region=new JnAuditZnsbSelfmachineregion();
                    region.setRowguid(regionjson.getString("rowguid"));
                    region.setRegionlevel(regionjson.getString("regionlevel"));
                    region.setParentguid(regionjson.getString("parentguid"));
                    region.setClickurl(regionjson.getString("clickurl"));
                    region.setIsenable(regionjson.getString("isenable"));
                    region.setRegionname(regionjson.getString("regionname"));
                    region.setPicattachguid(regionjson.getString("picattachguid"));
                    region.setAreacode(regionjson.getString("areacode").split("\\.")[0]);
                    region.setCreatedate(new Date());
                    region.setIsindividuation(StringUtil.isNotBlank(regionjson.getString("isindividuation"))?regionjson.getString("isindividuation"):"0");
                    region.setIstobeupdated("1");
                    region.setPixel(regionjson.getString("pixel"));
                    region.setOrdernum(regionjson.getString("ordernum"));
                    service.insert(region); 
                }
            }
            //先将旧数据删除
            service.deleteOldRegion();
            //再将新数据转正
            service.updateNewRegion();
        }
        return "配置更新成功";
    }
    

    public TreeModel getTreeData() {

        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                /**
                 * 树加载事件
                 *
                 * @param treeNode
                 *            当前展开的节点
                 * @return List<TreeNode>
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有地区");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        if(StringUtil.isBlank(objectGuid)) {
                            // 一级目录
                            List<JnAuditZnsbSelfmachineregion> parentlist=service.getParentRegionList().getResult();
                            for (JnAuditZnsbSelfmachineregion JnAuditZnsbSelfmachineregion : parentlist) {
                                TreeNode node = new TreeNode();
                                node.setId(JnAuditZnsbSelfmachineregion.getRowguid());
                                node.setText(JnAuditZnsbSelfmachineregion.getRegionname());
                                node.setPid(objectGuid);
                                node.setLeaf(true);
                                int count = service.getChildRegionListByParentguid(JnAuditZnsbSelfmachineregion.getRowguid()).getResult().size();
                                if (count>0) {
                                    node.setLeaf(false);                               
                                }                            
                                list.add(node);
                            }
                            
                            
                        }else {
                            // 二级目录
                            List<JnAuditZnsbSelfmachineregion> childlist= service.getChildRegionListByParentguid(objectGuid).getResult();
                            for (JnAuditZnsbSelfmachineregion JnAuditZnsbSelfmachineregion : childlist) {
                                TreeNode node = new TreeNode();
                                node.setId(JnAuditZnsbSelfmachineregion.getRowguid());
                                node.setText(JnAuditZnsbSelfmachineregion.getRegionname());
                                node.setPid(objectGuid);
                                node.setLeaf(true);
                                List<JnAuditZnsbSelfmachineregion> grandchildlist= service.getChildRegionListByParentguid(
                                        JnAuditZnsbSelfmachineregion.getRowguid()).getResult();
                                if (grandchildlist.size()>0) {
                                    node.setLeaf(false);                               
                                }
                                list.add(node);
                            }
                            
                            
                        }
                    }
                    return list;
                }

                /**
                 * 树节点点击事件
                 *
                 * @param item
                 *            当前点击的树节点
                 * @return List<com.epoint.core.dto.model.SelectItem>
                 */
                @Override
                public List<SelectItem> onLazyNodeSelect(TreeNode item) {
                    // TODO Auto-generated method stub
                    return super.onLazyNodeSelect(item);
                }

            };
        }
        return treeModel;
    }

}
