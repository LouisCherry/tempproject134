/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;


public class CnsUserService {
	
	private FrameRoleService9 frameRoleService = new FrameRoleService9();
	private FrameUserService9 frameUserService = new FrameUserService9();
	private FrameOuService9 frameOuService = new FrameOuService9();
	ICommonDao dao ; 
	public List<FrameUser> getUserListByOu(String ouguid, String roleName, int level) {
		List frameUserList = null;
		String roleGuid = "";
		if (StringUtil.isNotBlank(roleName)) {
			roleGuid = this.frameRoleService.getRoleGuidByRoleName(roleName);
		}
		if(StringUtil.isBlank(roleGuid)){
		    return null;
		}
		frameUserList = this.frameUserService.listUserByOuGuid(ouguid, roleGuid, "", "", false, false, true, level);
		return frameUserList;
	}
	public List<FrameUser> getUserByRoleNameAndCategoryguid(String roleName, String categoryguid) {
	    dao = CommonDao.getInstance();
	    String ouguid = dao.find("select ouguid from cns_kinfo_category where rowguid='"+categoryguid+"'", String.class, null);
	    List<FrameUser> users = frameUserService.listAllUser();
        List<FrameUser> frameUserList = new ArrayList<FrameUser>();;
        for(FrameUser user :users){
            String userguid = user.getUserGuid();
            String roles =  frameRoleService.listRoleByUserGuid(userguid).toString();
            if(user.getOuGuid().equals(ouguid)&&roles.contains(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE)){
                frameUserList.add(user);
            }
        }
        

        return frameUserList;
    }
	
	public List<FrameUser> getUserByRoleNameAndAreacode(String roleName, String areacode) {
		
		List frameUserList = null;
		if (StringUtil.isNotBlank(roleName)) {
			String roleGuid = this.frameRoleService.getRoleGuidByRoleName(roleName);
			if (StringUtil.isNotBlank(roleGuid)) {
				frameUserList = this.frameUserService.listUserByOuGuid("", roleGuid, "", (String) null, false, false,
						false, 3);
//				frameUserList.removeIf((frameUser) -> {
//					return !ouguidList.contains(frameUser.getOuGuid());
//				});
			}
		}

		return frameUserList;
	}

	public List<FrameUser> getUserByRoleName(String roleName) {
		List frameUserList = null;
		if (StringUtil.isNotBlank(roleName)) {
			String roleGuid = this.frameRoleService.getRoleGuidByRoleName(roleName);
			if (StringUtil.isNotBlank(roleGuid)) {
				frameUserList = this.frameUserService.listUserByOuGuid("", roleGuid, "", (String) null, false, false,
						false, 3);
			}
		}

		return frameUserList;
	}

	public List<FrameUser> getUserListByOuList(String ouguidList, String roleName, int level) {
		ArrayList frameUserList = null;
		String roleGuid = "";
		if (StringUtil.isNotBlank(roleName)) {
			roleGuid = this.frameRoleService.getRoleGuidByRoleName(roleName);
		}

		if (StringUtil.isNotBlank(ouguidList)) {
			String[] ouGuids = ouguidList.split(",");
			if (ouGuids != null && ouGuids.length > 0) {
				frameUserList = new ArrayList();
				String[] arg6 = ouGuids;
				int arg7 = ouGuids.length;

				for (int arg8 = 0; arg8 < arg7; ++arg8) {
					String ouguid = arg6[arg8];
					List userList = this.frameUserService.listUserByOuGuid(ouguid, roleGuid, "", "", false, false,
							true, level);
					frameUserList.addAll(userList);
				}
			}
		}

		return frameUserList;
	}

	public Set<FrameOu> getOuSetByRole(String roleName) {
		HashSet ouSet = new HashSet();
		List userList = this.getUserByRoleName(roleName);
		if (userList != null && userList.size() > 0) {
			Iterator arg3 = userList.iterator();

			while (arg3.hasNext()) {
				FrameUser frameUser = (FrameUser) arg3.next();
				ouSet.add(this.frameOuService.getOuByUserGuid(frameUser.getUserGuid()));
			}
		}

		return ouSet;
	}

	public List<FrameOu> getOuListByRole(String roleName) {
		ArrayList ouList = new ArrayList();
		Set ouSet = this.getOuSetByRole(roleName);
		if (ouSet != null && ouSet.size() > 0) {
			Iterator arg3 = ouSet.iterator();

			while (arg3.hasNext()) {
				FrameOu frameOu = (FrameOu) arg3.next();
				ouList.add(frameOu);
			}
		}

		return ouList;
	}

	public Set<FrameOu> getOuSetByRoleAndAreacode(String roleName, String areacode) {
		HashSet ouSet = new HashSet();
		List userList = this.getUserByRoleNameAndAreacode(roleName, areacode);
		if (userList != null && userList.size() > 0) {
			Iterator arg4 = userList.iterator();

			while (arg4.hasNext()) {
				FrameUser frameUser = (FrameUser) arg4.next();
				ouSet.add(this.frameOuService.getOuByUserGuid(frameUser.getUserGuid()));
			}
		}

		return ouSet;
	}

	public List<FrameOu> getOuListByRoleAndAreacode(String roleName, String areacode) {
		ArrayList ouList = new ArrayList();
		Set ouSet = this.getOuSetByRoleAndAreacode(roleName, areacode);
		if (ouSet != null && ouSet.size() > 0) {
			Iterator arg4 = ouSet.iterator();

			while (arg4.hasNext()) {
				FrameOu frameOu = (FrameOu) arg4.next();
				ouList.add(frameOu);
			}
		}

		return ouList;
	}
}