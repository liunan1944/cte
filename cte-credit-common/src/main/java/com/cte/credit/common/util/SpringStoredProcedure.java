package com.cte.credit.common.util;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
/**
* @Title: SpringStoredProcedure.java 
* @Package com.gbicc.irs.util 
* @Description: 存储过程定义类(Spring)
* @version V1.0
 */
public class SpringStoredProcedure extends StoredProcedure {
    public ArrayList<HashMap> alist = new ArrayList<HashMap>();
    private Map inParam; 
     
    public class RowMapper implements RowCallbackHandler {
        public void processRow(ResultSet rs) throws SQLException{
            int rowCount = rs.getRow(); 
            int count = rs.getMetaData().getColumnCount();
            String[] header = new String[count];
            for(int i=0;i<count;i++)
                header[i] = rs.getMetaData().getColumnName(i+1);    
            do{                             
                HashMap<String,String> row = new HashMap(count+7);
                for(int i=0;i<count;i++)
                    row.put(header[i],rs.getString(i+1));            
                alist.add(row);
            }while(rs.next());
        }
    };  
    
    public SpringStoredProcedure(DataSource ds, String SQL) {
        setDataSource(ds);
        setSql(SQL);
    }
    public void setOutParameter(String column,int type){
        declareParameter(new SqlOutParameter(column, type,new RowMapper()));//利用回调句柄注册输出参数
    }
    public void setParameter(String column,int type){
        declareParameter(new SqlParameter(column, type)); //设置输入输数
    }
    public void SetInParam(Map inParam){
        this.inParam = inParam;
    }
    public Map execute() {
        compile();            //编译
        if(this.inParam!=null)
        	return super.execute(this.inParam);
        else
        	return super.execute();
    }
}
