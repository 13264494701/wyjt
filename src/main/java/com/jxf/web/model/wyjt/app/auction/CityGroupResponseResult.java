package com.jxf.web.model.wyjt.app.auction;

import java.util.ArrayList;
import java.util.List;

import com.jxf.web.model.wyjt.app.auction.CityListResponseResult.City;

public class CityGroupResponseResult {

	/** 城市标签*/
	private String tap;
	
	/** 城市列表 */
	private List<City> list = new ArrayList<City>();

	public String getTap() {
		return tap;
	}

	public void setTap(String tap) {
		this.tap = tap;
	}

	public List<City> getList() {
		return list;
	}

	public void setList(List<City> list) {
		this.list = list;
	}
	
	
	
}
