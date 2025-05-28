package com.epoint.newtranslation.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

public class SyncToRightOracleNewDao
{
    protected ICommonDao commonDao;

    transient Logger log = LogUtil.getLog(SyncToRightOracleNewDao.class);
    private static String region_code = ConfigUtil.getConfigValue("SunRight", "region_code");

    public SyncToRightOracleNewDao(DataSourceConfig dataSourceConfig) {
        //浪潮前置库
        commonDao = CommonDao.getInstance(dataSourceConfig);
    }

    public List<Record> getQLSXOU() {
        // String sql = "select DISTINCT ORG_CODE from QLT_QLSX_SD where
        // NVL(EX_FLAG1,'0') = '0' and NVL(ORG_CODE,'') is not null and rownum =
        // 1";//正式语句
        String sql = "select DISTINCT ORG_CODE from QLT_QLSX_SD where NVL(EX_FLAG1,'0') = '0' and NVL(ORG_CODE,'0')!=0 and rownum=1";
        
        // String sql = "select DISTINCT ORG_CODE from QLT_QLSX_SD where
        // NVL(EX_FLAG1,'0') = '0' and NVL(ORG_CODE,'') is not null and
        // REGION_CODE='370990000000'";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class, new Object[] {});
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }

    /**
     * 获取浪潮前置库事项信息
     * @authory shibin
     * @version 2019年10月30日 上午9:43:59
     * @param orgCode
     * @return
     */
    public List<Record> selectRight(String orgCode) {

        //String sql = "select ROWGUID,UPDATE_TYPE from QLT_QLSX_SD where ORG_CODE=? AND NVL(EX_FLAG1,'0')='0' AND ROWNUM<=6 ORDER BY ITEM_CODE,UPDATE_DATE asc";
        String sql = "select ROWGUID,UPDATE_TYPE from qlt_qlsx_hb_new where NVL(EX_FLAG,'0')='0' AND TYPE in('QZ','JL','ZS','CF','JF','GG','CJ','QR','XK','JC','QT') and ROWNUM<=6 ORDER BY ITEM_CODE,UPDATE_DATE asc";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class, orgCode);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }

    /**
     * @Description:按照权力编码和时间先后获取待同步权力
     * @author:许烨斌
     * @time 2019年3月20日下午3:45:00
     * @return
     */
    public List<Record> selectRightnew() {
        //String sql = "select * from (select * from qlt_qlsx_hb_new where nvl(EX_FLAG,'0')='0'  and UPDATE_TYPE in('I','U','C') ORDER BY REGION_CODE,ITEM_CODE,version asc) where rownum<=20";
        //String sql = "select * from (select * from qlt_qlsx_hb_new where nvl(EX_FLAG,'0')='0'  and UPDATE_TYPE in('I','U','C') ORDER BY REGION_CODE,ITEM_CODE,version asc) where rownum<=20";
        String sql = "";
        if (StringUtil.isBlank(region_code)) {
            sql = "select * from (select * from qlt_qlsx_hb_new where EX_FLAG ='0' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=20";
        }
        else {
            sql = "select * from (select * from qlt_qlsx_hb_new where nvl(EX_FLAG,'0')='0' and region_code='"
                    + region_code + "' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=20";
        }
//        sql = "select * from (select * from qlt_qlsx_hb_new where EX_FLAG ='0' and ORG_CODE = '370900SPJ' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=20";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }
    /**
     * @Description:按照权力编码和时间先后获取待同步权力
     * @author:许烨斌
     * @time 2019年3月20日下午3:45:00
     * @return
     */
    public List<Record> selectAgencyOrganRightnew() {
        //String sql = "select * from (select * from qlt_qlsx_hb_new where nvl(EX_FLAG,'0')='0'  and UPDATE_TYPE in('I','U','C') ORDER BY REGION_CODE,ITEM_CODE,version asc) where rownum<=20";
        //String sql = "select * from (select * from qlt_qlsx_hb_new where nvl(EX_FLAG,'0')='0'  and UPDATE_TYPE in('I','U','C') ORDER BY REGION_CODE,ITEM_CODE,version asc) where rownum<=20";
        String sql = "select * from qlt_qlsx_hb_new where rowguid in ('03660F0431E64AA8A24D2DE748F5E33B','0631286D00044FE0B113DE257FA79B24','06EA0E2C70814C1183A6311F9097A152','08F2643A6DD244C4892A134ADF8C588D','1204D5D9FCAF416F8DF3B422512AEBBD','1AD14EE41CD7432EB6A25733F4A600EA','2621A38A8F854CD699AA9A8BEBF14A93','33C5703A496D4EBA939E35EE8C34980D','377CBA18FDBC42D5B480F3074C0F37EF','3789869C7F9147D18E01BF6F5B26915D','38EF16C0B1B241CCAB43AA930EE6E853','423C542B4CBD4CA9BF385A5F1FD93982','42438888F6794C749E9A53DA4560E882','4B15B74E41934DC998EBA0E75DB43878','4F7D6E0476AB4AAAAA68038E685F156A','5395D1D6C71B446F8E3D344CAB5B56CB','54D6EE1C3E474009BBB35F368E052F59','582095C1CBC1481B9B743426AC061008','59CD96510E1F4EB1AEF37BC68E7CAEB6','5C0833E106BD4E54BDC8B32BEDC80471','60CB2A094C6D4D2D9129A9F6408B8215','613F01CD6C944148A802BF9416153CAB','6302C05772D7449896A738B5CA390F2C','6B37B0E02C0748FC8CACB21DAB4EEE5D','7237F456480F47B08A9256A57101869F','7BC87E0F12E54F1897D3061A0B864965','83301346B89E4B8995AEBCC3C0DB3B9E','834C0321AA624EB0BB8ED3AD893F7BE6','85782999EADD4DE1BCAA91323E7AD621','899490D191594B8CA2FFBA96BB1798E2','8FA8D188D9B84E67AD83C49779A92093','9047B65912F344009FF5D3D23F3BB547','9BFF0C0923AE486582F220B4797E34C5','9EFB7224D54E44F38B6F1C234863206F','A3F87FA3740A41EBA9DAE048D446389B','A5C656F0120A45E7BE7D678E33D96F44','A7C430EDE12A40179C65D696939698E7','ACA42683979A470FB3A8599FB50EC240','AEBF5D99BA7A4919BEC15FF0C375A5D6','B2C23AA360B74D45B4CB5665BA1B62E1','B8EFEEA73DB344AEB847F84AAB07CBA0','BE6EDABEBC164D2C9CC82262BF70E93C','C98C49A562D44DAE978330DB5CE81D8E','D30AB0EBEFE74406ADCD7341D3894E91','D36033F546294255A8EA8E7118FB8B57','D6DB40688AD24175B96DB3B6262FE465','DED5CE5CBBB64DF8B9E218D928CBC595','DFB8352D3A614670A56DD717F8704C58','E0510901349D43BBAAA843FB53735D3A','E8427D7979DD42B99DE506FBF3711DE4','EF7A36D41572477DB2ACA4344ED0387A','F174D230B655478D994400283CA7C0D2','F7A39BEC889746CDB3E60168C3EB46DB','FB4D9F05F311492E8514E639C8314160') "; 
//        sql = "select * from (select * from qlt_qlsx_hb_new where EX_FLAG ='0' and ORG_CODE = '370900SPJ' ORDER BY update_date,region_code,ITEM_CODE,version asc) where rownum<=20";
        String taskguids ="'03660F0431E64AA8A24D2DE748F5E33B','0631286D00044FE0B113DE257FA79B24','06EA0E2C70814C1183A6311F9097A152','08F2643A6DD244C4892A134ADF8C588D','1204D5D9FCAF416F8DF3B422512AEBBD','1AD14EE41CD7432EB6A25733F4A600EA','2621A38A8F854CD699AA9A8BEBF14A93','33C5703A496D4EBA939E35EE8C34980D','377CBA18FDBC42D5B480F3074C0F37EF','3789869C7F9147D18E01BF6F5B26915D','38EF16C0B1B241CCAB43AA930EE6E853','423C542B4CBD4CA9BF385A5F1FD93982','42438888F6794C749E9A53DA4560E882','4B15B74E41934DC998EBA0E75DB43878','4F7D6E0476AB4AAAAA68038E685F156A','5395D1D6C71B446F8E3D344CAB5B56CB','54D6EE1C3E474009BBB35F368E052F59','582095C1CBC1481B9B743426AC061008','59CD96510E1F4EB1AEF37BC68E7CAEB6','5C0833E106BD4E54BDC8B32BEDC80471','60CB2A094C6D4D2D9129A9F6408B8215','613F01CD6C944148A802BF9416153CAB','6302C05772D7449896A738B5CA390F2C','6B37B0E02C0748FC8CACB21DAB4EEE5D','7237F456480F47B08A9256A57101869F','7BC87E0F12E54F1897D3061A0B864965','83301346B89E4B8995AEBCC3C0DB3B9E','834C0321AA624EB0BB8ED3AD893F7BE6','85782999EADD4DE1BCAA91323E7AD621','899490D191594B8CA2FFBA96BB1798E2','8FA8D188D9B84E67AD83C49779A92093','9047B65912F344009FF5D3D23F3BB547','9BFF0C0923AE486582F220B4797E34C5','9EFB7224D54E44F38B6F1C234863206F','A3F87FA3740A41EBA9DAE048D446389B','A5C656F0120A45E7BE7D678E33D96F44','A7C430EDE12A40179C65D696939698E7','ACA42683979A470FB3A8599FB50EC240','AEBF5D99BA7A4919BEC15FF0C375A5D6','B2C23AA360B74D45B4CB5665BA1B62E1','B8EFEEA73DB344AEB847F84AAB07CBA0','BE6EDABEBC164D2C9CC82262BF70E93C','C98C49A562D44DAE978330DB5CE81D8E','D30AB0EBEFE74406ADCD7341D3894E91','D36033F546294255A8EA8E7118FB8B57','D6DB40688AD24175B96DB3B6262FE465','DED5CE5CBBB64DF8B9E218D928CBC595','DFB8352D3A614670A56DD717F8704C58','E0510901349D43BBAAA843FB53735D3A','E8427D7979DD42B99DE506FBF3711DE4','EF7A36D41572477DB2ACA4344ED0387A','F174D230B655478D994400283CA7C0D2','F7A39BEC889746CDB3E60168C3EB46DB','FB4D9F05F311492E8514E639C8314160'";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }

    
    public List<Record> selectRightByRowGuid(String rowGuid, String updateType) {
        String sql = "select * from qlt_qlsx_hb_new where NVL(EX_FLAG1,'0')='0' AND RowGuid=? and UPDATE_TYPE=? ORDER BY UPDATE_DATE asc";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class, rowGuid, updateType);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }

    /**
     *  更新前置库同步状态位
     *  @param rowGuid
     *  @param updateType
     *  @param flag
     */
    public void updateSyncSign(String rowGuid, String updateType, String flag) {
        String sql = "Update qlt_qlsx_hb_new set EX_FLAG=?,EX_DATE=SYSDATE where RowGuid=? and UPDATE_TYPE=?";
        long begintime = new Date().getTime();
        commonDao.execute(sql, flag, rowGuid, updateType);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
    }

    public List<Record> getfileInfo(String filePath) {
        String sql = "select file_content,file_name from QLT_QLSX_OF_FILE_NEW where FILE_PATH=?";
        long begintime = new Date().getTime();
        List<Record> list = commonDao.findList(sql, Record.class, filePath);
        long endtime = new Date().getTime();
        log.info("查询耗时：" + (endtime - begintime));
        commonDao.close();
        return list;
    }

    /*
     * public void Update_SYNC_SIGN(String rowGuid){ String sql =
     * "Update QLT_QLSX_SD set EX_FLAG1='1',EX_DATE=SYSDATE where RowGuid=?";
     * commonDao.execute(sql, rowGuid); commonDao.close(); }
     */
}
