package com.jxf.wx.api;

import com.alibaba.fastjson.JSON;
import com.jxf.wx.api.entity.Industry;
import com.jxf.wx.api.entity.TemplateMsg;
import com.jxf.wx.api.exception.WeixinException;
import com.jxf.wx.api.response.AddTemplateResponse;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.response.SendTemplateResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 模版消息 api
 */
public class TemplateMsgAPI extends BaseAPI {
	
	private static final Logger LOG = LoggerFactory.getLogger(TemplateMsgAPI.class);
	
	public TemplateMsgAPI(String accessToken) {
		super(accessToken);
	}

    /**
     * 设置行业
     *
     * @param industry 行业参数
     * @return 操作结果
     */
    public ResultType setIndustry(Industry industry) {
        LOG.debug("设置行业......");
        Assert.notNull(industry,"");
        String url = BASE_API_URL + "cgi-bin/template/api_set_industry?access_token=#";
        BaseResponse response = executePost(url, JSON.toJSONString(industry));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 添加模版
     *
     * @param shortTemplateId 模版短id
     * @return 操作结果
     */
    public AddTemplateResponse addTemplate(String shortTemplateId) {
        LOG.debug("获取模版id......");
        Assert.notNull(shortTemplateId,"");
        String url = BASE_API_URL + "cgi-bin/template/api_add_template?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("template_id_short", shortTemplateId);
        BaseResponse r = executePost(url, JSON.toJSONString(params));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() :JSON.toJSONString(r);
        AddTemplateResponse result = JSON.parseObject(resultJson, AddTemplateResponse.class);
        return result;
    }

    /**
     * 发送模版消息
     *
     * @param msg 消息
     * @return 发送结果
     */
    public SendTemplateResponse send(TemplateMsg msg) {

        LOG.debug("获取模版发送结果......");
        Assert.notNull(msg,"");
        Assert.notNull(msg.getTouser(),"");
        Assert.notNull(msg.getTemplateId(),"");
        Assert.notNull(msg.getData(),"");
        Assert.notNull(msg.getTopcolor(),"");
        Assert.notNull(msg.getPage(),"");
        Assert.notNull(msg.getEmphasisKeyword(),"");
        Assert.notNull(msg.getFormId(),"");
        String url = BASE_API_URL + "cgi-bin/message/wxopen/template/send?access_token=#";
        SendTemplateResponse result = null;
        /** 调用核心接口 获取模板发送结果通知*/
        BaseResponse r = executePost(url, JSON.toJSONString(msg));
        if(r != null) {
        	if(isSuccess(r.getErrcode())) {
        		result  = JSON.parseObject(r.getErrmsg(), SendTemplateResponse.class);
        	}else {
        		throw new WeixinException("微信模板发送结果获取出错，错误信息:"
						+ r.getErrcode() + ","
						+ r.getErrmsg());
        	}
        }else {
        	throw new WeixinException("调用接口异常");
        }
        return result;
    }


}
