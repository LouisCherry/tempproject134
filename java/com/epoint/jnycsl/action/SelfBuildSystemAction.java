package com.epoint.jnycsl.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.dto.PageParam;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;


/**
 * 自建系统
 * @author 刘雨雨
 * @time 2018年9月12日下午1:44:14
 */
@RestController("selfbuildsystemaction")
@Scope("request")
public class SelfBuildSystemAction extends TaianBaseAction {

	private static final long serialVersionUID = -3800283680157067901L;

	@Autowired
	private DeptSelfBuildSystemService selfBuildSystemService;
	@Autowired
	private IAttachService attachservice;

	private DataGridModel<DeptSelfBuildSystem> selfBuildSystemModel;

	private DeptSelfBuildSystem selfBuildSystem;

	private DeptSelfBuildSystem param;
	
    private FileUploadModel9 sltfileUploadModel;
    
    private String sltcliengguid;

	private List<SelectItem> apiTypes;
	
	private List<SelectItem> selfBuildSystemList;
	
    private List<SelectItem> ouModal;
    

	@Override
	public void pageLoad() {
		String fromPage = getRequestParameter(FROM_PAGE);
		init(fromPage);
	}

	@Override
	protected void initList() {
		if (param == null) {
			param = new DeptSelfBuildSystem();
		}
	}

	@Override
	protected void initEdit() {
		String rowguid = getRequestParameter("rowguid");
		selfBuildSystem = selfBuildSystemService.findSelfBuildSystemByRowGuid(rowguid);
		if(selfBuildSystem != null){
		    sltcliengguid = selfBuildSystem.getSltcliengguid();
		    if(StringUtil.isBlank(sltcliengguid)){
		        sltcliengguid = UUID.randomUUID().toString();
		        selfBuildSystem.setSltcliengguid(sltcliengguid);
		        selfBuildSystemService.saveOrUpdateSelfBuildSystem(selfBuildSystem);
		    }
		}
	}

	@Override
	protected void initDetail() {
		String rowguid = getRequestParameter("rowguid");
		selfBuildSystem = selfBuildSystemService.findSelfBuildSystemByRowGuid(rowguid);
		if(selfBuildSystem != null){
            sltcliengguid = selfBuildSystem.getSltcliengguid();
            if(StringUtil.isBlank(sltcliengguid)){
                sltcliengguid = UUID.randomUUID().toString();
                selfBuildSystem.setSltcliengguid(sltcliengguid);
                selfBuildSystemService.saveOrUpdateSelfBuildSystem(selfBuildSystem);
            }
        }
	}

	@Override
	protected void initAdd() {
		if (selfBuildSystem == null) {
			selfBuildSystem = new DeptSelfBuildSystem();
			sltcliengguid = UUID.randomUUID().toString();
			selfBuildSystem.setSltcliengguid(sltcliengguid);
		}
	}
	
	public List<DeptSelfBuildSystem> getSlefSystem(){
	    PageData<DeptSelfBuildSystem> pageData = 
	            selfBuildSystemService.getSelfBuildSystemPage(param, new PageParam(0, 10));
	    List<DeptSelfBuildSystem> list = pageData.getList();
	    String ouguid = userSession.getOuGuid();
	    for (Iterator<DeptSelfBuildSystem> iterator = list.iterator(); iterator.hasNext();) {
            DeptSelfBuildSystem sys = (DeptSelfBuildSystem) iterator.next();
	        if(StringUtil.isBlank(sys.getOuguid())||!sys.getOuguid().contains(ouguid)){
	            iterator.remove();
                continue;
            }
	        List<FrameAttachInfo> sltlist = attachservice.getAttachInfoListByGuid(sys.getSltcliengguid());
	        if(sltlist != null && sltlist.size()>0){
	            sys.set("attachguid", sltlist.get(0).getAttachGuid());
	        }
        }
	    return list;
	}
	
    public List<SelectItem> getOuList(String query) {
        if (ouModal == null) {
            ouModal = new ArrayList<SelectItem>();
            List<FrameOu> list = null;
            if(StringUtil.isNotBlank(query)){
                list = CommonDao.getInstance().findList(
                        "select ouguid,ouname,oushortname from frame_ou where ouname like '%"+query+"%'", FrameOu.class);
            }else{
                String ouguidstr = selfBuildSystem.getOuguid();
                String sqlouguid = "";
                if(StringUtil.isNotBlank(ouguidstr)){
                    String[] ouguid = ouguidstr.split(",");
                    for (int i = 0; i<ouguid.length; i++) {
                        if(i==ouguid.length-1){
                            sqlouguid += "'"+ouguid[i]+"'";
                        }else{
                            sqlouguid += "'"+ouguid[i]+"',";
                        }
                    }
                }
                if(StringUtil.isNotBlank(sqlouguid)){
                    list = CommonDao.getInstance().findList(
                            "select ouguid,ouname,oushortname from frame_ou where ouguid in ("+sqlouguid+")", FrameOu.class);
                }else{
                    list = CommonDao.getInstance().findList(
                            "select ouguid,ouname,oushortname from frame_ou where ouname like '%济宁市%'", FrameOu.class);
                }
            }
            for (FrameOu ou : list) {
                ouModal.add(new SelectItem(ou.getOuguid(),ou.getOuname()));
            }
        }
        return this.ouModal;
    }

	public DataGridModel<DeptSelfBuildSystem> getSelfBuildSystemModel() {
		if (selfBuildSystemModel == null) {
			selfBuildSystemModel = new DataGridModel<DeptSelfBuildSystem>() {

				@Override
				public List<DeptSelfBuildSystem> fetchData(int startIndex, int pageSize, String sortFeild,
						String sortOrder) {
					PageData<DeptSelfBuildSystem> pageData = selfBuildSystemService.getSelfBuildSystemPage(param,
							new PageParam(startIndex, pageSize));
					setRowCount(pageData.getRowCount());
					return pageData.getList();
				}
			};
		}
		return selfBuildSystemModel;
	}
	
    //自建系统图标
    public FileUploadModel9 getSltfileUploadModel() {
        if (sltfileUploadModel == null) {
            sltfileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(sltcliengguid, null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return sltfileUploadModel;
    }

	public void saveOrUpdate() {
		CommonDao commonDao = CommonDao.getInstance();
		commonDao.beginTransaction();
		try {
			selfBuildSystemService.saveOrUpdateSelfBuildSystem(selfBuildSystem);
			commonDao.commitTransaction();
			addCallbackParam("msg", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonDao.rollBackTransaction();
			addCallbackParam("msg", "保存失败");
		}
	}

	public List<SelectItem> getApiTypes() {
		if (apiTypes == null) {
			apiTypes = selfBuildSystemService.getApiTypes();
		}
		return apiTypes;
	}
	
	public List<SelectItem> getSelfBuildSystemList() {
		if (selfBuildSystemList == null) {
			selfBuildSystemList = selfBuildSystemService.getSelfBuildSystemSIList();
		}
		return selfBuildSystemList;
	}
	
	public void deleteRows() {
		List<String> rowguids = getSelfBuildSystemModel().getSelectKeys();
		selfBuildSystemService.deleteSelfBuildSystems(rowguids);
	}

	public DeptSelfBuildSystem getSelfBuildSystem() {
		return selfBuildSystem;
	}

	public void setSelfBuildSystem(DeptSelfBuildSystem selfBuildSystem) {
		this.selfBuildSystem = selfBuildSystem;
	}

	public DeptSelfBuildSystem getParam() {
		return param;
	}

	public void setParam(DeptSelfBuildSystem param) {
		this.param = param;
	}

}
