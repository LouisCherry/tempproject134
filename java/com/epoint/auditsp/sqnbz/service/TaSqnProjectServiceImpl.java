package com.epoint.auditsp.sqnbz.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.auditsp.sqnbz.api.ITaSqnProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.core.grammar.Record;

@Component
@Service
public class TaSqnProjectServiceImpl implements ITaSqnProject
{

    /**
     * 
     */
    private static final long serialVersionUID = -7197876231001866812L;

    @Override
    public int isExsitXmdm(String itemcode) {
        return new TaSqnProjectService().isExsitXmdm(itemcode);
    }

    @Override
    public Record getContectInfo(String itemcode) {
        return new TaSqnProjectService().getContectInfo(itemcode);
    }

    @Override
    public Record getCjdaContectInfo(String itemcode) {
        return new TaSqnProjectService().getCjdaContectInfo(itemcode);
    }

    @Override
    public int isExsitXmspsxblxxb(String spsxblbm) {
        return new TaSqnProjectService().isExsitXmspsxblxxb(spsxblbm);
    }

    @Override
    public ParticipantsInfo getlegalByCreditcode(String creditcode) {
        return new TaSqnProjectService().getlegalByCreditcode(creditcode);
    }

    @Override
    public Record getHtxxByGcdm(String gcdm) {
        return new TaSqnProjectService().getHtxxByGcdm(gcdm);
    }

    @Override
    public void addHtxx(Record record) {
        new TaSqnProjectService().addHtxx(record);
    }

    @Override
    public void updateHtxx(Record record) {
        new TaSqnProjectService().updateHtxx(record);
    }

    @Override
    public List<AuditRsItemBaseinfo> getItemCodeBycompanynameAnditemcode(String gcdm, String companyname) {
        return new TaSqnProjectService().getItemCodeBycompanynameAnditemcode(gcdm, companyname);
    }

    @Override
    public AuditRsItemBaseinfo getitemDetailByItemcode(String gcdm) {
        return new TaSqnProjectService().getitemDetailByItemcode(gcdm);
    }

    @Override
    public List<AuditSpSpGcjsxk> getAuditSpSpGcjsxkByItemcode(String gcdm) {
        return new TaSqnProjectService().getAuditSpSpGcjsxkByItemcode(gcdm);
    }

    @Override
    public List<String> getTaskIdList(String basetaskguid) {
        // TODO Auto-generated method stub
        return new TaSqnProjectService().getTaskIdList(basetaskguid);
    }
}
