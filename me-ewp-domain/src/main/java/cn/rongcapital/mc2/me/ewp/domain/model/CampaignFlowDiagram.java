package cn.rongcapital.mc2.me.ewp.domain.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import cn.rongcapital.mc2.me.commons.util.GsonUtils;
import cn.rongcapital.mc2.me.ewp.domain.FieldName;

public class CampaignFlowDiagram {

	@Expose
	@Field(FieldName.FIELD_NODES)
	public String nodes;

	public CampaignFlowDiagram() {}

	/**
	 * 解析活动节点
	 * @return
	 */
	public List<CampaignNode> parseNodes(int tenantId, String campaignId, String flowId) {
		List<CampaignNode> nodes = GsonUtils.createExposeLowerCaseWithUnderscores().fromJson(this.nodes, new TypeToken<List<CampaignNode>>() {}.getType());
		nodes.stream().forEach(node -> {
			node.appendTenandId(tenantId).appendCampaignId(campaignId).appendFlowId(flowId);
		});
		return nodes;
	}

}
