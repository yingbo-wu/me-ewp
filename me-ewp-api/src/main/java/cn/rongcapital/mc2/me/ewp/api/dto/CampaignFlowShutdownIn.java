package cn.rongcapital.mc2.me.ewp.api.dto;

import cn.rongcapital.mc2.me.commons.api.ApiIn;

public class CampaignFlowShutdownIn implements ApiIn {

	private String id;

	private int shutdownOption;

	public CampaignFlowShutdownIn() {}

	public CampaignFlowShutdownIn(String id, int shutdownOption) {
		this.id = id;
		this.shutdownOption = shutdownOption;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getShutdownOption() {
		return shutdownOption;
	}

	public void setShutdownOption(int shutdownOption) {
		this.shutdownOption = shutdownOption;
	}

}
