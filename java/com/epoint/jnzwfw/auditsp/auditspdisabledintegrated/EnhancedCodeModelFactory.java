package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsServiceInternal;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeTreeHandler;

import java.util.List;
import java.util.Map;

/**
 * 用数据管理平台代码生成所需的各种modal的工厂类
 *
 * @author shiyuan
 * @version [版本号, 2020-10-30]
 */
public class EnhancedCodeModelFactory {

    private static String codeId = "";

    /**
     * 初始化codeitem值
     *
     * @param codeName 代码名字
     * @param codeID   代码ID
     * @return List<CodeItems>
     */
    private static String getCodeID(String codeName, String codeID) {
        if (codeID == null) {
            // 如果连名字都为空,那么直接return
            if (StringUtil.isBlank(codeName)) {
                return null;
            }
            else {
                ICodeMainService mainService = ContainerFactory.getContainInfo().getComponent(ICodeMainService.class);
                Integer code = mainService.getCodeMainByName(codeName).getCodeID();
                if (code != null) {
                    codeID = String.valueOf(code);
                }
            }
        }
        return codeID;
    }

    /**
     * 利用代码项生成控件model的工具方法
     *
     * @param name     控件名字(EpointKeyNames.CHECK_SELECT_GROUP等)
     * @param codeName 代码名字
     * @param codeID   代码ID
     * @return T
     */
    public static TreeModel factoryTree(String name, String codeName, String codeID, String rootName) {
        codeID = getCodeID(codeName, codeID);
        if (codeID != null && name != null) {
            codeId = codeID;
            LazyTreeModal9 modal = new LazyTreeModal9(new CodeTreeHandler(codeID)) {
                private static final long serialVersionUID = -3624526000618820726L;

                @Override
                public String getSelectText(String selectedvalue) {
                    ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                            .getComponent(ICodeItemsService.class);
                    return codeItemsService.getItemTextByCodeID(codeId, selectedvalue);
                }
            };
            // 默认为单选
            modal.setTreeType(ConstValue9.RADIO_SINGLE);
            // 如果是多选
            if (name.equals(EpointKeyNames9.DROP_DOWN_CHECK_TREE)) {
                modal.setTreeType(ConstValue9.CHECK_SINGLE);
            }
            else {
                modal.setRootSelect(false);
            }
            if (StringUtil.isBlank(rootName)) {
                rootName = "所有节点";
            }
            modal.setRootName(rootName);
            return modal;

        }
        return null;
    }

    /**
     * 利用代码项生成控件model的工具方法
     *
     * @param codeName     代码名字
     * @param codeID       代码ID
     * @param hasInitValue 是否需要初始化值
     * @return T
     */
    public static List<Map<String, String>> factoryList(String codeName, String codeID, boolean hasInitValue) {
        codeID = getCodeID(codeName, codeID);
        if (codeID != null) {
            ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsService.class);
            return ICodeItemsService.changeBeanToMap(codeItemsService.listCodeItemsByCodeID(codeID), hasInitValue);

        }
        return null;
    }

    /**
     * 利用代码项生成控件model的工具方法
     *
     * @param codeName     代码名字
     * @param codeID       代码ID
     * @param hasInitValue 是否需要初始化值
     * @param level        层级
     * @return T
     */
    public static List<Map<String, String>> factoryList(String codeName, String codeID, boolean hasInitValue,
                                                        int level, String parentItemValue) {
        codeID = getCodeID(codeName, codeID);
        if (codeID != null) {
            ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsService.class);
            String id = "";
            if(StringUtil.isNotBlank(parentItemValue)){
                CodeItems parentCode = codeItemsService.getCodeItemByCodeName(codeName, parentItemValue);
                Integer itemId = parentCode.getItemId();
                id = itemId.toString();
            }
            return ICodeItemsService.changeBeanToMap(codeItemsService.listCodeItemsByLevel(codeID, id, level),
                    hasInitValue);
        }
        return null;
    }

    /**
     * 利用代码项生成控件model的工具方法
     *
     * @param codeName     代码名字
     * @param codeID       代码ID
     * @param hasInitValue 是否需要初始化值
     * @param itemValue    父级代码项值
     * @return T
     */
    public static List<Map<String, String>> factoryListLevel(String codeName, String codeID, boolean hasInitValue,
                                                             String itemValue) {
        codeID = getCodeID(codeName, codeID);
        if (codeID != null && StringUtil.isNotBlank(itemValue)) {
            ICodeItemsServiceInternal codeItemsService = ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsServiceInternal.class);
            CodeItems parentCode = codeItemsService.getCodeItemByCodeName(codeName, itemValue);
            Integer itemId = parentCode.getItemId();
            return ICodeItemsService
                    .changeBeanToMap(codeItemsService.listCodeItemsByLevel(codeID, itemId.toString(), 0), hasInitValue);
        }
        return null;
    }

}
