package com.jxf.web.model.wyjt.app.member;

import java.util.List;

/**
 * 消息中心对话界面（信用档案）
 * 
 * @author Administrator
 *
 */
public class CreditaInformationResponseResult {
	// 名字
	private String userName;
	// 信宝id

	private String memberId;

	// 聊天内容
	private List<CreditfilesChat> creditfilesChat;

	// 按钮的状态
	private List<CreditfilesButton> creditfilesButtons;
	
	private String caReportUrl;//分享链接 查看全部数据

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public List<CreditfilesChat> getCreditfilesChat() {
		return creditfilesChat;
	}
	public void setCreditfilesChat(List<CreditfilesChat> creditfilesChat) {
		this.creditfilesChat = creditfilesChat;
	}
	public List<CreditfilesButton> getCreditfilesButtons() {
		return creditfilesButtons;
	}
	public void setCreditfilesButtons(List<CreditfilesButton> creditfilesButtons) {
		this.creditfilesButtons = creditfilesButtons;
	}
	public String getCaReportUrl() {
		return caReportUrl;
	}
	public void setCaReportUrl(String caReportUrl) {
		this.caReportUrl = caReportUrl;
	}
	public class CreditfilesChat{
			
			/**
			 * 文字显示
			 */
			private String text;
			
		    /**
		     * 内容的名字
		     */
			private String username;
			/**
			 * 头像地址
			 */
			private String imgUrl;
			/**
			 * 内容时间
			 */
			private String date;
			
			/**
			 * 内容的位置 0左边聊天内容  1 中间聊天内容  2 右侧聊天内容
			 */
			private int locationStatus;
			/**
			 * 内容类型  1（普通聊天）
			 *  3  好友已同意，可以查看好友档案   4灰底白字
			 *  5不可点击拒绝同意（我想查看您的信用档案，你是否同意） 6 可点击拒绝同意（我想查看您的信用档案，你是否同意）
			 */
			
			private int textType;
			public String getText() {
				return text;
			}
			public void setText(String text) {
				this.text = text;
			}
			public String getUsername() {
				return username;
			}
			public void setUsername(String username) {
				this.username = username;
			}
			public String getImgUrl() {
				return imgUrl;
			}
			public void setImgUrl(String imgUrl) {
				this.imgUrl = imgUrl;
			}
			
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			public int getLocationStatus() {
				return locationStatus;
			}
			public void setLocationStatus(int locationStatus) {
				this.locationStatus = locationStatus;
			}
			public int getTextType() {
				return textType;
			}
			public void setTextType(int textType) {
				this.textType = textType;
			}
	}
			public class CreditfilesButton {
				/**
				 * 按钮内容
				 */
				private String text;

				/**
				 * 按钮编号 001 发送档案   002 请求档案
				 */
				private String textNum;
				
				/**
				 * 按钮状态 0请求 按钮（亮可点击） 1请求按钮（不亮置灰）
				 */
				private String buttonStatus;

				public String getText() {
					return text;
				}

				public void setText(String text) {
					this.text = text;
				}

				public String getTextNum() {
					return textNum;
				}

				public void setTextNum(String textNum) {
					this.textNum = textNum;
				}

				public String getButtonStatus() {
					return buttonStatus;
				}

				public void setButtonStatus(String buttonStatus) {
					this.buttonStatus = buttonStatus;
				}
				
			}

}
