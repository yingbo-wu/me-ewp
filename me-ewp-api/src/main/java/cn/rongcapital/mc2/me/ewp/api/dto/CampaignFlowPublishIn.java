package cn.rongcapital.mc2.me.ewp.api.dto;

import cn.rongcapital.mc2.me.commons.api.ApiIn;

public class CampaignFlowPublishIn implements ApiIn {

	private String diagramJson;

	public CampaignFlowPublishIn() {}

	public CampaignFlowPublishIn(String diagramJson) {
		this.diagramJson = diagramJson;
	}

	public String getDiagramJson() {
		return diagramJson;
	}

	public void setDiagramJson(String diagramJson) {
		this.diagramJson = diagramJson;
	}

}
