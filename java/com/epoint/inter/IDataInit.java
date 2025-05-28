package com.epoint.inter;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

public interface IDataInit {
    public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public void initData();
}
