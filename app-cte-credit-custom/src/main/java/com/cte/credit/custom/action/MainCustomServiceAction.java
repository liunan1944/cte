package com.cte.credit.custom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.custom.service.CustomCoreService;

@Controller
@RequestMapping(value="/inner/service/customService")
public class MainCustomServiceAction {
    private final Logger logger = LoggerFactory.getLogger(MainCustomServiceAction.class);

    @Autowired
    private CustomCoreService customCoreService;
    @Autowired
	PropertyUtil propertyEngine;
    @RequestMapping(value = "request/{trade_id}", method = RequestMethod.POST)
    @ResponseBody
    public CRSCoreResponse service(final HttpServletResponse response,
                                      final HttpServletRequest request,
                                      @PathVariable final String trade_id ,
                                      @RequestBody final CRSCoreRequest ds){

        CRSCoreResponse rets = new CRSCoreResponse();
        String prefix = trade_id +" "+ Conts.KEY_SYS_AGENT_HEADER; //流水号标识
        boolean is_mock = "1".equals(propertyEngine.readById("sys_public_mock_switch"));
        logger.info("{} 收到HTTP请求!", prefix);
        if(is_mock){
        	logger.info("{} action请求走mock!", prefix);
        	rets.setRetcode(Conts.KEY_STATUS_SUCCESS);
        	rets.setRetdata(null);
        	rets.setRetmsg("交易成功");
        }else{
        	if(!StringUtil.isEmpty(trade_id) && ds!=null){
                rets =  customCoreService.request(trade_id, ds);
            }
        } 
        logger.info("{} HTTP请求处理完成!",prefix);
        return rets;
    }
}
