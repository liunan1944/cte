package com.ln.test.action;

import java.util.HashMap;
import java.util.Map;

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
import com.cte.credit.api.dto.DataSource;
import com.ln.test.DataSourceService;

/**
 * @Title: 征信数据源主服务接口
 * @Package com.wanda.credit.ds.action
 * @Description: 征信数据源主服务接口
 * @author chenglin.xiao
 * @date 2016年6月17日 下午12:16:36
 * @version V1.0
 */
@Controller
@RequestMapping(value="service")
public class MainDataServiceAction {
    private final Logger logger = LoggerFactory.getLogger(MainDataServiceAction.class);
    @Autowired
    private DataSourceService dataSourceService;
    @RequestMapping(value = "fetch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> service(final HttpServletResponse response,
                                      final HttpServletRequest request,
                                      @PathVariable final String trade_id ,
                                      @RequestBody final DataSource ds)
            throws Exception {
        String prefix = trade_id +" "+ Conts.KEY_SYS_AGENT_HEADER; //流水号标识
        logger.info("{} 收到HTTP请求!",prefix);
        request.setCharacterEncoding("utf-8");
        Map<String, Object> rets = new HashMap<String, Object>();
        rets =  dataSourceService.fetch(trade_id,ds);
        logger.info("{} HTTP请求处理完成!",prefix);
        return rets;
    }
}
