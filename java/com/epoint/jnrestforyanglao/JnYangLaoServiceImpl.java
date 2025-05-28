package com.epoint.jnrestforyanglao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;

import com.epoint.jnrestforyanglao.JnYangLaoService;

@Component
@Service
public class JnYangLaoServiceImpl implements IJnYangLaoService
{

    @Override
    public List<Record> getYangLaoList(int pageIndex, int pageSize) {
        JnYangLaoService jnYangLaoService=new JnYangLaoService();
        return jnYangLaoService.getYangLaoList(pageIndex, pageSize);
    }

    @Override
    public List<Record> getYangLaoListByName(String cname, int pageIndex, int pageSize) {
        JnYangLaoService jnYangLaoService=new JnYangLaoService();
        return jnYangLaoService.getYangLaoListByName(cname, pageIndex, pageSize);
    }

    @Override
    public int getCount() {
        JnYangLaoService jnYangLaoService=new JnYangLaoService();
        return jnYangLaoService.getCount();
    }

    @Override
    public int getCountByCName(String cname) {
        JnYangLaoService jnYangLaoService=new JnYangLaoService();
        return jnYangLaoService.getCountByCName(cname);
    }

    @Override
    public Record findByPk(String pk_old_agency) {
        JnYangLaoService jnYangLaoService=new JnYangLaoService();
        return jnYangLaoService.findByPk(pk_old_agency);
    }

  

}
