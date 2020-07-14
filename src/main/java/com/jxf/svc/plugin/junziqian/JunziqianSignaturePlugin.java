package com.jxf.svc.plugin.junziqian;

import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginConfigAttr;
import com.jxf.svc.plugin.SignaturePlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Component;


/**
 * 君子签电子签章插件
 *
 * @author Administrator
 */
@Component("junziqianPlugin")
public class JunziqianSignaturePlugin extends SignaturePlugin {


	@Override
	public String getName() {
		return "君子签";
	}
	
	@Override
	public String getVersion() {
		return "2.0";
	}
  @Override
  public String getInstallUrl() {
      return null;
  }

  @Override
  public String getUninstallUrl() {
      return null;
  }

  @Override
  public String getSettingUrl() {
      return null;
  }
}