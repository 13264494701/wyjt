package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月5日 下午3:44:02
 * @功能说明:根据姓名或电话查找借条
 */
public class FindByUsernameOrNameRequestParam {

	/** 电话号或姓名 */
	private String usernameOrName;
	
	public String getUsernameOrName() {
		return usernameOrName;
	}

	public void setUsernameOrName(String usernameOrName) {
		this.usernameOrName = usernameOrName;
	}

}
