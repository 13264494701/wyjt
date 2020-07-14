package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @类功能说明： 添加模版响应
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午3:42:38 
 * @版本：V1.0
 */
public class AddTemplateResponse extends BaseResponse {
    /** 
	 * @Fields serialVersionUID : TODO 
	 */ 
	private static final long serialVersionUID = 1L;
	/**
     * 模版id
     */
    @JSONField(name = "template_id")
    private String templateId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
