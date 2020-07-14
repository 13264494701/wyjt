package com.jxf.web.app.wyjt.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberFriendRelation;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberFriendRelationService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.MemUtils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsTransferRecord;
import com.jxf.nfs.entity.NfsTransferRecord.Status;
import com.jxf.nfs.service.NfsTransferRecordService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.act.ActTransferRequestParam;
import com.jxf.web.model.wyjt.app.act.CheckTransferRequestParam;
import com.jxf.web.model.wyjt.app.act.TransferListRequestParam;
import com.jxf.web.model.wyjt.app.act.TransferListResponseResult;
import com.jxf.web.model.wyjt.app.act.TransferListResponseResult.Transfer;
import com.jxf.web.model.wyjt.app.act.TransferPageResponseResult;
import com.jxf.web.model.wyjt.app.act.TransferPageResponseResult.TransferPageItem;
import com.jxf.web.model.wyjt.app.act.TransferDetailRequestParam;
import com.jxf.web.model.wyjt.app.act.TransferDetailResponseResult;
import com.jxf.web.model.wyjt.app.act.TransferPageRequestParam;
import com.jxf.web.model.wyjt.app.member.ActTrxDetailRequestParam;
import com.jxf.web.model.wyjt.app.member.ActTrxDetailResponseResult;
import com.jxf.web.model.wyjt.app.member.ActTrxListRequestParam;
import com.jxf.web.model.wyjt.app.member.ActTrxListResponseResult;
import com.jxf.web.model.wyjt.app.member.VideoVerifyResponseResult;
import com.jxf.web.model.wyjt.app.member.ActTrxListResponseResult.Trx;

/**
 *  账户流水与转账
 * @author suhuimin
 * @version 2018-10-31
 */
@Controller("wyjtAppMemberActController")
@RequestMapping(value = "${wyjtApp}/member")
public class MemberActController extends BaseController {
	@Autowired
	private MemberService memberService;
	@Autowired 
	private MemberActTrxService memberActTrxService;
	@Autowired 
	private NfsTransferRecordService nfsTransferRecordService;
	@Autowired 
	private MemberActService memberActService;
	@Autowired 
	private MemberFriendRelationService memberFriendRelationService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	
	/**
	 * 账户流水
	 * @return
	 */
	@RequestMapping(value = "actTrxList")
	@ResponseBody
	public ResponseData actTrxList(HttpServletRequest request) {
		Member member = memberService.getCurrent2();
		String param = request.getParameter("param");
		ActTrxListRequestParam reqData = JSONObject.parseObject(param,ActTrxListRequestParam.class);
		Integer group = reqData.getGroup();
		Integer pageNo = reqData.getPageNo();//当前页码
		String month = reqData.getMonth();
		String startTime = reqData.getStartTime();
		String endTime = reqData.getEndTime();
		String startMoney = reqData.getStartMoney();
		String endMoney = reqData.getEndMoney();
		String type = reqData.getType();
		Date startTimeQuery = null;
		Date endTimeQuery = null;
		BigDecimal startMoneyQuery = null;
		BigDecimal endMoneyQuery = null;
		Date nowDate = new Date();
		StringBuffer time = new StringBuffer("");
		if(StringUtils.isNotBlank(month)){
			startTimeQuery = CalendarUtil.StringToDate(month+"-01 00:00:00");
			endTimeQuery = CalendarUtil.addMonth(startTimeQuery , 1);
			time.append(month);
		} 
		if(StringUtils.isNotBlank(startTime)){
			startTimeQuery = CalendarUtil.StringToDate(startTime+" 00:00:00");
			time = time.append(startTime+"到");
		}
		if(StringUtils.isNotBlank(endTime)){
			endTimeQuery = CalendarUtil.StringToDate(endTime+" 23:59:59");
			if(StringUtils.isNotBlank(startTime)){
				time.append(endTime);
			}else{
				time.append("到"+endTime);
			}
		}
		if(StringUtils.isNotBlank(startMoney)){
			startMoneyQuery = new BigDecimal(startMoney);
		}
		if(StringUtils.isNotBlank(endMoney)){
			endMoneyQuery = new BigDecimal(endMoney);
		}
		MemberActTrx memberActTrx = new MemberActTrx();
		
		String nowDateStr = DateUtils.formatDate(nowDate, "yyyy-MM-dd"); 
		
		memberActTrx.setMember(member);
		memberActTrx.setBeginTime(startTimeQuery);
		memberActTrx.setEndTime(endTimeQuery);
		memberActTrx.setMinAmount(startMoneyQuery);
		memberActTrx.setMaxAmount(endMoneyQuery);
		Page<MemberActTrx> page = null;
		
		if(StringUtils.equals(type, "1")){//1 近三个月的充值   2近三个月的转账   3近三个月的放款  4 近三个月的收款 
			startTimeQuery = CalendarUtil.addMonth(nowDate , -3);
			String startTimeStr = DateUtils.formatDate(startTimeQuery, "yyyy-MM-dd");
			time.append(startTimeStr);
			time.append("到"+nowDateStr);
			memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_RECHARGE);
			memberActTrx.setDrc("D");
		}else if(StringUtils.equals(type, "2")){
			startTimeQuery = CalendarUtil.addMonth(nowDate , -3);
			String startTimeStr = DateUtils.formatDate(startTimeQuery, "yyyy-MM-dd");
			time.append(startTimeStr);
			time.append("到"+nowDateStr);
			memberActTrx.setTrxGroup(MemberActTrx.Group.transfer);
		}else if(StringUtils.equals(type, "3")){
			startTimeQuery = CalendarUtil.addMonth(nowDate , -3);
			String startTimeStr = DateUtils.formatDate(startTimeQuery, "yyyy-MM-dd");
			time.append(startTimeStr);
			time.append("到"+nowDateStr);
			memberActTrx.setTrxGroup(MemberActTrx.Group.loan);
			memberActTrx.setDrc("C");
			page = memberActTrxService.findLoanTrxListPage(memberActTrx,pageNo,20);
		}else if(StringUtils.equals(type, "4")){
			startTimeQuery = CalendarUtil.addMonth(nowDate , -3);
			String startTimeStr = DateUtils.formatDate(startTimeQuery, "yyyy-MM-dd");
			time.append(startTimeStr);
			time.append("到"+nowDateStr);
			memberActTrx.setTrxGroup(MemberActTrx.Group.repayment);
			memberActTrx.setDrc("D");
			page = memberActTrxService.findCollectionTrxListPage(memberActTrx,pageNo,20);
		}
		
		if(group != -1 && !StringUtils.equals(type, "2")&& !StringUtils.equals(type, "1")){
			memberActTrx.setTrxGroup(MemberActTrx.Group.values()[group]);
			page = memberActTrxService.findActTrxListPage(memberActTrx,pageNo,20);
		}else if(!StringUtils.equals(type, "4")&& !StringUtils.equals(type, "3")){
			page = memberActTrxService.findAllActTrxListPage(memberActTrx,pageNo,20);
		}
		List<MemberActTrx> list = page.getList();
		MemberActTrx countTotal = null;
		if(list != null && list.size()>0){
			memberActTrx.setPage(null);
			countTotal = memberActTrxService.countTotalAccountByGroup(memberActTrx); 
		}
		ActTrxListResponseResult result = new ActTrxListResponseResult();
		if(countTotal != null){
			BigDecimal totalIncome = countTotal.getTotalIncome();
			BigDecimal totalExpenditure = countTotal.getTotalExpenditure();
			if(totalIncome != null){
				result.setTotalIncome(StringUtils.decimalToStr(totalIncome, 2));
			}else{
				result.setTotalIncome("0");
			}
			if(totalExpenditure != null){
				result.setTotalExpenditure(StringUtils.decimalToStr(totalExpenditure, 2));
			}else{
				result.setTotalExpenditure("0");
			}
		}
		List<Trx> trxList = result.getTrxList();
		for (MemberActTrx actTrx : page.getList()) {
			Trx trx = new ActTrxListResponseResult().new Trx();
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			trx.setAmount(decimalFormat.format(actTrx.getTrxAmt()));
			trx.setTrxTime(DateUtils.formatDateTime(actTrx.getCreateTime()));
			trx.setDrc(actTrx.getDrc().equals("D")?1:0);
			trx.setTitle(actTrx.getTitle());
			trx.setTrxId(String.valueOf(actTrx.getId()));
			String image = memberActTrxService.getImage(actTrx);
			trx.setImgUrl(image);
			trxList.add(trx);
		}
		result.setTrxList(trxList);
		result.setTime(time.toString());
		return ResponseData.success("查询流水成功",result);
	}
	
	/**
	 * 流水详情
	 * @return
	 */
	@RequestMapping(value = "actTrxDetail")
	@ResponseBody
	public ResponseData actTrxDetail(HttpServletRequest request) {
		String param = request.getParameter("param");
		ActTrxDetailRequestParam reqData = JSONObject.parseObject(param,ActTrxDetailRequestParam.class);
		MemberActTrx memberActTrx = memberActTrxService.get(Long.valueOf(reqData.getTrxId()));
		ActTrxDetailResponseResult result = new ActTrxDetailResponseResult();
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		result.setAmount(decimalFormat.format(memberActTrx.getTrxAmt()));
		result.setTrxTime(DateUtils.formatDateTime(memberActTrx.getCreateTime()));
		result.setDrc(memberActTrx.getDrc().equals("D")?1:0);
		result.setRmk(memberActTrx.getRmk());
		result.setTitle(memberActTrx.getTitle());
		String image = memberActTrxService.getImage(memberActTrx);
		result.setImgUrl(image);
		
		result.setIdNo(memberActTrx.getId().toString());
		return ResponseData.success("查询详情成功",result);
	}
	/**
	 * 转账
	 * @return
	 */
	@RequestMapping(value = "transfer")
	@ResponseBody
	public ResponseData transfer(HttpServletRequest request) {
		Member member = memberService.getCurrent();//我转账给别人 我相当于loaner
		String param = request.getParameter("param");
		ActTransferRequestParam reqData = JSONObject.parseObject(param,ActTransferRequestParam.class);
		String reqAmount = reqData.getAmount();
		String payPwd = reqData.getPayPwd();
		String reqFriendId = reqData.getFriendId();
		//校验支付密码
		ResponseData checkPwd = memberService.checkPayPwd(payPwd, member);
		if(checkPwd.getCode() != 0){
			return ResponseData.error(checkPwd.getMessage());
		}
		
		Long friendId = Long.valueOf(reqFriendId);
		Member friend = memberService.get(friendId);
		String rmk = reqData.getRmk();
		BigDecimal amount = new BigDecimal(reqAmount);
		//转账限制
		ResponseData check = memberActService.checkCanTransfer(member,friend,amount);
		if(check.getCode() != 0){	
			return ResponseData.error(check.getMessage());
		}
		NfsTransferRecord transferRecord = new NfsTransferRecord();
		transferRecord.setAmount(amount);
		transferRecord.setStatus(NfsTransferRecord.Status.pendingReview);
		transferRecord.setFriend(friend);
		transferRecord.setMember(member);
		if(StringUtils.isNotBlank(rmk)){
			transferRecord.setRmk(rmk);
		}
		nfsTransferRecordService.save(transferRecord);
		
		//创建视频认证实体
		MemberVideoVerify memberVideoVerify = new MemberVideoVerify();
		memberVideoVerify.setMember(member);
		memberVideoVerify.setType(MemberVideoVerify.Type.transfer);
		memberVideoVerify.setStatus(MemberVideoVerify.Status.pendingReview);
		memberVideoVerify.setChannel(MemberVideoVerify.Channel.app);
		memberVideoVerify.setTrxId(transferRecord.getId());
		memberVideoVerify.setIdNo(member.getIdNo());
		memberVideoVerifyService.save(memberVideoVerify);
		
		//异步通知
		VideoVerifyResponseResult result = new VideoVerifyResponseResult();
		result = memberVideoVerifyService.getResult(3,memberVideoVerify.getId().toString(),member);
		
		return ResponseData.success("操作成功，人脸认证成功后即可到账",result);
	}
	/**
	 * 点好友时校验能否转账
	 * @return
	 */
	@RequestMapping(value = "checkTransfer")
	@ResponseBody
	public ResponseData checkTransfer(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String param = request.getParameter("param");
		CheckTransferRequestParam reqData = JSONObject.parseObject(param,CheckTransferRequestParam.class);
		String friendId = reqData.getFriendId();
		Member friend = memberService.get(Long.parseLong(friendId));
		MemberFriendRelation memberFriendRelation = memberFriendRelationService.findByMemberIdAndFriendId(friend.getId(),me.getId());
		if(memberFriendRelation == null){
			return ResponseData.error("该好友已将您移除好友列表,请重新添加后再操作");
		}
		Integer friendsVerifiedList = friend.getVerifiedList();
		Boolean verified = VerifiedUtils.isVerified(friendsVerifiedList, 1) && 
				VerifiedUtils.isVerified(friendsVerifiedList, 2);//身份和视频认证
		if(!verified){
			return ResponseData.error("您的好友还未认证,为了您的资金安全,暂时无法进行此操作");
		}
		return ResponseData.success("可以转账");
	}
	
	/**
	 * 获取跟好友的转账历史页面
	 * @return
	 */
	@RequestMapping(value = "transferPage")
	@ResponseBody
	public ResponseData transferPage(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String param = request.getParameter("param");
		TransferPageRequestParam reqData = JSONObject.parseObject(param,TransferPageRequestParam.class);
		String friendId = reqData.getFriendId();
		Member friend = memberService.get(Long.parseLong(friendId));
		TransferPageResponseResult result = new TransferPageResponseResult();
		List<TransferPageItem> transferPageItems = result.getTransferPageItems();
		
		List<NfsTransferRecord> transferList = nfsTransferRecordService.findByMemberIdAndFriendId(me.getId(), friend.getId());
		if(transferList != null && transferList.size() > 0){
			for (NfsTransferRecord transfer : transferList) {
				TransferPageItem transferPageItem = new TransferPageResponseResult().new TransferPageItem();
				transferPageItem.setAmount(transfer.getAmount().intValue());
				transferPageItem.setTransferId(transfer.getId().toString());
				transferPageItem.setNote(transfer.getRmk());
				transferPageItem.setTime(DateUtils.formatDate(transfer.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				
				Status status = transfer.getStatus();
				Member member = transfer.getMember();//发起转账的用户
				if(status.equals(Status.alreadyReceived)){
					if(member.getId().equals(me.getId())){//我是发起者
						transferPageItem.setStatus("对方"+status.getName());
					}else{
						transferPageItem.setStatus("您"+status.getName());
					}
				}else{
					transferPageItem.setStatus(status.getName());
				}
				if(member.getId().equals(me.getId())){//我是发起者
					transferPageItem.setLocation(0);
				}else{
					transferPageItem.setLocation(1);
				}
				transferPageItems.add(transferPageItem);
			}
		}
		
		result.setTransferPageItems(transferPageItems);
		result.setMemberName(friend.getName());
		result.setLeftHeadImage(friend.getHeadImage());
		result.setRightHeadImage(me.getHeadImage());
		MemUtils.mask(friend);
		result.setFriendUsername(friend.getUsername());
		return ResponseData.success("转账页查询成功",result);
	}
	/**
	 * 获取跟好友的转账详情页
	 * @return
	 */
	@RequestMapping(value = "transferDetail")
	@ResponseBody
	public ResponseData transferDetail(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String param = request.getParameter("param");
		TransferDetailRequestParam reqData = JSONObject.parseObject(param,TransferDetailRequestParam.class);
		String transferId = reqData.getTransferId();
		
		NfsTransferRecord transferRecord = nfsTransferRecordService.get(Long.parseLong(transferId));
		TransferDetailResponseResult result = new TransferDetailResponseResult();
		result.setAmount(transferRecord.getAmount().intValue() + "");
		
		if(transferRecord.getStatus().equals(NfsTransferRecord.Status.alreadyReceived)){
			result.setCurrentStates("转账成功");
		}else{
			result.setCurrentStates("转账失败-人脸认证失败");
		}
		result.setRmk(transferRecord.getRmk());
		result.setTime(DateUtils.formatDate(transferRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
		result.setTransferId(transferRecord.getId().toString());
		
		Member member = transferRecord.getMember();
		Member friend = transferRecord.getFriend();
		friend = memberService.get(friend);
		if(member.getId().equals(me.getId())){//我发起的
			result.setTransferType("转账给"+friend.getName());
			result.setType(1);
		}else{
			result.setTransferType("收到" + friend.getName() + "的转账");
			result.setType(0);
		}
		return ResponseData.success("转账详情查询成功",result);
	}
	/**
	 * 我的转账记录
	 * @return
	 */
	@RequestMapping(value = "transferList")
	@ResponseBody
	public ResponseData transferList(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String param = request.getParameter("param");
		TransferListRequestParam reqData = JSONObject.parseObject(param,TransferListRequestParam.class);
		Integer pageNo = reqData.getPageNo();
		Integer type = reqData.getType();
		TransferListResponseResult result = new TransferListResponseResult();
		List<Transfer> transferList = result.getTransferList();
		NfsTransferRecord nfsTransferRecord = new NfsTransferRecord();
		Page<NfsTransferRecord> page = null;
		
		if(type == -1){//查全部
			page = nfsTransferRecordService.findAllTransferPageList(me,pageNo,20);	
		}else{
			if(type==0){//转入
				nfsTransferRecord.setFriend(me);
			}else{//转出
				nfsTransferRecord.setMember(me);
			}
			page = nfsTransferRecordService.findPageList(nfsTransferRecord,pageNo,20);	
		}
		List<NfsTransferRecord> transferRecordList = page.getList();
		if(transferRecordList != null && transferRecordList.size() > 0){
			for (NfsTransferRecord t : transferRecordList) {
				Transfer transfer = new TransferListResponseResult().new Transfer();
				transfer.setAmount(t.getAmount().intValue() + "");
				Member other = null;
				if(me.getId().equals(t.getMember().getId())){//转出
					other = t.getFriend();
					other = memberService.get(other);
					transfer.setFriendHendImage(other.getHeadImage());
					transfer.setFriendName("转给"+other.getName());
					transfer.setTransferType(2);
				}else{
					other = t.getMember();
					other = memberService.get(other);
					transfer.setFriendHendImage(other.getHeadImage());
					transfer.setFriendName("收到"+other.getName()+"的转账");
					transfer.setTransferType(1);
				}
				transfer.setTime(DateUtils.formatDate(t.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				transfer.setTransferId(t.getId().toString());
				transferList.add(transfer);
			}
		}
		result.setTransferList(transferList);
		return ResponseData.success("转账页查询成功",result);
	}
}