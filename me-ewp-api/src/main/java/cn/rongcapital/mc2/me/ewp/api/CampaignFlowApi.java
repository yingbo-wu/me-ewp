package cn.rongcapital.mc2.me.ewp.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;

import org.apache.ignite.services.Service;

import cn.rongcapital.mc2.me.commons.api.ApiResult;
import cn.rongcapital.mc2.me.ewp.api.dto.CampaignFlowIdIn;

@ValidateOnExecution
public interface CampaignFlowApi extends Service {

	ApiResult<String> flowId(@NotNull(message = "4000") @Valid CampaignFlowIdIn in);

}
