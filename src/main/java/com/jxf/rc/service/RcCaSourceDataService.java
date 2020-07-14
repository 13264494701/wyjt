package com.jxf.rc.service;

import com.jxf.svc.sys.crud.service.CrudService;




import com.jxf.rc.entity.RcCaSourceData;

/**
 * 信用报告原始数据表Service
 * @author lmy
 * @version 2018-12-17
 */
public interface RcCaSourceDataService extends CrudService<RcCaSourceData> {
	

	/**
	 * 创建第三方数据信息
	 * @param userId
	 * @param note
	 * @param type
	 * @return
	 * @throws WebDataException
	 */
	//0紧急联系人   1 淘宝  2运营商 3芝麻认证 4学信网 5 社保 6公积金 8网银  10 运营商报告
	RcCaSourceData  createShuJuMofangSouceData(RcCaSourceData reCaSourceData);
	


	
	

	// 查询社保/公积金结果
	String getsbInformation(String task_id);


	// 查询运营商
	String getyyInformation(String task_id);

	// 淘宝
	String gettbInformation(String task_id);

	// 网银获取数据
	String getwyInformation(String task_id);

	// 查询运营商报告
    String getyyBgInformation(String task_id);
	
	/**
	 * 京东数据 2019/04/01
	 * @param task_id
	 * @return
	 */
	String getjdInformation(String task_id);
	}