package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午9:05:24 
 * @版本：V1.0
 */
public class GetUsersResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	
	private long   total;
    private int    count;
    private Openid data;
    @JSONField(name = "next_openid")
    private String nextOpenid;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Openid getData() {
        return data;
    }

    public void setData(Openid data) {
        this.data = data;
    }

    public String getNextOpenid() {
        return nextOpenid;
    }

    public void setNextOpenid(String nextOpenid) {
        this.nextOpenid = nextOpenid;
    }

    public class Openid implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String[] openid;

        public String[] getOpenid() {
            return openid;
        }

        public void setOpenid(String[] openid) {
            this.openid = openid;
        }
    }
}
