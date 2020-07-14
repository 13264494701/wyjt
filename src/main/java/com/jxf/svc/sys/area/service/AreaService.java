package com.jxf.svc.sys.area.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.svc.sys.area.dao.AreaDao;
import com.jxf.svc.sys.area.entity.Area;
import com.jxf.svc.sys.tree.service.impl.TreeServiceImpl;
import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.StringUtils;



/**
 * 区域Service
 * @author jxf
 * @version 2015-07-28
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeServiceImpl<AreaDao, Area> {

	@Autowired 
	private AreaDao areaDao;
	
	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}
	
	public List<Area> getRoot(){
		return areaDao.getRoot();
	}
	
	public List<Area> getChildren(Area parent){
		return areaDao.getChildren(parent.getId());
	}
	
	public List<Area> getParents(Long[] parentIds){
		return areaDao.getParents(parentIds);
	}
	
	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	/**
	 * 根据地区编码获取地址
	 * @param code
	 * @return
	 */
	public Area getAreaByCode(String code) {
		return areaDao.getAreaByCode(code);
	}
	
	/**
	 * 根据等级获取地址
	 * @param code
	 * @return
	 */
	public List<Area> getAreaByType(String type) {
		return areaDao.getAreaByType(type);
	}
	
	public List<Area> findAreaList(Area area){
		return areaDao.findAreaList(area);
	}
	/**
	 * 	获取身份证上地址对应的市级地区
	 * @param address 身份证上地址
	 * @return
	 */
	public Area getCityByIDCardAddress(String address) {
		Area targetCity = new Area();
		List<Area> provinceAreas = getAreaByType("1");
		List<Area> ciytAreas = getAreaByType("2");

		for (Area province : provinceAreas) {
			String provinceNamePrefix = province.getName().substring(0, 2);
			// 身份证上地址带有省份的
			if (StringUtils.contains(address, provinceNamePrefix)) {
				Area countyOfThisProvince = null;
				Area areaParam = new Area();
				areaParam.setType("3");
				areaParam.setParentIds(province.getId() + "");
				List<Area> countyList = findAreaList(areaParam);
				for (Area county : countyList) {
					if (StringUtils.contains(address, county.getName())) {
						countyOfThisProvince = county;
						break;
					}
				}
				if (countyOfThisProvince == null) {
					continue;
				}
//				Long[] parentIds = { countyOfThisProvince.getParentId() };
//				List<Area> parentList = getParents(parentIds);
//				targetCity = parentList.get(0);
				
				targetCity = get(countyOfThisProvince.getParentId());
				
				return targetCity;
			}
		}
		// 身份证上地址不带省份的
		for (Area city : ciytAreas) {
			if (StringUtils.contains(address, city.getName())) {
				return city;
			}
		}
		return targetCity;
	}
}
