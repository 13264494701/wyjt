package com.jxf.mem.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.config.Global;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import com.jxf.web.model.wyjt.app.member.CreditaInformationResponseResult;
import com.jxf.web.model.wyjt.app.member.CreditaInformationResponseResult.CreditfilesButton;
import com.jxf.web.model.wyjt.app.member.CreditaInformationResponseResult.CreditfilesChat;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberFriendCa;

import com.jxf.mem.dao.MemberDao;
import com.jxf.mem.dao.MemberFriendCaDao;
import com.jxf.mem.service.MemberFriendCaService;


/**
 * 信用报告申请ServiceImpl
 * 
 * @author wo
 * @version 2018-12-17
 */
@Service("memberFriendCaService")
@Transactional(readOnly = true)
public class MemberFriendCaServiceImpl extends CrudServiceImpl<MemberFriendCaDao, MemberFriendCa>
		implements MemberFriendCaService {

	@Autowired
	private MemberFriendCaDao memberFriendCaDao;

	@Autowired
	private MemberDao memberDao;

	@Override
	public MemberFriendCa get(Long id) {
		return super.get(id);
	}

	@Override
	public List<MemberFriendCa> findList(MemberFriendCa memberFriendCa) {
		return super.findList(memberFriendCa);
	}

	@Override
	public Page<MemberFriendCa> findPage(Page<MemberFriendCa> page, MemberFriendCa memberFriendCa) {
		return super.findPage(page, memberFriendCa);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(MemberFriendCa memberFriendCa) {
		super.save(memberFriendCa);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(MemberFriendCa memberFriendCa) {
		super.delete(memberFriendCa);
	}

	@Override
	public Page<MemberFriendCa> findCredictBaoGaoRecode(MemberFriendCa memberFriendCa, Integer pageNo,
			Integer pageSize) {
		Page<MemberFriendCa> page = new Page<MemberFriendCa>(pageNo == null ? 1 : pageNo,
				pageSize == null ? 20 : pageSize);
		memberFriendCa.setPage(page);
		List<MemberFriendCa> loanList = memberFriendCaDao.findCredictBaoGaoRecode(memberFriendCa);
		page.setList(loanList);
		return page;
	}

	@Override
	public CreditaInformationResponseResult getConsentOrRefusalCredita(Member member, String valId) {
		CreditaInformationResponseResult result = new CreditaInformationResponseResult();
		MemberFriendCa entity = get(Long.parseLong(valId));
		// 按钮集合
		List<CreditfilesButton> creditfilesButtonsList = new ArrayList<CreditfilesButton>();
		// 聊天内容
		List<CreditfilesChat> creditfilesChatList = new ArrayList<CreditfilesChat>();
		/**
		 * 内容的位置 0左边聊天内容 1 中间聊天内容 2右侧聊天内容
		 */
		/**
		 * 内容类型 1白底黑字（左边的聊天记录 3 好友已同意，可以查看好友档案 4灰底白字 5不可点击拒绝同意（我想查看您的信用档案，你是否同意） 6
		 * 可点击拒绝同意（我想查看您的信用档案，你是否同意）
		 */
		//被申请的好友
		Member friend = memberDao.get(entity.getFriend());
		
		// 自己的发起内容
		String myiconAddr = member.getAddr();
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString1 = formatter1.format(new Date());

		CreditfilesChat creditfilesChat3 = new CreditaInformationResponseResult().new CreditfilesChat();
		String dateString = formatter1.format(entity.getCreateTime());
		CreditfilesChat creditfilesChat10 = new CreditaInformationResponseResult().new CreditfilesChat();
		creditfilesChat10.setDate(dateString1);
		creditfilesChat10.setImgUrl(myiconAddr);
		creditfilesChat10.setLocationStatus(2);
		creditfilesChat10.setText("我想查看您的信用档案，您是否同意");
		creditfilesChat10.setTextType(1);
		creditfilesChat10.setUsername(member.getName());
		creditfilesChatList.add(creditfilesChat10);

		creditfilesChat3.setDate(dateString1);
		creditfilesChat3.setImgUrl("");
		creditfilesChat3.setLocationStatus(1);
		creditfilesChat3.setText("您请求已发送");
		creditfilesChat3.setTextType(4);
		creditfilesChat3.setUsername("");
		creditfilesChatList.add(creditfilesChat3);
		// 中间系统消息
		CreditfilesChat systemchatnet = getSystemchatnet();
		creditfilesChatList.add(systemchatnet);
		// 没申请过
		if (entity.getStatus().ordinal() == 1) {

			CreditfilesButton button2 = getButton(1, "0");
			creditfilesButtonsList.add(button2);
			CreditfilesButton button3 = getButton(2, "1");
			creditfilesButtonsList.add(button3);

			CreditfilesChat creditfilesChat4 = new CreditaInformationResponseResult().new CreditfilesChat();
			creditfilesChat4.setDate(dateString);
			creditfilesChat4.setImgUrl("");
			creditfilesChat4.setLocationStatus(1);
			creditfilesChat4.setText("对方信用档案已发送可查看，可以查看好友档案");
			creditfilesChat4.setTextType(3);
			creditfilesChat4.setUsername("");
			creditfilesChatList.add(creditfilesChat4);
			// 拒绝
		} else if (entity.getStatus().ordinal() == 2) {
			CreditfilesButton button2 = getButton(1, "0");
			creditfilesButtonsList.add(button2);
			CreditfilesButton button3 = getButton(2, "0");
			creditfilesButtonsList.add(button3);
			CreditfilesChat creditfilesChat5 = new CreditaInformationResponseResult().new CreditfilesChat();
			creditfilesChat5.setDate(dateString);
			creditfilesChat5.setImgUrl("");
			creditfilesChat5.setLocationStatus(1);
			creditfilesChat5.setText("好友已经拒绝你的请求");
			creditfilesChat5.setTextType(4);
			creditfilesChat5.setUsername("");
			creditfilesChatList.add(creditfilesChat5);
		}
		// 判读是好友查看还是自己查看
		if (member.getId().toString().equals(entity.getFriend().getId().toString())) {
			result.setMemberId(entity.getMember().getId() + "");
			Member member2 = memberDao.get(entity.getMember().getId());
			result.setUserName(member2.getName());
		} else {
			Member member2 = memberDao.get(entity.getFriend().getId());
			result.setMemberId(entity.getFriend().getId() + "");
			result.setUserName(member2.getName());
		}
		result.setCaReportUrl(Global.getConfig("domain")+"/gxt/report?key="+friend.getSafeKeyValue());
		result.setCreditfilesChat(creditfilesChatList);
		result.setCreditfilesButtons(creditfilesButtonsList);
		return result;
	}

	@Override
	public CreditaInformationResponseResult getCreditarchives(Member member, String valId) {
		CreditaInformationResponseResult result = new CreditaInformationResponseResult();
		MemberFriendCa memberFriendCa = get(Long.parseLong(valId));
		// 按钮集合
		List<CreditfilesButton> creditfilesButtonsList = new ArrayList<CreditfilesButton>();
		// 聊天内容
		List<CreditfilesChat> creditfilesChatList = new ArrayList<CreditfilesChat>();
		/**
		 * 内容的位置 0左边聊天内容 1 中间聊天内容 2右侧聊天内容
		 */
		/**
		 * 内容类型 1白底黑字（左边的聊天记录) 3 好友已同意，可以查看好友档案 4灰底白字 5不可点击拒绝同意（我想查看您的信用档案，你是否同意） 6
		 * 可点击拒绝同意（我想查看您的信用档案，你是否同意）
		 */
		// 自己的发起内容
		// 好友的
		Member searchUser = memberDao.get(memberFriendCa.getFriend());
		//主动发送人
		Member applyer = memberDao.get(memberFriendCa.getMember());
		// 好友的头像
		String friconAddr = searchUser.getAddr();
		// 中间系统消息
		CreditfilesChat systemchatnet = getSystemchatnet();
		creditfilesChatList.add(systemchatnet);
		// 没申请过 申请状态 0：申请中，1：已同意，2：已拒绝

		if (memberFriendCa.getStatus().ordinal() == 1) {
			CreditfilesButton button2 = getButton(1, "1");
			creditfilesButtonsList.add(button2);
			CreditfilesButton button3 = getButton(2, "0");
			creditfilesButtonsList.add(button3);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(memberFriendCa.getCreateTime());
			CreditfilesChat creditfilesChat3 = new CreditaInformationResponseResult().new CreditfilesChat();
			creditfilesChat3.setDate(dateString);
			creditfilesChat3.setImgUrl("");
			creditfilesChat3.setLocationStatus(1);
			// 0：申请档案 1发送档案
			if (memberFriendCa.getDrc().ordinal() == 0) {
				creditfilesChat3.setText("你已经同意了好友的授权申请");
				creditfilesChat3.setTextType(4);
			} else {
				creditfilesChat3.setText("对方信用档案已发送可查看，可以查看好友档案");
				creditfilesChat3.setTextType(3);
			}
			creditfilesChat3.setUsername("");
			creditfilesChatList.add(creditfilesChat3);
			// 申请中
		} else if (memberFriendCa.getStatus().ordinal() == 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			CreditfilesButton creditfilesButton = new CreditaInformationResponseResult().new CreditfilesButton();
			creditfilesButton.setButtonStatus("0");
			creditfilesButton.setTextNum("001");
			creditfilesButton.setText("发送档案");
			creditfilesButtonsList.add(creditfilesButton);
			CreditfilesButton button3 = getButton(2, "0");
			creditfilesButtonsList.add(button3);
			// 聊天内容
			CreditfilesChat creditfilesChat3 = new CreditaInformationResponseResult().new CreditfilesChat();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(memberFriendCa.getCreateTime());
			creditfilesChat3.setDate(dateString);
			creditfilesChat3.setImgUrl(friconAddr);
			creditfilesChat3.setLocationStatus(0);
			creditfilesChat3.setText("我想查看您的信用档案，你是否同意");
			creditfilesChat3.setTextType(6);
			creditfilesChat3.setUsername(searchUser.getName());
			creditfilesChatList.add(creditfilesChat3);
			// 拒绝
		} else if (memberFriendCa.getStatus().ordinal() == 2) {
			CreditfilesButton button2 = getButton(1, "1");
			creditfilesButtonsList.add(button2);
			CreditfilesButton button3 = getButton(2, "0");
			creditfilesButtonsList.add(button3);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(memberFriendCa.getCreateTime());
			CreditfilesChat creditfilesChat3 = new CreditaInformationResponseResult().new CreditfilesChat();
			creditfilesChat3.setDate(dateString);
			creditfilesChat3.setImgUrl("");
			creditfilesChat3.setLocationStatus(1);
			creditfilesChat3.setText("你已拒绝好友的请求");
			creditfilesChat3.setTextType(4);
			creditfilesChat3.setUsername("");
			creditfilesChatList.add(creditfilesChat3);
		}
		// 判读是好友查看还是自己查看
		// 判读是好友查看还是自己查看
		if (member.getId().toString().equals(memberFriendCa.getFriend().getId().toString())) {
			result.setMemberId(memberFriendCa.getMember().getId() + "");
			Member member2 = memberDao.get(memberFriendCa.getMember().getId());
			result.setUserName(member2.getName());
		} else {
			Member member2 = memberDao.get(memberFriendCa.getFriend().getId());
			result.setMemberId(memberFriendCa.getFriend().getId() + "");
			result.setUserName(member2.getName());
		}
		result.setCaReportUrl(Global.getConfig("domain")+"/gxt/report?key="+applyer.getSafeKeyValue());
		result.setCreditfilesChat(creditfilesChatList);
		result.setCreditfilesButtons(creditfilesButtonsList);
		return result;
	}

	// 固定不变的系统
	private CreditfilesChat getSystemchatnet() {
		// 中间系统消息
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString1 = formatter1.format(new Date());
		CreditfilesChat creditfilesChat2 = new CreditaInformationResponseResult().new CreditfilesChat();
		creditfilesChat2.setDate(dateString1);
		creditfilesChat2.setImgUrl("");
		creditfilesChat2.setLocationStatus(1);
		creditfilesChat2.setText("你们是好友关系，可以随时互相查看信用档案");
		creditfilesChat2.setTextType(4);
		creditfilesChat2.setUsername("");
		return creditfilesChat2;

	}

	// 按钮状态 1:发送按钮 2 请求按钮
	private CreditfilesButton getButton(int type, String status) {
		CreditfilesButton creditfilesButton = new CreditaInformationResponseResult().new CreditfilesButton();
		creditfilesButton.setButtonStatus(status);
		if (type == 1) {
			creditfilesButton.setTextNum("001");
			creditfilesButton.setText("发送档案");
		} else {
			creditfilesButton.setTextNum("002");
			creditfilesButton.setText("请求档案");
		}
		return creditfilesButton;

	}
}