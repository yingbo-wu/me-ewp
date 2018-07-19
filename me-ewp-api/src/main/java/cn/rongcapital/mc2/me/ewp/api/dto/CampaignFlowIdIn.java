package cn.rongcapital.mc2.me.ewp.api.dto;

import cn.rongcapital.mc2.me.commons.api.ApiIn;

public class CampaignFlowIdIn implements ApiIn {

	private String campaignId;

	public CampaignFlowIdIn() {}

	public CampaignFlowIdIn(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

}
