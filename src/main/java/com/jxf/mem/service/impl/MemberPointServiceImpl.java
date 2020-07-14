package com.jxf.mem.service.impl;

import com.alibaba.fastjson.JSON;
import com.jxf.mem.dao.MemberPointDao;
import com.jxf.mem.entity.*;
import com.jxf.mem.service.MemberPointDetailService;

import com.jxf.mem.service.MemberPointService;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.model.Notice;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员积分ServiceImpl
 *
 * @author JINXINFU
 * @version 2016-04-25
 */
@Service("memberPointService")
@Transactional(readOnly = true)
public class MemberPointServiceImpl extends CrudServiceImpl<MemberPointDao, MemberPoint> implements MemberPointService {


    @Autowired
    private MemberPointDetailService memberPointDetailService;
    @Autowired
    private MemberService memberService;

    public MemberPoint get(Long id) {
        return super.get(id);
    }

    public List<MemberPoint> findList(MemberPoint memberPoint) {
        return super.findList(memberPoint);
    }

    public Page<MemberPoint> findPage(Page<MemberPoint> page, MemberPoint memberPoint) {
        return super.findPage(page, memberPoint);
    }

    @Transactional(readOnly = false)
    public void save(MemberPoint memberPoint) {
        super.save(memberPoint);
    }

    @Transactional(readOnly = false)
    public void delete(MemberPoint memberPoint) {
        super.delete(memberPoint);
    }

    /**
     * 通过会员编号获取积分
     *
     * @param memNo
     * @return
     */
    @Override
    public MemberPoint getByMember(Member member) {

        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        if (findList(memberPoint).size() == 0) {
            initMemPoint(member);
        }
        return findList(memberPoint).get(0);
    }

    /**
     * 初始化会员积分
     *
     * @param memNo
     */
    @Override
    @Transactional(readOnly = false)
    public Boolean initMemPoint(Member member) {
        MemberPoint memPoint = new MemberPoint();
        memPoint.setMember(member);
        memPoint.setBalancePoints(0L);
        memPoint.setReducePoints(0L);
        memPoint.setTotalPoints(0L);
        save(memPoint);
        return true;
    }

    /**
     * 调整修改会员积分
     */
    @Override
    @Transactional(readOnly = false)
    public void updateMemberPoint(Member member, MemberPointRule.Type type, BigDecimal amount) {

        Long points = amount.longValue();
        MemberPoint memPoint = getByMember(member);
        MemberPointDetail memPointDetail = new MemberPointDetail();

        Long balPoint = memPoint.getBalancePoints();
        Long totalPoints = memPoint.getTotalPoints();
        Long reducePoints = memPoint.getReducePoints();

        memPointDetail.setMember(member);
        memPointDetail.setType(type);
        memPointDetail.setTrxNo("");

        switch (type) {
            case agreeLoan:
                points = points + 100;
                break;
            case repay15:
                points = points + 100;
                break;
            case repay30:
                points = points / 2 + 100;
                break;
            case repay45:
                points = points / 5 + 100;
                break;
            case overDue5:
                points = -points - 200;
                break;
            case staging:
                break;
            case stagingPayOff:
                points = points + 100;
                break;
            case overDue45:
                points = 0L;
                break;
            default:
                points = 0L;
                break;
        }

        Long balancePoints = balPoint + points;
        memPoint.setBalancePoints(balancePoints);


        String rankNo = member.getMemberRank().getRankNo();
        if ("AAAAA".equals(rankNo) || "C".equals(rankNo)) {
            //不再操作等级
        } else {
            Map<String,String> rateMap = new HashMap<String,String>();
            if (MemberPointRule.Type.overDue45.equals(type) && amount.intValue() >= 200) {
                //c
                String newRank = "C";
                if (!newRank.equals(rankNo)) {
                    rateMap.put("type", "3");
                    rateMap.put("str1", "您已进入失信地图\n且行且珍惜！");
                    rateMap.put("str2", "");
                    rateMap.put("str3", "");
                    updateRank(rateMap, member, newRank);
                }
            } else if (MemberPointRule.Type.overDue5.equals(type) || balancePoints < 0) {
                String newRank = "B";
                if (!newRank.equals(rankNo)) {
                    rateMap.put("type", "2");
                    rateMap.put("str1", "友信宝温馨提示：\n信用需要及时维护！");
                    rateMap.put("str2", "");
                    rateMap.put("str3", "");
                    updateRank(rateMap, member, newRank);
                }
            }
            if (!MemberPointRule.Type.overDue45.equals(type) && !MemberPointRule.Type.overDue5.equals(type)) {
                if (balancePoints < 0) {
                    String newRank = "B";
                    if (!newRank.equals(rankNo)) {
                        rateMap.put("type", "2");
                        rateMap.put("str1", "友信宝温馨提示：\n信用需要及时维护！");
                        rateMap.put("str2", "");
                        rateMap.put("str3", "");
                        updateRank(rateMap, member, newRank);
                    }
                }
                if (balancePoints >= 0 && balancePoints < 11000) {
                    //a
                    String newRank = "A";
                    if ("B".equals(rankNo)) {
                        rateMap.put("type", "1");
                        rateMap.put("str1", "您已击败");
                        rateMap.put("str2", "20");
                        rateMap.put("str3", "%人\n成功跻身信用达人行列！");
                        updateRank(rateMap, member, newRank);
                    } else if (!newRank.equals(rankNo)) {
                        rateMap.put("type", "2");
                        rateMap.put("str1", "降级容易升级难\n请珍惜您的信用！");
                        rateMap.put("str2", "");
                        rateMap.put("str3", "");
                        updateRank(rateMap, member, newRank);
                    }
                }
                if (balancePoints >= 11000 && balancePoints < 500000) {
                    //aa
                    String newRank = "AA";
                    if ("B".equals(rankNo) || "A".equals(rankNo)) {
                        rateMap.put("type", "1");
                        rateMap.put("str1", "信用达人\n已成功立足");
                        rateMap.put("str2", "50");
                        rateMap.put("str3", "%人之上\n可以用来装Bility啦！");
                        updateRank(rateMap, member, newRank);
                    } else if (!newRank.equals(rankNo)) {
                        rateMap.put("type", "2");
                        rateMap.put("str1", "降级容易升级难\n请珍惜您的信用！");
                        rateMap.put("str2", "");
                        rateMap.put("str3", "");
                        updateRank(rateMap, member, newRank);
                    }

                }
                if (balancePoints >= 500000 && balancePoints < 3000000) {
                    //aaa
                    String newRank = "AAA";
                    if ("B".equals(rankNo) || "A".equals(rankNo) || "AA".equals(rankNo)) {
                        rateMap.put("type", "1");
                        rateMap.put("str1", "超级信用\n已有");
                        rateMap.put("str2", "80");
                        rateMap.put("str3", "%的用户被您的信用值秒杀！");
                        updateRank(rateMap, member, newRank);
                    } else if (!newRank.equals(rankNo)) {
                        rateMap.put("type", "2");
                        rateMap.put("str1", "降级容易升级难\n请珍惜您的信用！");
                        rateMap.put("str2", "");
                        rateMap.put("str3", "");
                        updateRank(rateMap, member, newRank);
                    }

                }
                if (balancePoints >= 3000000) {
                    //aaaa
                    String newRank = "AAAA";
                    if (!newRank.equals(rankNo)) {
                        rateMap.put("type", "1");
                        rateMap.put("str1", "超级信用\n已有");
                        rateMap.put("str2", "98");
                        rateMap.put("str3", "%的用户被您的信用值秒杀！");
                        updateRank(rateMap, member, newRank);
                    }

                }
            }
            //aaaaa人工升级

        }


        if (points > 0) {
            memPointDetail.setCreditPoints(points);
            memPointDetail.setDebitPoints(0L);
            memPointDetail.setCurrBalPoints(balPoint + points);
            memPoint.setTotalPoints(totalPoints + points);
        } else {
            memPointDetail.setCreditPoints(0L);
            memPointDetail.setDebitPoints(points);
            memPointDetail.setCurrBalPoints(balPoint + points);
            memPoint.setReducePoints(reducePoints + points);
        }

        save(memPoint);
        memberPointDetailService.save(memPointDetail);
    }

//	@Override
//	@Transactional(readOnly =false)
//	public void updateMemberPoint(Member member, MemberPointRule.Type type,Binding binding) {
//
//		MemberPointRule memberPointRule = new MemberPointRule();
//		memberPointRule.setType(type);
//		memberPointRule.setDrc(MemberPointRule.Drc.reward);
//		memberPointRule.setIsOn(true);
//		List<MemberPointRule> rules = memberPointRuleService.findList(memberPointRule);
//		Long points = 0L;
//
//		for(MemberPointRule rule:rules) {
//			points += rule.calculatePoint(binding);
//		}
//
//		updateMemberPoint(member,points,type);
//	}

    private void updateRank(Map<String,String> rateMap, Member member, String newRank) {
        MemberRank memberRank = new MemberRank();
        memberRank.setRankNo(newRank);
        member.setMemberRank(memberRank);
        rateMap.put("newRate", newRank);
        Notice notice = new Notice();
        notice.setNoticeType(4);
        notice.setNoticeId("");
        notice.setNoticeMessage(JSON.toJSONString(rateMap));
        RedisUtils.leftPush("memberNotice" + member.getId(), notice);
        memberService.updateRankNo(member);
    }

}