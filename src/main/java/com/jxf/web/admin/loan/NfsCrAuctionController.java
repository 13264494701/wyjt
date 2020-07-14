package com.jxf.web.admin.loan;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.itextpdf.text.Document;
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
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.service.impl.MemberMessageServiceImpl;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.service.impl.SendMsgServiceImpl;
import com.jxf.svc.config.Global;
import com.jxf.svc.font.MyFontsProvider;
import com.jxf.svc.model.Message;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.ZIPUtil;
import com.jxf.web.admin.sys.BaseController;

import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * 债权买卖Controller
 * @author wo
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/crAuction")
public class NfsCrAuctionController extends BaseController {

	@Autowired
	private NfsCrAuctionService crAuctionService;
	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private MemberMessageServiceImpl memberMessageServiceImpl;
	@Autowired
	private SendMsgServiceImpl sendMsgServiceImpl;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberVideoVerifyService videoVerifyService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@ModelAttribute
	public NfsCrAuction get(@RequestParam(required=false) Long id) {
		NfsCrAuction entity = null;
		if (id!=null){
			entity = crAuctionService.get(id);
		}
		if (entity == null){
			entity = new NfsCrAuction();
		}
		return entity;
	}
	
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = {"list", ""})
	public String list(NfsCrAuction nfsCrAuction, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsCrAuction> page = crAuctionService.findPage(new Page<NfsCrAuction>(request, response), nfsCrAuction); 
		model.addAttribute("page", page);
		return "admin/loan/cr/nfsCrAuctionList";
	}
	
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "pendingCheck")
	public String auditList(NfsCrAuction nfsCrAuction, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsCrAuction> page = crAuctionService.findAuditPage(new Page<NfsCrAuction>(request, response), nfsCrAuction); 
		model.addAttribute("page", page);
		return "admin/loan/cr/nfsCrPendingCheckList";
	}
	
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "checkedList")
	public String auditedList(NfsCrAuction nfsCrAuction, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<NfsCrAuction> page = crAuctionService.findAuditedPage(new Page<NfsCrAuction>(request, response), nfsCrAuction); 
		model.addAttribute("page", page);
		return "admin/loan/cr/nfsCrCheckedList";
	}
	
	/**
	 * 客服审核
	 */
	@ResponseBody
	@RequiresPermissions("cr:nfsCrAuction:edit")
	@RequestMapping(value = "applyCheck")
	public Message applyCheck(NfsCrAuction nfsCrAuction, Model model,String status) {
		NfsCrAuction auction = crAuctionService.get(nfsCrAuction);
		if(StringUtils.equals(status, "forsale")) {
			if(auction.getStatus() != NfsCrAuction.Status.audit) {
				return Message.success("借条状态已变更,请勿重复操作");
			}
			auction.setStatus(NfsCrAuction.Status.forsale);
			auction.setIsPub(true);
			auction.setAuditOpinion(nfsCrAuction.getAuditOpinion());
			auction.setBeginTime(new Date());
			auction.setRmk("等待购买人购买");
		}else if(StringUtils.equals(status, "auditFailed")) {
			if(auction.getStatus() != NfsCrAuction.Status.audit&&auction.getStatus() != NfsCrAuction.Status.forsale) {
				return Message.success("借条状态已变更,请勿重复操作");
			}
			NfsLoanRecord loanRecord = loanRecordService.get(auction.getLoanRecord());
			loanRecord.setAuctionStatus(NfsLoanRecord.AuctionStatus.initial);
			loanRecordService.save(loanRecord);
			auction.setStatus(NfsCrAuction.Status.auditFailed);
			auction.setIsPub(false);
			auction.setAuditOpinion(nfsCrAuction.getAuditOpinion());
			auction.setRmk("平台审核失败");
			try {
				//发送会员消息
				MemberMessage message = memberMessageServiceImpl.sendMessage(MemberMessage.Type.auditFailAuctionImsLoaner,auction.getId());
				
				// 短信
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("money", loanRecord.getAmount());
				sendSmsMsgService.sendCollectionSms("auditFailAuctionImsLoaner", loanRecord.getLoaner().getUsername(), map);
				
				//推送
				sendMsgServiceImpl.beforeSendAppMsg(message);
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}else if(StringUtils.equals(status, "unsale")) {
			if(auction.getStatus() != NfsCrAuction.Status.auditFailed) {
				return Message.success("借条状态已变更,请勿重复操作");
			}
			auction.setStatus(NfsCrAuction.Status.unsale);
			auction.setAuditOpinion(nfsCrAuction.getAuditOpinion());
			auction.setRmk("平台重置");
		}
		crAuctionService.save(auction);
		return Message.success("操作成功");
	}
	
	/**
	 * 添加审核页面跳转
	 */
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "toCheck")
	public String toCheck(NfsCrAuction nfsCrAuction, Model model) {
		model.addAttribute("nfsCrAuction", nfsCrAuction);
		return "admin/loan/cr/addCheckRmk";
	}
	
	/**
	 * 添加页面跳转
	 */
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "add")
	public String add(NfsCrAuction nfsCrAuction, Model model) {
		model.addAttribute("nfsCrAuction", nfsCrAuction);
		return "loan/cr/nfsCrAuctionAdd";
	}
	/**
	 * 查看页面跳转
	 */
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "query")
	public String query(NfsCrAuction nfsCrAuction, Model model) {
		model.addAttribute("nfsCrAuction", nfsCrAuction);
		return "loan/cr/nfsCrAuctionQuery";
	}
	/**
	 * 修改页面跳转
	 */
	@RequiresPermissions("cr:nfsCrAuction:view")
	@RequestMapping(value = "update")
	public String update(NfsCrAuction nfsCrAuction, Model model) {
		model.addAttribute("nfsCrAuction", nfsCrAuction);
		return "loan/cr/nfsCrAuctionUpdate";
	}
	 
	 /**
	 * 新增与修改的提交
	 */
	@RequiresPermissions("cr:nfsCrAuction:edit")
	@RequestMapping(value = "save")
	public String save(NfsCrAuction nfsCrAuction, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, nfsCrAuction)){
			return add(nfsCrAuction, model);
		}
		crAuctionService.save(nfsCrAuction);
		addMessage(redirectAttributes, "保存债权买卖成功");
		return "redirect:"+Global.getAdminPath()+"/crAuction/?repage";
	}
	/**
	 * 真删除提交
	 */
	@RequiresPermissions("cr:nfsCrAuction:edit")
	@RequestMapping(value = "delete")
	public String delete(NfsCrAuction nfsCrAuction, RedirectAttributes redirectAttributes) {
		crAuctionService.delete(nfsCrAuction);
		addMessage(redirectAttributes, "删除债权买卖成功");
		return "redirect:"+Global.getAdminPath()+"/crAuction/?repage";
	}
	
	@RequiresPermissions("cr:nfsCrAuction:edit")
	@RequestMapping(value = "download")
	public String downloadFile(Long id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

		NfsCrAuction nfsCrAuction = crAuctionService.get(id);
		NfsLoanRecord loanRecord = loanRecordService.get(nfsCrAuction.getLoanRecord());
		MemberVideoVerify verify = videoVerifyService.getMemberVideoVerifyByMemberId(loanRecord.getLoanee().getId());
		List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
		
		
		String baseStaticPath=Global.getConfig("baseStaticPath");
        String yyyy = DateUtils.getYear(nfsCrAuction.getCreateTime());
        String mm = DateUtils.getMonth(nfsCrAuction.getCreateTime());
        		
		String unzipDirName=MessageFormat.format("{0}/cr_data/{1}{2}/unzip/{3}_{4}/",baseStaticPath,yyyy,mm,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
		
		String idcardFrontPhoto = (verify.getId()>2981046?baseStaticPath:"/data/wyjt")+verify.getIdcardFrontPhoto();
		String idcardBackPhoto = (verify.getId()>2981046?baseStaticPath:"/data/wyjt")+verify.getIdcardBackPhoto();
		FileUtils.copyFile(idcardFrontPhoto, unzipDirName);
		FileUtils.copyFile(idcardBackPhoto, unzipDirName);
		
		//导出原放款人的资金流水记录
		Document document = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loaner", loanRecord.getLoaner());
			map.put("loanerActTrxList", loanerActTrxList);
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/loanerTrxList.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			OutputStream os = new FileOutputStream(unzipDirName + "出借人资金流水.pdf");
	        PdfWriter writer = PdfWriter.getInstance(document, os);
			 //使用字体提供器，并将其设置为unicode字体样式
			document.open();
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		    XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
			        new ByteArrayInputStream(result.toString().getBytes("UTF-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
			document.close();    
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
		String zipPath=MessageFormat.format("{0}/cr_data/{1}{2}/zip/",baseStaticPath,yyyy,mm);
		FileUtils.createDirectory(zipPath);
		String zipFileName = MessageFormat.format("{0}/{1}_{2}.zip",zipPath,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
		
		File zipFile = new File(zipFileName);
		if (zipFile.exists()) {
			FileUtils.delFile(zipFileName);
		}	
		if(!ZIPUtil.createZipFile(unzipDirName, zipFileName)){
			return null;
		}  	
		
		String fileName = Encodes.urlEncode(MessageFormat.format("{0}_{1}.zip",loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName()));
		response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+ fileName);
        try {

            InputStream inputStream = new FileInputStream(new File(zipFileName));
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
             // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}

}