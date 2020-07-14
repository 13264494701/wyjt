package com.jxf.web.home;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.svc.config.Global;
import com.jxf.svc.font.MyFontsProvider;
import com.jxf.svc.model.AjaxRsp;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;


import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller("homeMemberController")
@RequestMapping(value ="/home/member")
public class MemberController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberCardService memberCardService;
	
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	@Autowired
	private NfsRchgRecordService rchgRecordService;
	
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	
	@RequestMapping(value ="recharge_list")
	public String recharge_list(NfsRchgRecord nfsRchgRecord, HttpServletRequest request, HttpServletResponse response, Model model){
		Member member = memberService.getCurrent2();
		nfsRchgRecord.setMember(member);
		Page<NfsRchgRecord> page = rchgRecordService.findPage(new Page<NfsRchgRecord>(request, response), nfsRchgRecord);
		model.addAttribute("page", page);
        HashMap<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", member.getId());
        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
        model.addAttribute("memberToken", memberToken);
        
		return "home/usercenter/recharge-list";
	}
	
	@RequestMapping(value ="loan_list")
	public String loan_list(NfsLoanRecord nfsLoanRecord, HttpServletRequest request, HttpServletResponse response, Model model){
		Member member = memberService.getCurrent2();
		nfsLoanRecord.setLoaner(member);
		nfsLoanRecord.setLoanee(member);
		Page<NfsLoanRecord> page = loanRecordService.findContractByMemberId(nfsLoanRecord, new Page<NfsLoanRecord>(request, response));
		model.addAttribute("page", page);
        HashMap<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", member.getId());
        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
        model.addAttribute("memberToken", memberToken);
        
		return "home/usercenter/loan-list";
	}
	
	@RequestMapping(value ="loan_caseList")
	public String loan_caseList(NfsLoanRecord nfsLoanRecord, HttpServletRequest request, HttpServletResponse response, Model model){
		Member member = memberService.getCurrent2();
		nfsLoanRecord.setLoaner(member);
		nfsLoanRecord.setLoanee(member);
		Page<NfsLoanRecord> page = loanRecordService.findContractByMemberId(nfsLoanRecord, new Page<NfsLoanRecord>(request, response));
		model.addAttribute("page", page);
        HashMap<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", member.getId());
        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
        model.addAttribute("memberToken", memberToken);
		return "home/usercenter/loan-caseList";
	}
	
	/***
	 * 
	 * @param nfsLoanRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="crList")
	public String cr_List(NfsCrAuction nfsCrAuction, HttpServletRequest request, HttpServletResponse response, Model model){
		Member member = memberService.getCurrent2();
		nfsCrAuction.setStatus(NfsCrAuction.Status.successed);
		nfsCrAuction.setCrBuyer(member);
		Page<NfsCrAuction> page = crAuctionService.findPage(new Page<NfsCrAuction>(request, response), nfsCrAuction);
		model.addAttribute("page", page);
        HashMap<String, Object> payLoad = new HashMap<>();
        payLoad.put("id", member.getId());
        String memberToken = JwtUtil.generToken(payLoad, Global.getTokenTimeout());
        model.addAttribute("memberToken", memberToken);
		return "home/usercenter/crList";
	}
	
	/***
	 * 
	 * @param nfsLoanRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="applyDownLoad")
	public @ResponseBody AjaxRsp download(Long crId){

		AjaxRsp rsp  = new AjaxRsp();
		NfsCrAuction crAuction = crAuctionService.get(crId);
		crAuction.setProofStatus(NfsCrAuction.ProofStatus.pendingCreate);
		crAuctionService.save(crAuction);
		rsp.setStatus(true);
		rsp.setMessage("申请下载提交成功,请稍后文件包生成后下载！");
		return rsp;
	}
	
	/**
	 * 转出pdf
	 */
	@RequestMapping(value="exportPdf")
	public void exportPdf(Long loanId, HttpServletRequest request, HttpServletResponse response, Model model) {
		Document document = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			NfsLoanRecord loanRecord = loanRecordService.listCase(loanId);
			BigDecimal loanerCurBal = memberService.getCulBal(loanRecord.getLoaner());
			BigDecimal loaneeCurBal = memberService.getCulBal(loanRecord.getLoanee());
			
			MemberCard memberLoanerCard = memberCardService.getCardByMemberId(loanRecord.getLoaner().getId());
			MemberCard memberLoaneeCard = memberCardService.getCardByMemberId(loanRecord.getLoanee().getId());
						
			List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
			List<MemberActTrx> loaneeActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoanee(),loanRecord);
			
			List<NfsRchgRecord> nfsRchgRecordList = rchgRecordService.getRchgRecordByMemberId(loanRecord.getLoaner().getId(),loanRecord.getCreateTime());
			List<NfsWdrlRecord> nfsWlrdRecordList = wdrlRecordService.getWdrlRecordByMemberId(loanRecord.getLoanee().getId(),loanRecord.getCreateTime());
			
			map.put("nfsLoanRecord", loanRecord);
			map.put("memberLoanerCard", memberLoanerCard);
			map.put("memberLoaneeCard", memberLoaneeCard);
			map.put("memberLoanerActTrxList", loanerActTrxList);
			map.put("memberLoaneeActTrxList", loaneeActTrxList);
			map.put("nfsRchgRecordList", nfsRchgRecordList);
			map.put("nfsWlrdRecordList", nfsWlrdRecordList);
			map.put("loanerCurBal", StringUtils.decimalToStr(loanerCurBal, 2));
			map.put("loaneeCurBal", StringUtils.decimalToStr(loaneeCurBal, 2));
			
						
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/loanCaseData.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				log.error(Exceptions.getStackTraceAsString(e));
			}		
			response.setContentType("application/pdf");
			OutputStream os = response.getOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, os);			

			document.open();
		    // 使用字体提供器，并将其设置为unicode字体样式
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
			        new ByteArrayInputStream(result.toString().getBytes("Utf-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
			document.close();
			response.flushBuffer();
		} catch (IOException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		catch (DocumentException e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
	
	}
	
	
}
