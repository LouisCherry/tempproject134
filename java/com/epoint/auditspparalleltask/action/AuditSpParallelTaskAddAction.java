package com.epoint.auditspparalleltask.action;

import com.epoint.auditspparalleltask.api.IAuditSpParallelTaskService;
import com.epoint.auditspparalleltask.api.entity.AuditSpParallelTask;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 并行阶段阶段事项配置表新增页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:18:40]
 */
@RightRelation(AuditSpParallelTaskListAction.class)
@RestController("auditspparalleltaskaddaction")
@Scope("request")
public class AuditSpParallelTaskAddAction extends BaseController
{
    @Autowired
    private IAuditSpParallelTaskService service;

    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    /**
     * 并行阶段阶段事项配置表实体对象
     */
    private AuditSpParallelTask dataBean = null;

    private List<SelectItem> phasenameModel = null;

    private List<SelectItem> typeModel = null;

    private List<SelectItem> isimportantModel = null;

    public void pageLoad() {
        dataBean = new AuditSpParallelTask();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        if (StringUtil.isNotBlank(dataBean.getPhaseguid())) {
            HashMap<String, Integer> map = new HashMap<>();
            String[] phaseguids = dataBean.getPhaseguid().split(",");
            for (int i = 0; i < phaseguids.length; i++) {
                AuditSpPhaseBaseinfo auditSpPhaseBaseinfo = iAuditSpPhaseBaseinfoService.find(phaseguids[i]);
                map.put(phaseguids[i], Integer.valueOf(auditSpPhaseBaseinfo.getOrdernum()));
            }
            List<String> phaseguid = new ArrayList<>();
            for (Map.Entry entry : sortMap(map).entrySet()) {
                phaseguid.add((String) entry.getKey());
            }
            dataBean.setPhaseguid(StringUtil.join(phaseguid, ","));
        }

        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    // Phaseguid map 排序
    public static Map<String, Integer> sortMap(Map<String, Integer> map) {
        // 利用Map的entrySet方法，转化为list进行排序
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        // 利用Collections的sort方法对list排序
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                // 正序排列，倒序反过来
                return o1.getValue() - o2.getValue();
            }
        });
        // 遍历排序好的list，一定要放进LinkedHashMap，因为只有LinkedHashMap是根据插入顺序进行存储
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> e : entryList) {
            linkedHashMap.put(e.getKey(), e.getValue());
        }
        return linkedHashMap;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditSpParallelTask();
    }

    public List<SelectItem> getPhasenameModel() {
        if (phasenameModel == null) {
            phasenameModel = new ArrayList<>();
            SqlConditionUtil sql = new SqlConditionUtil();

            List<AuditSpPhaseBaseinfo> list = iAuditSpPhaseBaseinfoService.getAllPhaseBaseinfo();
            if (!list.isEmpty()) {
                for (AuditSpPhaseBaseinfo auditSpPhaseBaseinfo : list) {
                    phasenameModel.add(
                            new SelectItem(auditSpPhaseBaseinfo.getRowguid(), auditSpPhaseBaseinfo.getPhasename()));
                }
            }
        }
        return this.phasenameModel;
    }

    public List<SelectItem> getIsimportantModel() {
        if (isimportantModel == null) {
            isimportantModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.isimportantModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "阶段类型", null, false));
        }
        return this.typeModel;
    }

    public AuditSpParallelTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpParallelTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpParallelTask dataBean) {
        this.dataBean = dataBean;
    }

}
