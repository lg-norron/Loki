package com.sltunion.cloudy.web.action.manage;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sltunion.cloudy.common.utils.DateUtils;
import com.sltunion.cloudy.persistent.model.TChannel;
import com.sltunion.cloudy.persistent.model.TUser;
import com.sltunion.cloudy.service.ChannelService;
import com.sltunion.cloudy.service.HomepagelockdailyService;
import com.sltunion.cloudy.web.action.PagerAction;

public class HomepagelockdailyAction extends PagerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7901732852114667376L;

	@Resource
	private HomepagelockdailyService homepagelockdailyService;
	@Resource
	private ChannelService channelService;
/*	@Resource
	private UserService userService;
*/
	private Long channelid;

	private Long userid;

	private Integer datedeff;

	private List<TUser> userList;

	private List<TChannel> channelList;

	public String index() {
		try {
			setPager();
			String createdate = DateUtils.getDateStr();
			pager.addParams("channelid", channelid);
			pager.addParams("userid", userid);
			pager.addParams("createdate", createdate);
			pager.addParams("group", "a.channelid");
			pager.addParams("order", "a.channelid DESC");
			homepagelockdailyService.findPagerList(pager);

			// userList = userService.selectAll();
			channelList = channelService.findAll();
		} catch (Exception e) {
			logger.error("", e);
		}
		return SUCCESS;
	}

	public String chart() {
		try {
			setPager();
			if(datedeff>2){
				dailyChart();
			}else{
				hourChart();
			}
			// userList = userService.selectAll();
			channelList = channelService.findAll();
		} catch (Exception e) {
			logger.error("", e);
		}
		return SUCCESS;
	}
	
	protected void hourChart(){
		datedeff = 24;
		String enddate = DateUtils.getDateStr();
		pager.addParams("channelid", channelid);
		pager.addParams("userid", userid);
		pager.addParams("startdate", enddate);
		pager.addParams("enddate", enddate);
		pager.addParams("group", "a.hour");
		pager.addParams("order", "a.hour ASC");
		List<Map<String, Object>> list = homepagelockdailyService.getChartResultList(pager.getParams());
		
		int[] xpnum = new int[datedeff];// 数量
		int[] win732num = new int[datedeff];
		int[] othernum = new int[datedeff];
		int[] total = new int[datedeff];// 数量
		String[] createdate = new String[datedeff];// 数量
		String tempdate = "0";
		if (list != null && !list.isEmpty()) {
			int index = 0;
			while (index<datedeff) {
				for (Map<String, Object> map : list) {
					String hour = map.get("hour")==null?"": map.get("hour").toString();
					if(tempdate.equals(hour)){
						xpnum[index] = Integer.parseInt(map.get("xpnum").toString());
						win732num[index] = Integer.parseInt(map.get("win732num").toString());
						othernum[index] = Integer.parseInt(map.get("othernum").toString());
						total[index] = xpnum[index] + win732num[index] + othernum[index];
					}
				}
				createdate[index] = tempdate;
				index++;
				tempdate = index+"";
			}
		}
		addResultMap("createdate", createdate);
		addResultMap("xpnum", xpnum);
		addResultMap("win732num", win732num);
		addResultMap("othernum", othernum);
		addResultMap("total", total);
	}
	
	protected void dailyChart(){
		String enddate = DateUtils.getDateStr();
		String startdate = DateUtils.getDateStr(enddate, 1-datedeff);
		pager.addParams("channelid", channelid);
		pager.addParams("userid", userid);
		pager.addParams("startdate", startdate);
		pager.addParams("enddate", enddate);
		pager.addParams("group", "a.createdate");
		pager.addParams("order", "a.createdate ASC");
		List<Map<String, Object>> list = homepagelockdailyService.getChartResultList(pager.getParams());
		
		int[] xpnum = new int[datedeff];// 数量
		int[] win732num = new int[datedeff];
		int[] othernum = new int[datedeff];
		int[] total = new int[datedeff];// 数量
		String[] createdate = new String[datedeff];// 数量
		String tempdate = startdate;
		if (list != null && !list.isEmpty()) {
			int index = 0;
			while (index<datedeff) {
				for (Map<String, Object> map : list) {
					String date = map.get("createdate").toString();
					if(tempdate.equals(date)){
						xpnum[index] = Integer.parseInt(map.get("xpnum").toString());
						win732num[index] = Integer.parseInt(map.get("win732num").toString());
						othernum[index] = Integer.parseInt(map.get("othernum").toString());
						total[index] = xpnum[index] + win732num[index] + othernum[index];
					}
				}
				createdate[index] = tempdate;
				tempdate = DateUtils.getDateStr(enddate, 2+index-datedeff);
				index++;
			}
		}
		addResultMap("createdate", createdate);
		addResultMap("xpnum", xpnum);
		addResultMap("win732num", win732num);
		addResultMap("othernum", othernum);
		addResultMap("total", total);
	}

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Integer getDatedeff() {
		return datedeff;
	}

	public void setDatedeff(Integer datedeff) {
		this.datedeff = datedeff;
	}

	public List<TUser> getUserList() {
		return userList;
	}

	public void setUserList(List<TUser> userList) {
		this.userList = userList;
	}

	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public List<TChannel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<TChannel> channelList) {
		this.channelList = channelList;
	}
}