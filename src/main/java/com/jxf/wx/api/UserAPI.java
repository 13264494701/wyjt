package com.jxf.wx.api;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.StringUtils;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.response.CreateGroupResponse;
import com.jxf.wx.api.response.GetGroupsResponse;
import com.jxf.wx.api.response.GetUserInfoResponse;
import com.jxf.wx.api.response.GetUsersResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理相关API
 *
 * @author peiyu
 * @since 1.2
 */
public class UserAPI extends BaseAPI {

	public UserAPI(String accessToken) {
		super(accessToken);
	}

	private static final Logger LOG = LoggerFactory.getLogger(UserAPI.class);

//    public UserAPI(ApiConfig config) {
//        super(config);
//    }

    /**
     * 获取关注者列表
     *
     * @param nextOpenid 下一个用户的ID
     * @return 关注者列表对象
     */
    public GetUsersResponse getUsers(String nextOpenid) {
        GetUsersResponse response = null;
        LOG.debug("获取关注者列表.....");
        String url = BASE_API_URL + "cgi-bin/user/get?access_token=#";
        if (StringUtils.isNotBlank(nextOpenid)) {
            url += "&next_openid=" + nextOpenid;
        }
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetUsersResponse.class);
        return response;
    }

    /**
     * 设置关注者备注
     *
     * @param openid 关注者ID
     * @param remark 备注内容
     * @return 调用结果
     */
    public ResultType setUserRemark(String openid, String remark) {
   
   
        LOG.debug("设置关注者备注.....");
        String url = BASE_API_URL + "cgi-bin/user/info/updateremark?access_token=#";
        Map<String, String> param = new HashMap<String, String>();
        param.put("openid", openid);
        param.put("remark", remark);
        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 创建分组
     *
     * @param name 分组名称
     * @return 返回对象，包含分组的ID和名称信息
     */
    public CreateGroupResponse createGroup(String name) {
        CreateGroupResponse response = null;
       
        LOG.debug("创建分组.....");
        String url = BASE_API_URL + "cgi-bin/groups/create?access_token=#";
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> group = new HashMap<String, Object>();
        group.put("name", name);
        param.put("group", group);
        BaseResponse r = executePost(url, JSON.toJSONString(param));
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() :JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, CreateGroupResponse.class);
        return response;
    }

    /**
     * 获取所有分组信息
     *
     * @return 所有分组信息列表对象
     */
    public GetGroupsResponse getGroups() {
        GetGroupsResponse response = null;
        LOG.debug("获取所有分组信息.....");
        String url = BASE_API_URL + "cgi-bin/groups/get?access_token=#";
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetGroupsResponse.class);
        return response;
    }

    /**
     * 通过关注者ID获取所在分组信息
     *
     * @param openid 关注者ID
     * @return 所在分组信息
     */
    public String getGroupIdByOpenid(String openid) {
     
        LOG.debug("通过关注者ID获取所在分组信息.....");
        String result = null;
        String url = BASE_API_URL + "cgi-bin/groups/getid?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("openid", openid);
        BaseResponse r = executePost(url, JSON.toJSONString(params));
        if (isSuccess(r.getErrcode())) {
            result = JSON.parseObject(r.getErrmsg()).getString("groupid");
        }
        return result;
    }

    /**
     * 修改分组信息
     *
     * @param groupid 分组ID
     * @param name    新名称
     * @return 调用结果
     */
    public ResultType updateGroup(Integer groupid, String name) {
 
        LOG.debug("修改分组信息.....");
        String url = BASE_API_URL + "cgi-bin/groups/update?access_token=#";
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> group = new HashMap<String, Object>();
        group.put("id", groupid);
        group.put("name", name);
        param.put("group", group);
        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 移动关注者所在分组
     *
     * @param openid    关注者ID
     * @param toGroupid 新分组ID
     * @return 调用结果
     */
    public ResultType moveGroupUser(String openid, Long toGroupid) {

        LOG.debug("移动关注者所在分组.....");
        String url = BASE_API_URL + "cgi-bin/groups/members/update?access_token=#";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("openid", openid);
        param.put("to_groupid", toGroupid);

        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 移动关注者所在分组
     *
     * @param openids    关注者ID
     * @param toGroupid 新分组ID
     * @return 调用结果
     */
    public ResultType moveGroupUser(String[] openids, String toGroupid) {
    	Assert.notNull(openids,"openids不能为空");
        Assert.notNull(toGroupid,"toGroupid不能为空");      
        LOG.debug("移动关注者所在分组.....");
        String url = BASE_API_URL + "cgi-bin/groups/members/batchupdate?access_token=#";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("openid_list", openids);
        param.put("to_groupid", toGroupid);

        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 获取关注者信息
     *
     * @param openid 关注者ID
     * @return 关注者信息对象
     */
    public GetUserInfoResponse getUserInfo(String openid) {
    
        Assert.notNull(openid,"openid不能为空");
        GetUserInfoResponse response = null;
        LOG.debug("获取关注者信息.....");
        String url = BASE_API_URL + "cgi-bin/user/info?access_token=#&lang=zh_CN&openid=" + openid;
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetUserInfoResponse.class);
        return response;
    }

    /**
     * 删除分组
     * @param groupId 分组ID
     * @return 删除结果
     */
    public ResultType deleteGroup(Integer groupId){
    	Assert.notNull(groupId,"groupId不能为空");
        LOG.debug("删除分组.....");
        String url = BASE_API_URL + "cgi-bin/groups/delete?access_token=#";
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Integer> groups = new HashMap<String, Integer>();
        groups.put("id", groupId);
        param.put("group", groups);
        BaseResponse response = executePost(url, JSON.toJSONString(param));
        return ResultType.get(response.getErrcode());
    }
}