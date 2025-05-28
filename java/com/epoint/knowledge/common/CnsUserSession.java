/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.entity.FrameRole;


public class CnsUserSession extends HashMap<String, Object> {
	private static final long serialVersionUID = 2705126480804243592L;
	private String areacode;
	private boolean isCnsDept;
	private FrameOuService9 frameOuService = new FrameOuService9();
	private FrameRoleService9 frameRoleService = new FrameRoleService9();
	
	private String userRolesName;

	public static CnsUserSession getInstance() {
		CnsUserSession session = new CnsUserSession();
		HashMap sessionObj = ContainerFactory.getContainInfo().getSessionObj();
		if (sessionObj.containsKey("cnsusersession")) {
			session = (CnsUserSession) sessionObj.get("cnsusersession");
		} else {
			session.initSession();
			ContainerFactory.getContainInfo().getSessionObj().put("cnsusersession", session);
		}

		return session;
	}

	private void initSession() {
		String userGuid = UserSession.getInstance().getUserGuid();
		FrameOu frameOu = this.frameOuService.getOuByUserGuid(userGuid);
		String areacode = this.getAreaCodeByOuGuid(frameOu.getOuguid());
		String userRolesName = this.getUserRolesNameByGuid(userGuid);
		this.setAreacode(areacode);
		this.setUserRolesName(userRolesName);
		if (userRolesName.contains("承办人")) {
			this.setCnsDept(true);
		} else {
			this.setCnsDept(false);
		}

	}

	private String getUserRolesNameByGuid(String userGuid) {
		String rolesName = "";
		List roleList = this.frameRoleService.listRoleByUserGuid(userGuid);
		FrameRole frameRole;
		if (roleList != null && roleList.size() > 0) {
			for (Iterator arg3 = roleList.iterator(); arg3
					.hasNext(); rolesName = rolesName + frameRole.getRoleName() + ";") {
				frameRole = (FrameRole) arg3.next();
			}
		}

		return rolesName;
	}

	private String getAreaCodeByOuGuid(String ouguid) {
		String areaCode = null;
		return areaCode;
	}

	public String getAreacode() {
		return this.areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public boolean isCnsDept() {
		return this.isCnsDept;
	}

	public void setCnsDept(boolean isCnsDept) {
		this.isCnsDept = isCnsDept;
	}

	public String getUserRolesName() {
		return this.userRolesName;
	}

	public void setUserRolesName(String userRolesName) {
		this.userRolesName = userRolesName;
	}
}