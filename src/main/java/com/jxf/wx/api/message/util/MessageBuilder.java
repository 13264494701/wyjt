package com.jxf.wx.api.message.util;

/***
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午4:15:57 
 * @版本：V1.0
 */
public class MessageBuilder {

    private StringBuilder builder;

    public MessageBuilder() {
        builder = new StringBuilder();
    }

    public MessageBuilder(int capacity) {
        builder = new StringBuilder(capacity);
    }

    public MessageBuilder(String str) {
        builder = new StringBuilder(str);
    }

    public void append(String str) {
        builder.append(str);
    }

    public void insert(String str) {
        builder.insert(0, str);
    }

    public void surroundWith(String tag) {
        StringBuilder sb = new StringBuilder(builder.capacity() + tag.length()
                * 2 + 5);
        sb.append("<").append(tag).append(">\n").append(builder).append("</")
                .append(tag).append(">\n");
        builder = sb;
    }

    public void addTag(String tagName, String text) {
        if (text == null) {
            return;
        }
        builder.append("<").append(tagName).append(">").append(text)
                .append("</").append(tagName).append(">\n");
    }

    public void addData(String tagName, String data) {
        if (data == null) {
            return;
        }
        builder.append("<").append(tagName).append("><![CDATA[").append(data)
                .append("]]></").append(tagName).append(">\n");
    }

    @Override
    public String toString() {
        return builder.toString();
    }

}
