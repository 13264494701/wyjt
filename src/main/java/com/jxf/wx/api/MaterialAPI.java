package com.jxf.wx.api;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.StringUtils;
import com.jxf.wx.api.entity.ArticleTemp;
import com.jxf.wx.api.response.*;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.utils.NetWorkCenter;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:38:47 
 * @版本：V1.0
 */
public class MaterialAPI extends BaseAPI {
	
	public enum MaterialType {

	    /**
	     * 图片
	     */
	    IMAGE("image"),

	    /**
	     * 语音
	     */
	    VOICE("voice"),

	    /**
	     * 视频
	     */
	    VIDEO("video"),

	    /**
	     * 图文消息
	     */
	    NEWS("news");

	    String value;

	    MaterialType(String value) {
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return this.value;
	    }
	}


	public MaterialAPI(String accessToken) {
		super(accessToken);
	}

	private static final Logger LOG = LoggerFactory.getLogger(MaterialAPI.class);

//    public MaterialAPI(ApiConfig config) {
//        super(config);
//    }

    /**
     * 上传永久素材文件。图片素材上限为5000，其他类型为1000
     * @param file 素材文件
     * @return 上传结果
     */
    public UploadMaterialResponse uploadMaterialFile(File file){
        return uploadMaterialFile(file, null, null);
    }

    /**
     * 上传永久视频素材文件。
     * @param file 素材文件
     * @param title 素材标题
     * @param introduction 素材描述信息
     * @return 上传结果
     */
    public UploadMaterialResponse uploadMaterialFile(File file, String title, String introduction){
        UploadMaterialResponse response;
        String url = "http://api.weixin.qq.com/cgi-bin/material/add_material?access_token=#";
        BaseResponse r;
        if(StringUtils.isBlank(title)) {
            r = executePost(url, null, file);
        }else{
            final Map<String, String> param = new HashMap<String, String>();
            param.put("title", title);
            param.put("introduction", introduction);
            r = executePost(url, JSON.toJSONString(param), file);
        }
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, UploadMaterialResponse.class);
        return response;
    }

    /**
     * 上传图文消息素材
     * @param articles 图文消息列表
     * @return 上传结果
     */
    public UploadMaterialResponse uploadMaterialNews(List<ArticleTemp> articles){
        UploadMaterialResponse response;
        String url = BASE_API_URL + "cgi-bin/material/add_news?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("articles", articles);
        BaseResponse r = executePost(url, JSON.toJSONString(params));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, UploadMaterialResponse.class);
        return response;
    }

    /**
     * 下载永久素材
     * @param mediaId 素材ID
     * @param type 素材类型
     * @return 下载结果
     */
    public DownloadMaterialResponse downloadMaterial(String mediaId, MaterialType type){
        DownloadMaterialResponse response = new DownloadMaterialResponse();
        String url = BASE_API_URL + "cgi-bin/material/get_material?access_token=" + this.getAccessToken();
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(NetWorkCenter.CONNECT_TIMEOUT).setConnectTimeout(NetWorkCenter.CONNECT_TIMEOUT).setSocketTimeout(NetWorkCenter.CONNECT_TIMEOUT).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpPost request = new HttpPost(url);
        StringEntity mediaEntity = new StringEntity("{\"media_id\":\"" + mediaId + "\"}", ContentType.APPLICATION_JSON);
        request.setEntity(mediaEntity);

        CloseableHttpResponse httpResponse = null;
        try{
            httpResponse = client.execute(request);
            if(HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()){
                HttpEntity entity;
                String resultJson;
                switch (type){
                    case NEWS:
                        entity = httpResponse.getEntity();
                        resultJson = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                        response = JSON.parseObject(resultJson, DownloadMaterialResponse.class);
                        LOG.debug("-----------------请求成功-----------------");
                        LOG.debug("响应结果:");
                        LOG.debug(resultJson);
                        if (StringUtils.isBlank(response.getErrcode())) {
                            response.setErrcode("0");
                            response.setErrmsg(resultJson);
                        }
                        break;
                    case VIDEO:
                        entity = httpResponse.getEntity();
                        resultJson = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                        LOG.debug("-----------------请求成功-----------------");
                        LOG.debug("响应结果:");
                        LOG.debug(resultJson);
                        response = JSON.parseObject(resultJson, DownloadMaterialResponse.class);
                        if (StringUtils.isBlank(response.getErrcode())) {
                            response.setErrcode("0");
                            response.setErrmsg(resultJson);
                            // 通过down_url下载文件。文件放置在content中。通过writeTo方法获取
                            downloadVideo(response);
                        }
                        break;
                    default:
                        Header length = httpResponse.getHeaders("Content-Length")[0];
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        response.setContent(inputStream, Integer.valueOf(length.getValue()));
                        break;
                }
            }else{
                response.setErrcode(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                response.setErrmsg("请求失败");
            }
        } catch (IOException e) {
            LOG.error("IO流异常", e);
        } catch (Exception e) {
            LOG.error("其他异常", e);
        }

        return response;
    }

    /**
     * 获取已创建永久素材的数量
     * @return 永久素材数量结果
     */
    public GetMaterialTotalCountResponse countMaterial(){
        GetMaterialTotalCountResponse response = null;
        String url = BASE_API_URL + "cgi-bin/material/get_materialcount?access_token=#";
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetMaterialTotalCountResponse.class);
        return response;
    }

    /**
     * 获取素材列表
     * @param type 素材类型
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     * @return 素材列表结果
     */
    public GetMaterialListResponse batchGetMaterial(MaterialType type, int offset, int count){
        if(offset < 0) offset = 0;
        if(count > 20) count = 20;
        if(count < 1) count = 1;

        GetMaterialListResponse response = null;
        String url = BASE_API_URL + "cgi-bin/material/batchget_material?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>(4, 1);
        params.put("type", type.toString());
        params.put("offset", offset);
        params.put("count", count);
        BaseResponse r = executePost(url, JSON.toJSONString(params));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetMaterialListResponse.class);

        return response;
    }

    /**
     * 删除一个永久素材
     * @param mediaId 素材ID
     * @return 删除结果
     */
    public ResultType deleteMaterial(String mediaId) {
        String url = BASE_API_URL + "cgi-bin/material/del_material?access_token=#";
        final Map<String, String> param = new HashMap<String, String>();
        param.put("media_id", mediaId);
        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }

    private void downloadVideo(DownloadMaterialResponse response){
        String url = response.getDownUrl();
        LOG.debug("Download url: " + url);
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(NetWorkCenter.CONNECT_TIMEOUT).setConnectTimeout(NetWorkCenter.CONNECT_TIMEOUT).setSocketTimeout(NetWorkCenter.CONNECT_TIMEOUT).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse r = client.execute(get);
            if (HttpStatus.SC_OK == r.getStatusLine().getStatusCode()) {
                InputStream inputStream = r.getEntity().getContent();
                Header[] headers = r.getHeaders("Content-disposition");
                Header length = r.getHeaders("Content-Length")[0];
                response.setContent(inputStream, Integer.valueOf(length.getValue()));
                response.setFileName(headers[0].getElements()[0].getParameterByName("filename").getValue());
            }
        } catch (IOException e){
            LOG.error("IO异常处理", e);
        } catch (Exception e) {
            LOG.error("其他异常", e);
        }
    }
}
