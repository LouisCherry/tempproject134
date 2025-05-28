package com.epoint.knowledge.audit.service;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.knowledge.audit.impl.CnsKinfoQuestionImpl;
import com.epoint.knowledge.audit.inter.ICnsKinfoQuestion;
import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;

public class CnsKinfoQuestionService extends CnsCommonService<CnsKinfoQuestion>
{
   // private ICnsKinfoQuestion kinfoQuestionImpl=new CnsKinfoQuestionImpl();
    
    @Override
    protected ICnsCommon<CnsKinfoQuestion> getICnsCommon() {
        // TODO Auto-generated method stub
        return new CnsKinfoQuestionImpl();
    }
    //获取本知识的所有提问数据且已解答的
    public List<CnsKinfoQuestion> getListByKinfoGuid(String guid) {
        String sql = "select * from cns_kinfo_question where iskinfo='1' "
                    + " and answerstatus = '20' and kinfoguid ='"+guid+"'";
        List<CnsKinfoQuestion> list = CommonDao.getInstance().findList(sql, CnsKinfoQuestion.class);
        
        return list;
    }

}
