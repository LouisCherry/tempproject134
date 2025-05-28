package com.epoint.screen.api;

import org.springframework.stereotype.Service;

@Service
public interface ILyAuditVisitCount {
	
	/**
     * 查询访问量
     * 
     * 
     * @return int
     */
    public int findVisitcount(String searchtimefrom, String searchtimeto);
    
    /**
     * 查询注册量
     * 
     * 
     * @return int
     */
    public int findRegistercount();
    
}
