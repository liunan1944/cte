package com.cte.credit.ds.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.ds.DataSourceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: 征信数据源主服务接口
 * @Package com.wanda.credit.ds.action
 * @Description: 征信数据源主服务接口
 * @author chenglin.xiao
 * @date 2016年6月17日 下午12:16:36
 * @version V1.0
 */
@Controller
@RequestMapping(value="/inner/service/dataService")
public class MainDataServiceAction {
    private final Logger logger = LoggerFactory.getLogger(MainDataServiceAction.class);
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
	PropertyUtil propertyEngine;
    @RequestMapping(value = "fetch/{trade_id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> service(final HttpServletResponse response,
                                      final HttpServletRequest request,
                                      @PathVariable final String trade_id ,
                                      @RequestBody final DataSource ds)
            throws Exception {
        String prefix = trade_id +" "+ Conts.KEY_SYS_AGENT_HEADER; //流水号标识
        logger.info("{} 收到HTTP请求!",prefix);
        request.setCharacterEncoding("utf-8");
        boolean is_mock = "1".equals(propertyEngine.readById("sys_public_mock_switch"));
        Map<String, Object> rets = new HashMap();
        if(is_mock){
        	logger.info("{} action请求走mock!", prefix);
        	rets.put(Conts.KEY_RET_DATA, null);
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
			rets.put(Conts.KEY_RET_MSG, "采集成功!");
			return rets;
        }else{
        	if(!StringUtil.isEmpty(trade_id) && ds!=null){
                rets =  dataSourceService.fetch(trade_id,ds);
            }
        }     
        logger.info("{} HTTP请求处理完成!",prefix);
        return rets;
    }
    
    @RequestMapping(value = "testfetch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> test(final HttpServletResponse response,
                                      final HttpServletRequest request,
                                      @RequestBody final DataSource ds)
            throws Exception {
    	String trade_id = StringUtil.getRandomNo();
        return this.service(response, request, trade_id, ds);
    }
}
