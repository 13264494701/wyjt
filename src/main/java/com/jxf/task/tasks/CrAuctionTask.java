package com.jxf.task.tasks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsCrAuctionService;
import com.jxf.loan.service.NfsCrContractService;
import com.jxf.loan.service.NfsLoanContractService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.signature.youdun.YouDunESignature;
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
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.ZIPUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;


@DisallowConcurrentExecution
public class CrAuctionTask implements Job{

	private static final Logger logger = LoggerFactory.getLogger(CrAuctionTask.class);

	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsCrAuctionService crAuctionService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private NfsRchgRecordService rchgRecordService;
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;

	@Autowired
	private NfsLoanContractService loanContractService;
	@Autowired
	private NfsCrContractService crContractService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		Date startDate = new Date();
		NfsCrAuction crAuction = new NfsCrAuction();
		crAuction.setProofStatus(NfsCrAuction.ProofStatus.pendingCreate);
		List<NfsCrAuction> crList = crAuctionService.findList(crAuction);
		Map<Long, String> fileCodeMap = new HashMap<Long, String>();
		for (NfsCrAuction nfsCrAuction : crList) {

			String fileCode = copyAndCreate(nfsCrAuction);
			fileCodeMap.put(nfsCrAuction.getId(), fileCode);
		}
		
		try {
			Thread.sleep(1000 * 180);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for (NfsCrAuction nfsCrAuction : crList) {

			downloadAndZip(nfsCrAuction,fileCodeMap.get(nfsCrAuction.getId()));
		}

		logger.debug("用时:" + DateUtils.pastMinutes(startDate) + "分钟");
		
	}

	private String copyAndCreate(NfsCrAuction crAuction) {

		NfsLoanRecord loanRecord = loanRecordService.listCase(crAuction.getLoanRecord().getId());	
		NfsCrContract crContract = crContractService.getCrContractByCrId(crAuction.getId());
		NfsLoanContract loanContract = loanContractService.getCurrentContractByLoanId(loanRecord.getId());
		
		String baseStaticPath=Global.getConfig("baseStaticPath");
        String yyyy = DateUtils.getYear(crAuction.getCreateTime());
        String mm = DateUtils.getMonth(crAuction.getCreateTime());
        		
		String unzipDirName=MessageFormat.format("{0}/cr_data/home/{1}{2}/unzip/{3}_{4}/",baseStaticPath,yyyy,mm,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
		FileUtils.copyFile(baseStaticPath+crContract.getContractUrl(), unzipDirName);
		
		String loanContractUrl = StringUtils.startsWith(loanContract.getContractUrl(), "/upload")? "/data/wyjt"+loanContract.getContractUrl():baseStaticPath+loanContract.getContractUrl();
		FileUtils.copyFile(loanContractUrl, unzipDirName);
		
		BigDecimal loanerCurBal = memberService.getCulBal(loanRecord.getLoaner());
		BigDecimal loaneeCurBal = memberService.getCulBal(loanRecord.getLoanee());
		
		MemberCard memberLoanerCard = memberCardService.getCardByMemberId(loanRecord.getLoaner().getId());
		MemberCard memberLoaneeCard = memberCardService.getCardByMemberId(loanRecord.getLoanee().getId());
		List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
		List<MemberActTrx> loaneeActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoanee(),loanRecord);
		List<NfsRchgRecord> nfsRchgRecordList = rchgRecordService.getRchgRecordByMemberId(loanRecord.getLoaner().getId(),loanRecord.getCreateTime());
		List<NfsWdrlRecord> nfsWlrdRecordList = wdrlRecordService.getWdrlRecordByMemberId(loanRecord.getLoanee().getId(),loanRecord.getCreateTime());
		
		String fileCode1 = "";
		String fileCode2 = "";
		//导出原放款人的资金流水记录
		Document documentArbitration = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
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
				e.printStackTrace();
			}
			OutputStream os = new FileOutputStream(unzipDirName + "仲裁证据.pdf");
	        PdfWriter writer = PdfWriter.getInstance(documentArbitration, os);
			 //使用字体提供器，并将其设置为unicode字体样式
	        documentArbitration.open();
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		    XMLWorkerHelper.getInstance().parseXHtml(writer, documentArbitration, 
			        new ByteArrayInputStream(result.toString().getBytes("UTF-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
		    documentArbitration.close();    
			os.flush();
			os.close();
			fileCode1 = YouDunESignature.signPdf(new File(unzipDirName + "仲裁证据.pdf"));		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//导出借款单
		Document documentLoan = new Document(new RectangleReadOnly(842F, 1000F));
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nfsLoanRecord", loanRecord);
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/ftl/loanCard.ftl");
			StringWriter result = new StringWriter();
			try {
				template.process(map, result);
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			OutputStream os = new FileOutputStream(unzipDirName + "借款单.pdf");
	        PdfWriter writer = PdfWriter.getInstance(documentLoan, os);
			 //使用字体提供器，并将其设置为unicode字体样式
	        documentLoan.open();
		    MyFontsProvider fontProvider = new MyFontsProvider();
		    fontProvider.addFontSubstitute("lowagie", "garamond");
		    fontProvider.setUseUnicode(true);
		    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		    XMLWorkerHelper.getInstance().parseXHtml(writer, documentLoan, 
			        new ByteArrayInputStream(result.toString().getBytes("UTF-8")),  
			        Charset.forName("UTF-8"),fontProvider);  
		    documentLoan.close();    
			os.flush();
			os.close();
			fileCode2 = YouDunESignature.signPdf(new File(unzipDirName + "借款单.pdf"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return fileCode1+"|"+fileCode2;
	}	
	
	private int downloadAndZip(NfsCrAuction crAuction,String fileCode) {

		NfsLoanRecord loanRecord = loanRecordService.listCase(crAuction.getLoanRecord().getId());	
		String fileCode1=StringUtils.substringBefore(fileCode, "|");
		String fileCode2=StringUtils.substringAfter(fileCode, "|");
	
		String baseStaticPath=Global.getConfig("baseStaticPath");
        String yyyy = DateUtils.getYear(crAuction.getCreateTime());
        String mm = DateUtils.getMonth(crAuction.getCreateTime());    		
		String unzipDirName=MessageFormat.format("{0}/cr_data/home/{1}{2}/unzip/{3}_{4}/",baseStaticPath,yyyy,mm,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
	
		YouDunESignature.downloadSignedPdf(fileCode1, unzipDirName, "仲裁证据.pdf");
		YouDunESignature.downloadSignedPdf(fileCode2, unzipDirName, "借款单.pdf");
			
		String zipPath=MessageFormat.format("{0}/cr_data/home/{1}{2}/zip/",baseStaticPath,yyyy,mm);
		FileUtils.createDirectory(zipPath);
		String zipFileName = MessageFormat.format("{0}{1}_{2}.zip",zipPath,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
		
		File zipFile = new File(zipFileName);
		if (zipFile.exists()) {
			FileUtils.delFile(zipFileName);
		}	
		if(!ZIPUtil.createZipFile(unzipDirName, zipFileName)){
			return -1;
		}  	
		String zipUrl = MessageFormat.format("/cr_data/home/{0}{1}/zip/{2}_{3}.zip",yyyy,mm,loanRecord.getLoaneePhoneNo(),loanRecord.getLoaneeName());
		crAuction.setZipPath(zipUrl);
		crAuction.setProofStatus(NfsCrAuction.ProofStatus.created);
		crAuctionService.save(crAuction);
		return 0;
	}	
	
}
