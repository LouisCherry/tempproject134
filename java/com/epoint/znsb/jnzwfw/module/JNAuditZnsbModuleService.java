package com.epoint.znsb.jnzwfw.module;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class JNAuditZnsbModuleService
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public int findMacListCountByConfigType(String macaddress, String centerguid, String moduleconfigtype) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where Macaddress=? and CenterGuid =? and Enable='1' and ifnull(moduleconfigtype,'0')=? order by Ordernum desc";
        return commonDao.queryInt(sql, macaddress, centerguid, moduleconfigtype);
    }

    public int findMacListCount(String macaddress, String centerguid) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where Macaddress=? and CenterGuid =? and Enable='1' order by Ordernum desc";
        return commonDao.queryInt(sql, macaddress, centerguid);
    }

    public int findCenterListCount(String centerguid) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "select count(1) from Audit_Znsb_SelfmachineModule where (Macaddress is null or Macaddress='') and CenterGuid =? and Enable='1' order by Ordernum desc";
        return commonDao.queryInt(sql, centerguid);
    }

    public List<AuditZnsbSelfmachinemodule> findMacListByConfigTypeAndLabel(String macaddress, String centerguid,
            String moduleconfigtype, String lableguid) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from  audit_znsb_selfmachinemodule a LEFT JOIN audit_znsb_ytjextend b  on a.RowGuid = b.moduleguid where  b.lableguid = ? and Macaddress= ? and CenterGuid =? and Enable='1' and ifnull(moduleconfigtype,'0')=?  and ifnull(b.is_show,'0')!='1' order by b.Ordernum desc limit 4";
        }
        return commonDao.findList(sql, AuditZnsbSelfmachinemodule.class, lableguid, macaddress, centerguid,
                moduleconfigtype);
    }

    public List<AuditZnsbSelfmachinemodule> findMacListCountAndLabel(String macaddress, String centerguid,
            String lableguid) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from  audit_znsb_selfmachinemodule a LEFT JOIN audit_znsb_ytjextend b  on a.RowGuid = b.moduleguid where  b.lableguid = ? and Macaddress=? and CenterGuid =? and Enable='1' and ifnull(b.is_show,'0')!='1'    order by b.Ordernum desc limit 4;";
        }
        return commonDao.findList(sql, AuditZnsbSelfmachinemodule.class, lableguid, macaddress, centerguid);
    }

    public List<AuditZnsbSelfmachinemodule> findCenterListCountAndLabel(String centerguid, String lableguid) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from  audit_znsb_selfmachinemodule a LEFT JOIN audit_znsb_ytjextend b  on a.RowGuid = b.moduleguid where  b.lableguid = ? and (Macaddress is null or Macaddress='') and CenterGuid =? and Enable='1' and ifnull(b.is_show,'0')!='1' order by b.Ordernum desc limit 4;";
        }
        return commonDao.findList(sql, AuditZnsbSelfmachinemodule.class, lableguid, centerguid);
    }

}
