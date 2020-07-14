package com.jxf.wx.api.entity;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： 模版消息 发送接口的载体 取消继承 CrudEntity 新增page属性 
 * @类修改者： gaobo
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:30:18 
 * @版本：V1.0
 */
public class TemplateMsg {

	private String touser;
    @JSONField(name = "template_id")
    private String templateId;
    private String page;
    @JSONField(name = "form_id")
    private String formId;
    private String topcolor;
    @JSONField(name = "emphasis_keyword")
    private String emphasisKeyword;
    
    private Map<String, TemplateParam> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, TemplateParam> getData() {
        return data;
    }

    public void setData(Map<String, TemplateParam> data) {
        this.data = data;
    }

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getEmphasisKeyword() {
		return emphasisKeyword;
	}

	public void setEmphasisKeyword(String emphasisKeyword) {
		this.emphasisKeyword = emphasisKeyword;
	}
    
    
}
