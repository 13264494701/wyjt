package com.jxf.svc.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.data.AccessControlLevel;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.Principal;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.FileUtils;

/**
 * CKFinder配置
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class CKFinderConfig extends Configuration {

	public CKFinderConfig(ServletConfig servletConfig) {
        super(servletConfig);  
    }
	
	@Override
    protected Configuration createConfigurationInstance() {
		Principal principal = (Principal) UserUtils.getPrincipal();
		if (principal == null){
			return new CKFinderConfig(this.servletConf);
		}
		boolean isView = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:view");
		boolean isUpload = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:upload");
		boolean isEdit = true;//UserUtils.getSubject().isPermitted("cms:ckfinder:edit");
		AccessControlLevel alc = this.getAccessConrolLevels().get(0);
		alc.setFolderView(isView);
		alc.setFolderCreate(isEdit);
		alc.setFolderRename(isEdit);
		alc.setFolderDelete(isEdit);
		alc.setFileView(isView);
		alc.setFileUpload(isUpload);
		alc.setFileRename(isEdit);
		alc.setFileDelete(isEdit);
//		for (AccessControlLevel a : this.getAccessConrolLevels()){
//			System.out.println(a.getRole()+", "+a.getResourceType()+", "+a.getFolder()
//					+", "+a.isFolderView()+", "+a.isFolderCreate()+", "+a.isFolderRename()+", "+a.isFolderDelete()
//					+", "+a.isFileView()+", "+a.isFileUpload()+", "+a.isFileRename()+", "+a.isFileDelete());
//		}
		AccessControlUtil.getInstance(this).loadACLConfig();
		try {
//			Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
//			this.baseURL = ServletContextFactory.getServletContext().getContextPath()+"/userfiles/"+principal+"/";
			this.baseURL = FileUtils.path(Servlets.getRequest().getContextPath() + Global.CKFINDER_UPLOAD_BASE_URL + principal + "/");
			this.baseDir = FileUtils.path(Global.getBaseStaticPath() + Global.CKFINDER_UPLOAD_BASE_URL + principal + "/");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new CKFinderConfig(this.servletConf);
    }

    @Override  
    public boolean checkAuthentication(final HttpServletRequest request) {
        return UserUtils.getPrincipal()!=null;
    }

}
