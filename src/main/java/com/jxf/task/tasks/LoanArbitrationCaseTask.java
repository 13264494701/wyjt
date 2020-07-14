package com.jxf.task.tasks;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanArbitrationService;
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
import com.jxf.web.admin.loan.NfsLoanArbitrationController;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *仲裁证据调取
 *
 * @author liuhuaixin
 */
@DisallowConcurrentExecution
public class LoanArbitrationCaseTask implements Job {

	private static final Logger log = LoggerFactory.getLogger(NfsLoanArbitrationController.class);
	@Autowired
	private NfsLoanArbitrationService nfsLoanArbitrationService;
	
	@Autowired
	private NfsLoanRecordService nfsLoanRecordService;
	
	@Autowired
	private MemberCardService memberCardService;
	
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	
	@Autowired
	private NfsWdrlRecordService nfsWdrlRecordService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	List<NfsLoanArbitration> list = nfsLoanArbitrationService.findTransferCase();
		for(NfsLoanArbitration nfsLoanArbitration : list) {
			Document document = new Document(new RectangleReadOnly(842F, 1000F));
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				NfsLoanRecord loanRecord = nfsLoanRecordService.listCase(nfsLoanArbitration.getLoan().getId());
				Member loaner = memberService.get(loanRecord.getLoaner().getId());
				MemberInfoResponseResult loanerInfoResponseResult = memberService.getMemberInfo(loaner);
				Member loanee = memberService.get(loanRecord.getLoanee().getId());
				MemberInfoResponseResult loaneeInfoResponseResult = memberService.getMemberInfo(loanee);
				MemberCard memberLoanerCard = memberCardService.getCardByMemberId(loanRecord.getLoaner().getId());
				MemberCard memberLoaneeCard = memberCardService.getCardByMemberId(loanRecord.getLoanee().getId());
				
				List<MemberActTrx> loanerActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoaner(),loanRecord);
				List<MemberActTrx> loaneeActTrxList = memberActTrxService.getTrxByLoanArbitrationProof(loanRecord.getLoanee(),loanRecord);
				
				List<NfsRchgRecord> nfsRchgRecordList = nfsRchgRecordService.getRchgRecordByMemberId(loanRecord.getLoaner().getId(),loanRecord.getCreateTime());
				List<NfsWdrlRecord> nfsWlrdRecordList = nfsWdrlRecordService.getWdrlRecordByMemberId(loanRecord.getLoanee().getId(),loanRecord.getCreateTime());
				map.put("nfsLoanRecord", loanRecord);
				map.put("memberLoanerCard", memberLoanerCard);
				map.put("memberLoaneeCard", memberLoaneeCard);
				map.put("memberLoanerActTrxList", loanerActTrxList);
				map.put("memberLoaneeActTrxList", loaneeActTrxList);
				map.put("nfsRchgRecordList", nfsRchgRecordList);
				map.put("nfsWlrdRecordList", nfsWlrdRecordList);
				map.put("loanerInfoResponseResult", loanerInfoResponseResult);
				map.put("loaneeInfoResponseResult", loaneeInfoResponseResult);
							
				Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/admin/loan/arbitration/loanCaseData.ftl");
				StringWriter result = new StringWriter();
				try {
					template.process(map, result);
				} catch (TemplateException e) {
					e.printStackTrace();
				}
				log.info(result.toString());			
				OutputStream os = new FileOutputStream(Global.getBaseStaticPath()+"/loan/arbitration/a.pdf");
				System.out.println(Global.getBaseStaticPath()+"/loan/arbitration/a.pdf");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (DocumentException e) {
				e.printStackTrace();
			}
		
		}
		
	}	
    	
    }

