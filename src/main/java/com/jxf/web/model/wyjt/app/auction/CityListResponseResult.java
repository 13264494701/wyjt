package com.jxf.web.model.wyjt.app.auction;

import java.util.ArrayList;
import java.util.List;



public class CityListResponseResult {

	/** 定位城市 */
	private String localName;
	
	/** 热门城市 */
	private List<City> popCityList = new ArrayList<City>();
	
	/** 城市列表 */
	private List<CityGroupResponseResult> cityList = new ArrayList<CityGroupResponseResult>();
	
	public class City{
		
		/** 城市编号 */
		private String cityId;
		/** 城市名称 */
		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public List<City> getPopCityList() {
		return popCityList;
	}

	public void setPopCityList(List<City> popCityList) {
		this.popCityList = popCityList;
	}

	public List<CityGroupResponseResult> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityGroupResponseResult> cityList) {
		this.cityList = cityList;
	}
	


}
