package com.jxf.mem.utils;


import java.util.List;

import com.jxf.mem.dao.MemberRankDao;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberRank;
import com.jxf.svc.init.SpringContextHolder;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.gxt.MemberInfoGxtResponseResult;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;

/**
 * 会员工具类
 * @author zhuhuijie
 *
 */
public class MemUtils {
	
	
	private static  MemberRankDao memberRankDao = SpringContextHolder.getBean(MemberRankDao.class);
	
	
	/**
	 * 获取等级名称
	 * @param rankNo
	 * @return
	 */
	public static String getMemRankName(String rankNo){
		String rankName = "";
		MemberRank memberRank = memberRankDao.getMemRankByNo(rankNo);
		if(memberRank != null){
			rankName = memberRank.getRankName();
		}
		return rankName;
	}
	/**
	 * 获取所有会员等级
	 * @return
	 */
	public static List<MemberRank> getMemRanks(){
		List<MemberRank> ranks = memberRankDao.findList(new MemberRank());
		return ranks;
	}

	/**
	 * 生成新等级编号
	 * @param 
	 * @return
	 */
	public static String  genNewRankNo(){
		String new_rank_no="";
		String max_rank_no=memberRankDao.getMaxRankNo();
		if(!StringUtils.isNullOrEmpty(max_rank_no)){
			new_rank_no=StringUtils.addLeftStr(StringUtils.toLong(max_rank_no)+1+"","0",3);	
		}else{
			new_rank_no = "001";
		}
		return new_rank_no;
		
	}
    /***
     *   数据脱敏
     * @param member
     * @return
     */
	public static Member maskAll(Member member) {
        if(null==member) {
          return null;	
        }
		member.setUsername(StringUtils.replacePattern(member.getUsername(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		member.setNickname(StringUtils.replacePattern(member.getNickname(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		member.setIdNo(StringUtils.replacePattern(member.getIdNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
        String name = member.getName();
        if(name.length() >1){
        	member.setName(StringUtils.replacePattern(member.getName(), "(?<=[\u4e00-\u9fa5]{1})[\u4e00-\u9fa5]", "*"));
        }
		String email = member.getEmail();
        int index = StringUtils.indexOf(email, "@");  
        if (index > 1)  {
			email = StringUtils.rightPad(StringUtils.left(email, 2), 4, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
			member.setEmail(email);
        }
		return member;
	}
    /***
     *   数据脱敏
     * @param member
     * @return
     */
	public static Member mask(Member member) {
        if(null==member) {
          return null;	
        }
		member.setUsername(StringUtils.replacePattern(member.getUsername(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		member.setNickname(StringUtils.replacePattern(member.getNickname(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		member.setIdNo(StringUtils.replacePattern(member.getIdNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		return member;
	}
    /***
     *   数据脱敏----真实姓名
     * @param member
     * @return
     */
	public static Member maskName(Member member) {
        if(null==member) {
          return null;	
        }
        String name = member.getName();
        if(name.length() >1){
        	member.setName(StringUtils.replacePattern(member.getName(), "(?<=[\u4e00-\u9fa5]{1})[\u4e00-\u9fa5]", "*"));
        }
		return member;
	}
	/***
	 *   数据脱敏----身份证号
	 * @param member
	 * @return
	 */
	public static Member maskIdNo(Member member) {
		if(null == member) {
			return null;	
		}
		String idNo = member.getIdNo();
		String replacePatternIdNo = StringUtils.replacePattern(idNo, "(?<=[\\d]{4})\\d(?=[\\d]{4})", "*");
		member.setIdNo(replacePatternIdNo.replace("**********", "****"));
		return member;
	}
	/***
	 *   数据脱敏----邮箱
	 * @param member
	 * @return
	 */
	public static Member maskEmail(Member member) {
		if(null == member) {
			return null;	
		}
		String email = member.getEmail();
        int index = StringUtils.indexOf(email, "@");  
        if (index <= 1)  {
        	return member;
        } else {
			email = StringUtils.rightPad(StringUtils.left(email, 2), 4, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
		member.setEmail(email);
		return member;
	}
	/***
     *   数据脱敏
     * @param result
	 * @return 
     * @return
     */
	public static MemberInfoResponseResult maskResult(MemberInfoResponseResult result) {
        if(null==result) {
          return null;	
        }
        result.setPhoneNo(StringUtils.replacePattern(result.getPhoneNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
        result.setNickname(StringUtils.replacePattern(result.getNickname(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
        result.setIdNo(StringUtils.replacePattern(result.getIdNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		return result;
	}
	public static MemberInfoGxtResponseResult maskResult(MemberInfoGxtResponseResult result) {
		   if(null==result) {
		          return null;	
		        }
        result.setUsername(StringUtils.replacePattern(result.getUsername(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
        result.setIdNo(StringUtils.replacePattern(result.getIdNo(), "(?<=[\\d]{3})\\d(?=[\\d]{4})", "*"));
		return result;
	}
	
	
    public static void main(String[] args) {
        
    	Member member = new Member();
    	member.setName("拉普拉多·艾哈麦提");
    	member=maskName(member);

    	System.out.println(member.getName());

    
    }
}
