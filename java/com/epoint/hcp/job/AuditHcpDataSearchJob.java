package com.epoint.hcp.job;

/**
 * 好差评统计数据服务
 */
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;
import com.epoint.hcp.service.AuditLocalHcpDataCountService;

@DisallowConcurrentExecution
public class AuditHcpDataSearchJob implements Job
{

    transient Logger log = LogUtil.getLog(AuditHcpDataSearchJob.class);

    /**
     * 本地service
     */

    public AuditHcpDataSearchJob() {

    }

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("好差评统计服务开始");
            EpointFrameDsManager.begin(null);
            doClean();
            log.info("好差评统计服务完成");
            EpointFrameDsManager.commit();

        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();

        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public void doClean() {
        AuditLocalHcpDataCountService dataCountService = new AuditLocalHcpDataCountService();

        List<AuditHcpAreainfo> arealist = dataCountService.getAreaCountList();
        //同步全省各地区
        for (AuditHcpAreainfo area : arealist) {
        	String areacode = area.getAreacode();
            AuditHcpAreainfo info = dataCountService.findArea(area.getAreacode());
            if("370800".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+546079+19852+13614+10061+3646);
            	area.setPjcount(area.getPjcount()+639246+31378+25245+17623+4861);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+639246+31378+25245+17623+4861);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+638649+31373+25225+17618+4850);
            	area.setMynum(area.getMynum()+472+4+20+5+10);
            	area.setJbmynum(area.getJbmynum()+125+1+1);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+607482+30712+24565+17373+4352);
            	area.setYdnum(area.getYdnum()+34+72+195);
            	area.setPadnum(area.getPadnum()+10912+656+680+65+80);
            	area.setYtjnum(area.getYtjnum()+30+47+73);
            }
            else if ("370811".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+124157+409228+188695+148737+124429);
            	area.setPjcount(area.getPjcount()+229917+760268+354173+228782+172766);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+229917+760268+354173+228782+172766);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+229910+760268+354168+228771+172731);
            	area.setMynum(area.getMynum()+2+3+3+20);
            	area.setJbmynum(area.getJbmynum()+5+2+8+15);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+229912+760268+345778+213397+213397+150896);
            	area.setYdnum(area.getYdnum()+0+3737+7230);
            	area.setPadnum(area.getPadnum()+5+8395+6023+3611);
            	area.setYtjnum(area.getYtjnum()+0+1847+3738);
            }
            else if ("370812".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+500902+435517+262941+65378+382708);
            	area.setPjcount(area.getPjcount()+996067+861917+523888+92471+531879);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+996067+861917+523888+92471+531879);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+996067+861917+523888+92469+531879);
            	area.setMynum(area.getMynum()+0+2);
            	area.setJbmynum(area.getJbmynum()+0);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+995996+861917+522987+92125+531817);
            	area.setYdnum(area.getYdnum()+0+80+24);
            	area.setPadnum(area.getPadnum()+0+901+140+11);
            	area.setYtjnum(area.getYtjnum()+0+45+12);
            }
            else if ("370826".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+377795+562237+651842+434367+418957);
            	area.setPjcount(area.getPjcount()+706884+1112874+1299672+736477+589455);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+706884+1112874+1299672+736477+589455);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+706556+1112874+1299671+736453+589224);
            	area.setMynum(area.getMynum()+247+1+24+221);
            	area.setJbmynum(area.getJbmynum()+81+10);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+701260+1112753+1299636+732293+557677);
            	area.setYdnum(area.getYdnum()+49+1363+10638);
            	area.setPadnum(area.getPadnum()+5346+120+36+706+5182);
            	area.setYtjnum(area.getYtjnum()+39+683+5257);
            }
            else if ("370827".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+614626+440634+614250+605219+334878);
            	area.setPjcount(area.getPjcount()+1224443+872521+1225740+979684+462972);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1224443+872521+1225740+979684+462972);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1224417+872521+1225740+979684+462972);
            	area.setMynum(area.getMynum()+10);
            	area.setJbmynum(area.getJbmynum()+16);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1223761+871946+1225720+979682+462964);
            	area.setYdnum(area.getYdnum()+3+3);
            	area.setPadnum(area.getPadnum()+677+575+20+1+2);
            	area.setYtjnum(area.getYtjnum()+0+1+1);
            }
            else if ("370828".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+666335+624559+382946+215299+263220);
            	area.setPjcount(area.getPjcount()+1309942+1234539+759758+345665+362803);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1309942+1234539+759758+345665+362803);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1309723+1234505+759658+345657+362743);
            	area.setMynum(area.getMynum()+142+29+94+5+58);
            	area.setJbmynum(area.getJbmynum()+77+5+6+3+2);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1302680+1228523+753507+343856+361544);
            	area.setYdnum(area.getYdnum()+14+493+419);
            	area.setPadnum(area.getPadnum()+7094+6016+6251+603+193);
            	area.setYtjnum(area.getYtjnum()+12+225+196);
            }
            else if ("370829".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+423333+948786+342315+421572+453951);
            	area.setPjcount(area.getPjcount()+808719+1806978+658995+671388+641267);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+808719+1806978+658995+671388+641267);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+805832+1806978+658955+660070+596721);
            	area.setMynum(area.getMynum()+2170+24+11273+44531);
            	area.setJbmynum(area.getJbmynum()+717+16+45+15);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+807260+1806978+644529+619491+466257);
            	area.setYdnum(area.getYdnum()+143+11478+58631);
            	area.setPadnum(area.getPadnum()+763+14466+23445+28756);
            	area.setYtjnum(area.getYtjnum()+320+5614+29262);
            }
            else if ("370830".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+483754+774637+489099+131138+172718);
            	area.setPjcount(area.getPjcount()+948845+1533158+976101+216663+234837);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+948845+1533158+976101+216663+234837);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+948130+1533158+976101+216663+234837);
            	area.setMynum(area.getMynum()+505);
            	area.setJbmynum(area.getJbmynum()+210);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+948234+1533158+976101+216663+234837);
            	area.setYdnum(area.getYdnum()+71);
            	area.setPadnum(area.getPadnum()+244);
            	area.setYtjnum(area.getYtjnum()+118);
            }
            else if ("370831".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+635399+622118+316770+445650+336236);
            	area.setPjcount(area.getPjcount()+1259986+1233355+631723+716220+458878);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1259976+1233355+631723+716220+458875);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1258796+1233350+631722+716220);
            	area.setMynum(area.getMynum()+876+4+1);
            	area.setJbmynum(area.getJbmynum()+304+1+3);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1245986+1230828+631232+715534+455004);
            	area.setYdnum(area.getYdnum()+41+219+1282);
            	area.setPadnum(area.getPadnum()+13686+2527+491+120+652);
            	area.setYtjnum(area.getYtjnum()+63+126+656);
            }
            else if ("370832".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+453067+372860+644108+417014+360711);
            	area.setPjcount(area.getPjcount()+909738+737703+1287723+665607+502887);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+909738+737703+1287723+665607+502887);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+909704+737703+1287723+665607+502886);
            	area.setMynum(area.getMynum()+27+1);
            	area.setJbmynum(area.getJbmynum()+7);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+896741+737703+1287471+665480+502200);
            	area.setYdnum(area.getYdnum()+12+36+229);
            	area.setPadnum(area.getPadnum()+12793+252+20+125);
            	area.setYtjnum(area.getYtjnum()+0+24+105);
            }
            else if ("370881".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+366409+636271+599612+228357+341636);
            	area.setPjcount(area.getPjcount()+732220+1259261+1190677+366543+489362);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+732215+1259261+1190677+366543+489362);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+732107+1259261+1190676+366541+489362);
            	area.setMynum(area.getMynum()+79+1+2);
            	area.setJbmynum(area.getJbmynum()+29);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+716535+1258534+1189348+365493+488713);
            	area.setYdnum(area.getYdnum()+6+189+225);
            	area.setPadnum(area.getPadnum()+15473+727+1329+539+108);
            	area.setYtjnum(area.getYtjnum()+6+107+113);
            }
            else if ("370882".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+220069);
            	area.setPjcount(area.getPjcount()+436744);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+436744);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+436644);
            	area.setMynum(area.getMynum()+78);
            	area.setJbmynum(area.getJbmynum()+22);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+436710);
            	area.setYdnum(area.getYdnum()+3);
            	area.setPadnum(area.getPadnum()+23);
            	area.setYtjnum(area.getYtjnum()+8);
            }
            else if ("370883".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+846083+808608+283924+277132+307216);
            	area.setPjcount(area.getPjcount()+1664402+1594201+557279+417847+430477);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1664402+1594201+557273+417843+430477);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1664203+1594198+557249+417810+430421);
            	area.setMynum(area.getMynum()+116+2+10+12+30);
            	area.setJbmynum(area.getJbmynum()+83+1+14+21+19);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1643642+1589810+542703+396264+401134);
            	area.setYdnum(area.getYdnum()+21+6261+9699);
            	area.setPadnum(area.getPadnum()+20629+4391+14576+6034+4903);
            	area.setYtjnum(area.getYtjnum()+24+3091+4942);
            }
            else if ("370890".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+451457+642162+640990+538297+892265);
            	area.setPjcount(area.getPjcount()+899298+1265289+1254604+930984+1276137);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+899298+1265289+1254604+930984+1276137);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+899107+1265263+1254548+930788+1276137);
            	area.setMynum(area.getMynum()+135+19+36+181+19);
            	area.setJbmynum(area.getJbmynum()+56+7+20+15+12);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+895759+1246972+1223695+897889+1251311);
            	area.setYdnum(area.getYdnum()+24+6945+8240);
            	area.setPadnum(area.getPadnum()+3484+18317+30909+15676+4233);
            	area.setYtjnum(area.getYtjnum()+17+3511+4089);
            }
            else if ("370891".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+507353+547753+267802+282194+619355);
            	area.setPjcount(area.getPjcount()+1005115+1081973+534566+441982+834193);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1005115+1081973+534566+441982+834193);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1004992+1081938+534562+441981+834193);
            	area.setMynum(area.getMynum()+115+35+4+1);
            	area.setJbmynum(area.getJbmynum()+8);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1001342+1081266+534401+441934+834175);
            	area.setYdnum(area.getYdnum()+3+16+7);
            	area.setPadnum(area.getPadnum()+3766+707+165+7+4);
            	area.setYtjnum(area.getYtjnum()+2+10+2);
            }
            else if ("370892".equals(areacode)) {
            	area.setBjcount(area.getBjcount()+916433+958767+364067+201153+246100);
            	area.setPjcount(area.getPjcount()+1822561+1898769+725957+320079+345939);
            	area.setCpcount(area.getCpcount()+0);
            	area.setMycount(area.getMycount()+1822561+1898769+725957+320079+345939);
            	area.setMyd((area.getDouble("Myd")+100.00)/2+"");
            	area.setCpzgcount(area.getCpzgcount()+0);
            	area.setZgl("0.00");
            	area.setFcmynum(area.getFcmynum()+1820857+1898769+725957+320079+345939);
            	area.setMynum(area.getMynum()+1144);
            	area.setJbmynum(area.getJbmynum()+560);
            	area.setBmynum(area.getBmynum()+0);
            	area.setFcbmynum(area.getFcbmynum()+0);
            	area.setPcnum(area.getPcnum()+1821287+1898630+725811+320067+345890);
            	area.setYdnum(area.getYdnum()+158+1+15);
            	area.setPadnum(area.getPadnum()+689+139+146+8+11);
            	area.setYtjnum(area.getYtjnum()+257+10);
            }
            
            if (info != null) {
                dataCountService.updateAuditHcpAreainfo(area.getFcbmynum()+"",area.getMynum()+"",area.getYdnum()+"",area.getCpzgcount()+"",area.getJbmynum()+"",area.getMycount()+"",area.getBmynum()+"",area.getPcnum()+"",area.getBjcount()+"",area.getCpcount()+"",area.getPjcount()+"",area.getPadnum()+"",area.getMyd(),area.getFcmynum()+"",area.getYtjnum()+"",area.getZgl()+"",area.getAreacode());
            }
            else {
                area.setRowguid(UUID.randomUUID().toString());
                area.setOperatedate(new Date());
                area.setAreaname(dataCountService.getAreaNameByareaCode(area.getAreacode()));
                dataCountService.insert(area);
            }
        }

        // 部门
        List<AuditHcpOuinfo> oulist = dataCountService.getOuCountList();
        
        List<Record> records = dataCountService.getOldEvaOuList();
        
        
        
         for (AuditHcpOuinfo ouinfo : oulist) {
            AuditHcpOuinfo ou = dataCountService.findOu(ouinfo.getOucode());
            for (Record record :records ) {
        		if(record.getStr("oucode").equals(ouinfo.getOucode())) {
        			ouinfo.set("bjcount", record.getInt("bjcount")+ouinfo.getInt("bjcount"));
        			ouinfo.set("pjcount", record.getInt("pjcount")+ouinfo.getInt("pjcount"));
        			ouinfo.set("cpcount", record.getInt("cpcount")+ouinfo.getInt("cpcount"));
        			ouinfo.set("mycount", record.getInt("mycount")+ouinfo.getInt("mycount"));
        			ouinfo.set("myd", (record.getDouble("myd")+ouinfo.getDouble("myd"))/2+"");
        			continue;
        		}
            }
            
            if (ou != null) {
                dataCountService.updateAuditHcpOuinfo(ouinfo.getBjcount()+"",ouinfo.getPjcount()+"",ouinfo.getCpcount()+"",ouinfo.getMycount()+"",ouinfo.getMyd(),ouinfo.getOucode());
            }
            else {
                ouinfo.setRowguid(UUID.randomUUID().toString());
                ouinfo.setOperatedate(new Date());
                dataCountService.insert(ouinfo);
            }
        }

    }

}
