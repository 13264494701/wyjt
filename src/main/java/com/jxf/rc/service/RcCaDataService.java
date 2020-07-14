package com.jxf.rc.service;


import java.util.ArrayList;
import java.util.Map;

import com.jxf.mem.entity.Member;
import com.jxf.rc.entity.RcCaData;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.wyjt.app.member.CaAuthResponseResult.UserBriefLegalize;
import com.jxf.web.model.wyjt.app.member.CreditReportResponseResult.CreditTable;


/**
 * 信用报告数据表Service
 * @author lmy
 * @version 2018-12-17
 */
public interface RcCaDataService extends CrudService<RcCaData>{
	
	/**
	   * 统计认证数量
	 * @param merber
	 * @return
	 */
	int  getRenZhengNum(Member merber);
	
	
	
	/**
	 * 获取根据的认证项信息(我的认证界面使用)
	 * @param data
	 * @return
	 */
	UserBriefLegalize  getRenZhengData(RcCaData data,int type);
	
	
	/**
	   * 根据手机号和认证类型获取信用档案
	 * @param 
	 * @return
	 */
	RcCaData  getByPhoneNoAndType(String phoneNo,RcCaData.Type type);
	
	
	/**
	 * 详细档案数据源
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesShuJuYuan(Map<String,Object> userMap);
	
	/**
	 * 详细档案风险排查
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	CreditTable getCreditarchivesFengXian(Map<String, Object> userMap,Map<String, Object> mapYY,String mapXX, Member Buser );
	
	/**
	 * 详细档案借贷信息
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesJieDai(Member Buser);
	
	/**
	 * 详细档案逾期
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesYuQi(Member Buser);
	
	
	/**
	 * 详细档案芝麻分数
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesZhiMa(Map<String, Object> userMap);
	
	
	/**
	 * 详细档案紧急联系人
	 * @param userMap
	 * @return   查看人的id， userId
	 *   被查看人的id  serachId
	 * @throws WebDataException
	 */
	
	CreditTable getCreditarchivesJinJiV2(Map<String, Object> map3,Map<String, Object> map4,Member member, Member friend);
	
	
	/**
	 * 详细档案手机号分析
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesMobileNum(Map<String, Object> userMap,Member Buser);
	
	
	
	
	/**
	 * 详细档案消费能力
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesYYover(Map<String, Object> userMap);
	
	/**
	 * 详细档案淘宝消费
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	public  CreditTable   getCreditarchivesTBover(Map<String, Object> userMap);
	
	/**
	 * 详细档案淘宝绑定支付宝
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	public  CreditTable   getCreditarchivesTBPayover(Map<String, Object> userMap);
	
	
	/**
	 * 详细档案居住地址
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	 CreditTable  getCreditarchivesaddress(Map<String, Object> userMap,Member Buser);
	
	/**
	 * 详细档案社保 
	 * @param userMap
	 * type 来源  0：详细档案  1不详细档案
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable  getCreditarchivesSheBao(Map<String, Object> userMap,Member Buser,int type);
	
	
	/**
	 * 详细档案公积金
	 * @param userMap
	 * type 来源  0：详细档案  1不详细档案
	 * @return
	 * @throws WebDataException
	 */
	
	 CreditTable  getCreditarchivesGongJing(Map<String, Object> userMap,Member Buser,int type);
	
	/**
	 * 详细档案学信网
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesXX(String userMap);
	
	
	
	/**
	 * 详细档案网银
	 * @param userMap                  
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable getCreditarchivesWYJieJi(Map<String, Object> userMap,Member loadById);
	
	/**
	 * 详细档案网银信用卡
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesWYXinYong(Map<String, Object> userMap,Member loadById);
	
	
	/**
	 * 获得新运营商的综合评分
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgZHF(Map<String, Object> userMap);
	
	/**
	 * 获得新运营商的联系人风险
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgLxFx(Map<String, Object> userMap);
	
	
	
	/**
	 * 获得新运营商的催收风险
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgCsFx(Map<String, Object> userMap);
	

	/**
	 * 获得新运营商的风险联系人
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
    CreditTable   getCreditarchivesYYBgFXlr(Map<String, Object> userMap);
	
	
	/**
	 * 获得新运营商的金融风险
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	 CreditTable   getCreditarchivesYYBgJrLr(Map<String, Object> userMap);
	
	
	/**
	 * 获得新运营商的通话统计
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgThTj(Map<String, Object> userMap);
	
	/**
	 * 获得新运营商运营商消费情况
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
   CreditTable   getCreditarchivesYYBgHFXF(Map<String, Object> userMap,Map<String, Object> userMapold);
	
	/**
	 * 获得新运营商运营静默时间
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgJMTJ(Map<String, Object> userMap);
	
	
	
	/**
	 * 获得新运营商运各个时间段
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgSJTJ(Map<String, Object> userMap);
	
	
	/**
	 * 获得新运营商通话详情2.0
	 * @param userMap
	 * @return
	 * @throws WebDataException
	 */
	
	CreditTable   getCreditarchivesYYBgTHXQ(Map<String, Object> userMap,Member Buser);
	
	/**
	 * 获得接待数据分析数据
	 * @param userId
	 * @param type  // 0:全部；1：一周；2：一个月；3:6个月；4:6个月前；
	 * @return
	 * @throws WebDataException
	 */
	
    ArrayList<String>  getloaninforCreditRecord(Member user,int type);
	
    
    /**
	 * 获得逾期记录数据
	 * @param userId
	 * @param type  // 0:全部；1：一周；2：一个月；3:6个月；4:6个月前；
	 * @return
	 * @throws WebDataException
	 */
	
	ArrayList<String>  getoverloaninforCreditRecord(Member user,int type);
	
	
	/**
	 * 紧急联系人
	 * @param userId
	 * @param nameF
	 * @param moblieF
	 * @param nameM
	 * @param moblieM
	 * @param nameL
	 * @param moblieL
	 * @return
	 */
	void createEmergencyContact(Member member, String nameF, String moblieF, String nameM, String moblieM,String nameL,String moblieL);
	
	
	public  String getZmToken(String name, String phoneNo, String idNo,String sequenceNo);
	

	/**
	 * 获取天机h5 地址
	 * @param member
	 * @param type
	 * @return
	 */
	String getTJAddr(Member member, int type);
	/**
	 * 获取公信宝token
	 * @param member
	 * @param type
	 * @return
	 */
	String getGxbToken(Member member, int type);



}
