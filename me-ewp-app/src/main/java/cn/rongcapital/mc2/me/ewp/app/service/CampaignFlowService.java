package cn.rongcapital.mc2.me.ewp.app.service;

import org.apache.ignite.resources.SpringResource;
import org.springframework.stereotype.Service;

import cn.rongcapital.mc2.me.commons.api.ApiResult;
import cn.rongcapital.mc2.me.commons.infrastructure.ignite.IgniteService;
import cn.rongcapital.mc2.me.ewp.api.CampaignFlowApi;
import cn.rongcapital.mc2.me.ewp.api.dto.CampaignFlowPublishIn;
import cn.rongcapital.mc2.me.ewp.api.dto.CampaignFlowShutdownIn;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowCancelService;
import cn.rongcapital.mc2.me.ewp.domain.service.CampaignFlowPublishService;

@Service
public class CampaignFlowService extends IgniteService implements CampaignFlowApi {

	private static final long serialVersionUID = 7693660257017553416L;

	@SpringResource(resourceName = "campaignFlowPublishService")
	private transient CampaignFlowPublishService campaignFlowPublishService;

	@SpringResource(resourceName = "campaignFlowCancelService")
	private transient CampaignFlowCancelService campaignFlowCancelService;

	@Override
	public ApiResult<Void> publish(CampaignFlowPublishIn in) {
		String diagramJson = in.getDiagramJson();
		campaignFlowPublishService.publish(diagramJson);
		return ApiResult.success();
	}

	@Override
	public ApiResult<Void> shutdown(CampaignFlowShutdownIn in) {
		String campaignId = in.getId();
		int shutdownOption = in.getShutdownOption();
		campaignFlowCancelService.cancel(campaignId, shutdownOption);
		return ApiResult.success();
	}

}
