package cn.rongcapital.mc2.me.ewp.app.service;

import org.apache.ignite.resources.SpringResource;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.commons.api.ApiResult;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteService;
import cn.rongcapital.mc2.me.ewp.api.CampaignFlowApi;
import cn.rongcapital.mc2.me.ewp.api.dto.CampaignFlowIdIn;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowFindService;

@Service
public class CampaignFlowService extends IgniteService implements CampaignFlowApi {

	private static final long serialVersionUID = 1L;

	@SpringResource(resourceName = "campaignFlowFindService")
	private transient CampaignFlowFindService campaignFlowFindService;

	@Override
	public ApiResult<String> flowId(CampaignFlowIdIn in) {
		String campaignId = in.getCampaignId();
		String flowId = campaignFlowFindService.flowId(campaignId);
		return ApiResult.success(flowId);
	}

}
