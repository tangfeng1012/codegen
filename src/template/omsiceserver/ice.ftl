package com.hhly.oms.ice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hhly.oms.entity.CoopenPicture;
import com.hhly.oms.entity.Dict;
import com.hhly.oms.entity.EurooddsEntity;
import com.hhly.oms.entity.EuropeanCup;
import com.hhly.oms.entity.FocusMatchEntity;
import com.hhly.oms.entity.InfomationEntity;
import com.hhly.oms.entity.LabConfig;
import com.hhly.oms.entity.LotteryConfig;
import com.hhly.oms.entity.PictureAdvert;
import com.hhly.oms.ice.entity.HomeLabEntity;
import com.hhly.oms.ice.entity.InformationType;
import com.hhly.oms.ice.service._AppOperationServiceDisp;
import com.hhly.oms.service.AppReviewService;
import com.hhly.oms.service.CoopenPictureService;
import com.hhly.oms.service.DictService;
import com.hhly.oms.service.EurooddsService;
import com.hhly.oms.service.EuropeanCupService;
import com.hhly.oms.service.InfoDisplaySettingService;
import com.hhly.oms.service.InfohomeManageService;
import com.hhly.oms.service.InfomationService;
import com.hhly.oms.service.LabConfigService;
import com.hhly.oms.service.LotteryConfigService;
import com.hhly.oms.service.MatchDisplaySettingService;
import com.hhly.oms.service.PictureAdvertService;
import com.hhly.oms.service.ShortcutConfigService;
import com.hhly.oms.util.Constants;
import com.hhly.oms.util.PictureAdvertComparator;

import Ice.Current;

@Service
public class AppOperationServiceImpl extends _AppOperationServiceDisp {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(AppOperationServiceImpl.class);

	@Autowired
	private CoopenPictureService coopenPictureService;

	@Autowired
	private ShortcutConfigService shortcutConfigService;

	@Autowired
	private PictureAdvertService advertService;

	@Autowired
	private AppReviewService reviewService;

	@Autowired
	private LotteryConfigService lotteryConfigService;

	@Autowired
	private InfomationService informationService;

	@Autowired
	private MatchDisplaySettingService matchDisplaySettingService;

	@Autowired
	private InfoDisplaySettingService infoDisplaySettingService;

	@Autowired
	private LabConfigService labConfigService;

	@Autowired
	private InfohomeManageService infoHomeManageService;
	@Autowired
	private EurooddsService eurooddsService;
	@Autowired
	private EuropeanCupService europeanCupService;
	
	
	@Autowired
	private DictService dictService;

	private List<String> getLabCodeList() {
		List<String> labCodeList = null;
		List<LabConfig> visibleAll = labConfigService.getVisibleAll(null, null);
		if (CollectionUtils.isNotEmpty(visibleAll)) {
			labCodeList = new ArrayList<String>(visibleAll.size());
			for (LabConfig labConfig : visibleAll) {
				labCodeList.add(labConfig.getCode());
			}
		}
		return labCodeList;
	}

	@Override
	public String getAppCoopenPicture(String platform, Current __current) {
		List<CoopenPicture> coopens = this.coopenPictureService.getCurrentCoopenPicture(platform);
		if (CollectionUtils.isNotEmpty(coopens)) {
			return toJsonString(coopens.get(0));
		}
		return null;
	}

	@Override
	public String getAppAdvertPictureList(String platform, String lang, Current __current) {
		List<PictureAdvert> advertList = this.advertService.getCurrentList(platform, lang);
		Map<Integer, List<PictureAdvert>> advertMap = new LinkedHashMap<>();
		Integer seq = null;
		for (PictureAdvert pictureAdvert : advertList) {
			seq = pictureAdvert.getSeq();
			if(advertMap.containsKey(seq)) {
				advertMap.get(seq).add(pictureAdvert);
			} else {
				List<PictureAdvert> list = new ArrayList<PictureAdvert>();
				list.add(pictureAdvert);
				advertMap.put(seq, list);
			}			
		}
		
		List<PictureAdvert> returnList = new ArrayList<PictureAdvert>(advertMap.values().size());
		for (List<PictureAdvert> adverts : advertMap.values()) {
			returnList.add(getLatestPictureAdvert(adverts));
		}
		return toJsonString(returnList);
	}
	
	private PictureAdvert getLatestPictureAdvert(List<PictureAdvert> pictureAdverts) {
		if(pictureAdverts.size() == 1) {
			return pictureAdverts.get(0);
		} else {
			Collections.sort(pictureAdverts, new PictureAdvertComparator());
			return pictureAdverts.get(0);
		}
	}

	@Override
	public String getAppShortcutList(String lang, String platform, Current __current) {
		return toJsonString(shortcutConfigService.getList(lang, platform));
	}

	@Override
	public String getAppLottoryList(Current __current) {
		return toJsonString(lotteryConfigService.getAll());
	}

	@Override
	public String getAppReview(Current __current) {
		return toJsonString(reviewService.getList());
	}

	@Override
	public String getAppFocusMatchList(Current __current) {
		List<FocusMatchEntity> focusMatches = matchDisplaySettingService.queryFocusMatchlist(null);
		for (FocusMatchEntity focusMatchEntity : focusMatches) {
			focusMatchEntity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_INSIDE);
			if ("0".equals(focusMatchEntity.getMatchtype())) {
				focusMatchEntity.setOperation("13");
			} else if ("1".equals(focusMatchEntity.getMatchtype())) {
				focusMatchEntity.setOperation("20");
			}
		}
		return toJsonString(focusMatches);
	}

	/**
	 * 获取资讯列表
	 */
	@Override
	public String getAppInformationList(String lang, String type, String platform, Current __current) {
		List<InfomationEntity> infomationes = informationService.queryinfomationlist(lang,type,platform);
		for (InfomationEntity infomationEntity : infomationes) {
			infomationEntity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_OUTSIDE);
			infomationEntity.setOperation(infomationEntity.getInfourl());
		}
		return toJsonString(infomationes);
	}

	/**
	 * 获取资讯类型列表
	 */
	@Override
	public String getAppInformationTypeList(String lang, String platform, Current __current) {
		List<Dict> dicts = dictService.getDictsByItemCode(Constants.ITEM_CODE_INFO_TYPE_PREFIX + platform + "_" + lang);
		List<InformationType> infoTypeList = new ArrayList<InformationType>(dicts.size());
		for (Dict dict : dicts) {
			infoTypeList.add(new InformationType(dict.getDictCode(), dict.getDictName()));
		}
		return toJsonString(infoTypeList);
	}

	/**
	 * 获取资讯首页轮播图列表
	 */
	@Override
	public String getAppInformationAdsList(String lang, String platform, Current __current) {
		return toJsonString(infoHomeManageService.getinfohomeimgbymsg(lang, platform));
	}

	/**
	 * 获取首页资讯列表
	 */
	@Override
	public String getAppIndexInformationList(String lang,String platform, Current __current) {
		List<InfomationEntity> infomationes = infoDisplaySettingService.queryfocusinfo(lang, platform);
		for (InfomationEntity infomationEntity : infomationes) {
			infomationEntity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_OUTSIDE);
			infomationEntity.setOperation(infomationEntity.getInfourl());
		}
		return toJsonString(infomationes);
	}

	private String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue);
	}
	private String toJsonStringNotNull(Object obj) {
		if (obj == null) {
			return null;
		}
		return JSONObject.toJSONString(obj);
	}
    
	@Override
	public String getAppHomeLabDataList(String lang, Current __current) {
		HomeLabEntity homeLabEntity = new HomeLabEntity();
		List<String> labCodeList = getLabCodeList();
		StringBuffer stringBuffer = new StringBuffer();
		for (String labCode : labCodeList) {
			if (Constants.DICT_CODE_HOME_LAB_LOTTERY.equals(labCode)) {
				List<LotteryConfig> lotteryConfigs;
				try {
					lotteryConfigs = lotteryConfigService.getAll();
					homeLabEntity.setLotteryConfigs(lotteryConfigs);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("查询彩票种类配置失败:" + e.getMessage());
				}
				stringBuffer.append(Constants.DICT_CODE_HOME_LAB_LOTTERY + ",");
			} else if (Constants.DICT_CODE_HOME_LAB_FOCUS_MATCH.equals(labCode)) {
				try {
					List<FocusMatchEntity> focusMatches = matchDisplaySettingService.queryFocusMatchlist(null);
					for (FocusMatchEntity focusMatchEntity : focusMatches) {
						focusMatchEntity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_INSIDE);
						if ("0".equals(focusMatchEntity.getMatchtype())) {
							focusMatchEntity.setOperation("13");
						} else if ("1".equals(focusMatchEntity.getMatchtype())) {
							focusMatchEntity.setOperation("20");
						}
					}
					homeLabEntity.setFocusMatches(focusMatches);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("查询首页热门赛事失败:" + e.getMessage());
				}
				stringBuffer.append(Constants.DICT_CODE_HOME_LAB_FOCUS_MATCH + ",");
			} else if (Constants.DICT_CODE_HOME_LAB_HOT_INFORMATION.equals(labCode)) {
				List<InfomationEntity> informations;
				try {
					informations = infoDisplaySettingService.queryfocusinfo(null);
					homeLabEntity.setInformations(informations);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("查询首页资讯失败:" + e.getMessage());
				}
				stringBuffer.append(Constants.DICT_CODE_HOME_LAB_HOT_INFORMATION + ",");
			}
		}
		if (stringBuffer.length() > 0) {
			homeLabEntity.setLabCodes(stringBuffer.substring(0, stringBuffer.length() - 1));
		}
		return toJsonString(homeLabEntity);
	}
    
	
	@Override
	public String getAppHomeLabList(String lang, String platform, Current __current) {
		return toJsonString(labConfigService.getVisibleAll(lang, platform));
	}
    
	//根据matchid获取资讯
	@Override
	public String getAppInformationByMatchid(String lang, String matchid,
			Current __current) {
		InfomationEntity info = informationService.getinfobymatchid(lang, matchid);
		return toJsonString(info);
	}
	
    //获取欧洲杯赔率
	@Override
	public String getAppEurooddslist(String platform, Current __current) {
		List<EurooddsEntity> list = eurooddsService.queryeurooddslist();
		return toJsonString(list);
	}
	
    //获取欧洲杯资讯
	@Override
	public String getAppEuroinfomationlist(String lang, String infotype,
			String platform, Current __current) {
		List<InfomationEntity> infomationes = informationService.geteuroinfo(lang, infotype);
		for (InfomationEntity infomationEntity : infomationes) {
			infomationEntity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_OUTSIDE);
			infomationEntity.setOperation(infomationEntity.getInfourl());
		}
		return toJsonString(infomationes);
	}

	//根据欧洲杯球队id获取队员列表
	@Override
	public String getAppEuropeanPlayerByTeamid(int teamid, Current __current) {
		// TODO Auto-generated method stub
		List<EuropeanCup> europlayer=europeanCupService.getEuropeanPlayerByTeamid(teamid);
	
		
		
		return toJsonStringNotNull(europlayer);
	}

	
	//获取欧洲杯球队列表
	@Override
	public String getAppEuropeanTeamlist(Current __current) {
		// TODO Auto-generated method stub
		List<EuropeanCup> europeanteam=europeanCupService.getEuropeanTeamlist();
		return toJsonStringNotNull(europeanteam);
	}

   //根据matchid获取球队资讯
	@Override
	public String getAPPEuroInformationByMatchid(String lang, String matchid, Current __current) {
		// TODO Auto-generated method stub
		
		List<InfomationEntity> euroinformation = informationService.geteuroinfobymatchid(lang, matchid);
		return toJsonString(euroinformation);
	}

	//根据infoid获取资讯
	@Override
	public String getAppInfomationByInfoid(String lang,String infoid,String platform, Current __current) {
		InfomationEntity entity = informationService.getInfomationByInfoid(lang,Long.parseLong(infoid),platform);
		if(entity!=null){
			entity.setJumpType(Constants.DICT_CODE_JUMP_TYPE_OUTSIDE);
			entity.setOperation(entity.getInfourl());
		}
		return toJsonString(entity);
	}

}
