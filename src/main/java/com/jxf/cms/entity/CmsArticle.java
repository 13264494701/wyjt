package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

import com.jxf.svc.config.TemplateConfig;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.SystemUtils;

/**
 * 文章Entity
 * @author JINXINFU
 * @version 2016-11-25
 */
public class CmsArticle extends CrudEntity<CmsArticle> {
	
	private static final long serialVersionUID = 1L;
	/** 点击数缓存名称 */
	public static final String HITS_CACHE_NAME = "articleHits";

	/**
	 * 静态生成方式
	 */
	public enum GenerateMethod {

		/** 无 */
		none,

		/** 即时 */
		eager,

		/** 延时 */
		lazy
	}
	
	/**
	 * 模板类型
	 */
	public enum ModuleType {

		/** 无图模板 */
		nonePic,

		/** 左图右字*/
		lPicRWords,
		
		/** 三图*/
		threePic,
		
		/** 上字下图 */
		tWordsBPic,
		
		/** 上图下字 */
		tPicBWords,
		
		/** 新闻组 */
		newsGroup
	}
	
	private String title;		// 标题
	private CmsChannel channel;		// 频道
	private Category category;// 分类
	private String images;		// 文章图片
	private String keywords;		// 关键字
	private String description;		// 描述、摘要
	private String defaultCover;		// 默认封面
	private String author;		// 作者
	private String copyfrom;		// 文章来源
	private Integer weight;		// 权重，越大越靠前
	private Date weightDate;		// 权重期限
	private Integer displayHits;		// 展示点击数
	private Integer realHits;		// 真实点击数
	private Integer likes;     // 点赞人数
	private Integer dislikes;  // 不喜欢人次
	private Boolean isTop;		// 是否置顶
	private Boolean isStatic;   // 是否静态
	private Boolean isPub;		// 是否发布
	private Date pubTime;		// 发布时间
	private Boolean allowComment;		// 是否允许评论
	private ModuleType moduleType;		// 模板类型
    private String dataOriginal;  //数据源
	/** 文章正文*/
	private CmsArticleContent articleContent;	
	
	private Date beginDate;	// 开始时间
	private Date endDate;	// 结束时间
    
	private Integer count;
	
	/** 静态生成方式 */
	private GenerateMethod generateMethod;
	
	
	public CmsArticle() {
		super();
	}

	public CmsArticle(Long id){
		super(id);
	}

	@Length(min=1, max=255, message="标题长度必须介于 1 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public CmsChannel getChannel() {
		return channel;
	}

	public void setChannel(CmsChannel channel) {
		this.channel = channel;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Length(min=0, max=2048, message="文章图片长度必须介于 0 和 2048 之间")
	public String getImages() {
		
		return images==null?"":images;
	}

	public void setImages(String images) {
		this.images = images;
	}
	
	@Length(min=0, max=255, message="关键字长度必须介于 0 和 255 之间")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Length(min=0, max=255, message="描述、摘要长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDefaultCover() {
		return defaultCover;
	}

	public void setDefaultCover(String defaultCover) {
		this.defaultCover = defaultCover;
	}
	
	@Length(min=0, max=64, message="作者长度必须介于 0 和 64 之间")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Length(min=0, max=64, message="文章来源长度必须介于 0 和 64 之间")
	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getDisplayHits() {
		return displayHits;
	}

	public void setDisplayHits(Integer displayHits) {
		this.displayHits = displayHits;
	}
	
	public Integer getRealHits() {
		return realHits;
	}

	public void setRealHits(Integer realHits) {
		this.realHits = realHits;
	}
	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	
	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}
	
	@NotNull(message="是否置顶不能为空")
	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	
	public Boolean getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(Boolean isStatic) {
		this.isStatic = isStatic;
	}
	
	public Boolean getIsPub() {
		return isPub;
	}

	public void setIsPub(Boolean isPub) {
		this.isPub = isPub;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	
	@NotNull(message="是否允许评论不能为空")
	public Boolean getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Boolean allowComment) {
		this.allowComment = allowComment;
	}
	
	public ModuleType getModuleType() {
		return moduleType;
	}

	public void setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
	}

	public String getDataOriginal() {
		return dataOriginal;
	}

	public void setDataOriginal(String dataOriginal) {
		this.dataOriginal = dataOriginal;
	}
	
	public CmsArticleContent getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(CmsArticleContent articleContent) {
		this.articleContent = articleContent;
	}
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public GenerateMethod getGenerateMethod() {
		return generateMethod;
	}

	public void setGenerateMethod(GenerateMethod generateMethod) {
		this.generateMethod = generateMethod;
	}	
   	

	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */

	public String getPcPath() {
		TemplateConfig pcArticleContent = SystemUtils.getTemplateConfig("pcArticleContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("article", this);
		return pcArticleContent.getRealStaticPath(model);
	}
	/**
	 * 获取路径
	 * 
	 * @return 路径
	 */

	public String getMbPath() {

		TemplateConfig mbArticleContent = SystemUtils.getTemplateConfig("mbArticleContent");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("article", this);
		return mbArticleContent.getRealStaticPath(model);
	}

	/**
	 * 获取PC URL
	 * 
	 * @return URL
	 */
	public String getPcUrl() {
		return getPcPath();
	}
	/**
	 * 获取移动 URL
	 * 
	 * @return URL
	 */
	public String getMbUrl() {
		return getMbPath();
	}
	/**
	 * 获取文本内容
	 * 
	 * @return 文本内容
	 */
	@JSONField(serialize=false)
	public String getContent() {
		if (StringUtils.isEmpty(getArticleContent().getContent())) {
			return StringUtils.EMPTY;
		}
		return getArticleContent().getContent();
	}

}