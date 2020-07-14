package com.jxf.svc.utils;

import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class HtmlUtils {
	
	private static final Logger log = LoggerFactory.getLogger(HtmlUtils.class);
	
	public  static String getDataFromSiteByUrl(String Url,String encode) 
	{

			String mUrl=Url;
			
			if(null!=mUrl)
			{
				java.io.InputStream l_urlStream=null;
				java.net.URL l_url=null;
				java.net.HttpURLConnection l_connection=null;
				int returnCode=0;
				ByteArrayOutputStream BAOS=null;
								
				try
				{					
				 	l_url=new java.net.URL(mUrl.trim());
				 	l_connection = (java.net.HttpURLConnection) l_url.openConnection();
					l_connection.setFollowRedirects(false);
					l_connection.setConnectTimeout(200000);
					l_connection.setReadTimeout(300000);
				 	
					returnCode = l_connection.getResponseCode();
					if(returnCode > 300)return "<!-- Exception -->";

					l_urlStream = l_connection.getInputStream();
					if(l_urlStream==null)
					{
						return "<!-- Exception -->";
					}										
					int k = 0;
					int len=1024;
					if(l_connection.getContentLength() > 0)
					{
						len=l_connection.getContentLength();
					}
					

					byte[] bytes = new byte[len];
					
					BAOS = new ByteArrayOutputStream(len);
					while ((k = l_urlStream.read(bytes)) > 0)
					{
					        BAOS.write(bytes, 0, k);
					}
					String result = BAOS.toString(encode);
					
					return result;
				}
				catch(Exception e)
				{
					log.error(Exceptions.getStackTraceAsString(e));	
					return null;
				}
				finally
				{
					if(BAOS!=null)
					{						
						try
						{
							BAOS.flush();
				   			BAOS.close(); 
				   		}
						catch(Exception e)
						{} 
					} 							
					if(l_urlStream!=null)
					{
						try
						{
				   			l_urlStream.close();   
				   		}
						catch(Exception e)
						{}
					} 
				
					if(l_connection != null)
					{
						try
						{
							l_connection.disconnect();
						}
						catch(Exception e)
						{}
					}
				}
				
			}
			else
				return "<!-- Exception -->";
	}
	public static String getContent(String url,String bianma)
	{
		if(bianma ==null)bianma="utf-8";
		String content = getDataFromSiteByUrl(url,bianma);
		return content;
	}
	
	public static String getContent(String url)
	{
		
		String content = getContent(url,"utf-8");
		return content;
	}
}
