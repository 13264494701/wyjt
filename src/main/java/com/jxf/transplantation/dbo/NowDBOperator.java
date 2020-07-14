package com.jxf.transplantation.dbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.config.Global;
import com.jxf.svc.utils.Exceptions;

/**
 * 数据库操作
 * 
 * @author JinXinFu
 * 
 */
public class NowDBOperator {

	private static Logger log = LoggerFactory.getLogger(NowDBOperator.class);

	private Connection con;
	private PreparedStatement prsm = null;
    private Statement s;
    private ResultSet rs;

	/**
	 * 获取数据库连接对象
	 * 
	 * @return
	 */

	private Connection getConnection() {
		try {
			// 加载jdbc
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 获取数据库对象
			con = DriverManager.getConnection(Global.getConfig("jdbc.url"),
					Global.getConfig("jdbc.username"),Global.getConfig("jdbc.password"));
//     		con = DriverManager.getConnection("jdbc:mysql://39.106.108.149:3306/wyjt_dev_bak?useUnicode=true&autoReconnect=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai","wyjt","wyjt");
			if (con != null) {
				log.debug("数据库连接成功！");
			}
		} catch (ClassNotFoundException e) {
			log.error("类没有找到！{}",Exceptions.getStackTraceAsString(e));
			log.error("数据库连接失败！");
		} catch (Exception e) {
			log.error("数据库连接失败！{}", Exceptions.getStackTraceAsString(e));
		} finally {
			
		}
		return con;
	}
	public String convertHexToString(String hex){

		  StringBuilder sb = new StringBuilder();
		  StringBuilder temp = new StringBuilder();

		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
		  for( int i=0; i<hex.length()-1; i+=2 ){

		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);

		      temp.append(decimal);
		  }

		  return sb.toString();
	}
	/**
	 * 执行 带 参数的 sql 语句 获取 PreparedStatement 对象
	 * 
	 * @param sql
	 *            String
	 * @return PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedStatement(String sql)
			throws SQLException {
		if (con == null) {
			getConnection();
		}
		prsm = con.prepareStatement(sql);
		return prsm;
	}

	
    /**
     * 关闭数据库
     * 
     * @wuff
     *
     */
    public void close() {

        closeResultSet();
        closeStatement();
        closePreparedStatement();
        closeConnection();
    }
    
    /**
     *  关闭 ResultSet 对象
     */
    private void closeResultSet() {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
            	log.error(Exceptions.getStackTraceAsString(e));
            }
        }
    }
    
    /**
     * 关闭 Statement 对象
     */
    private void closeStatement() {
        if (s != null) {
            try {
                s.close();
                s = null;
            } catch (SQLException e) {
            	log.error(Exceptions.getStackTraceAsString(e));
            }
        }
    }
    
    
    /**
     * 关闭 PreparedStatement 对象
     */
    private void closePreparedStatement() {
        if (prsm != null) {
            try {
                prsm.close();
                prsm = null;
            } catch (SQLException e) {
            	log.error(Exceptions.getStackTraceAsString(e));
            }

        }
    }
    
    
    /**
     * 关闭自动提交服务
     * @throws SQLException 
     */
    
    public  void getAutoCommit(boolean f) throws SQLException {
		if (con == null) {
			getConnection();
		}
    	con.setAutoCommit(f);
    }
    /**
     * commit提交服务
     * @throws SQLException
     */
    public  void getCommit() throws SQLException{
		if (con == null) {
			getConnection();
		}
    	con.commit();
    	
    }
    /**
     * 关闭 连接
     */
    private void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
            }
        } catch (SQLException ex) {
        }
    }

}
