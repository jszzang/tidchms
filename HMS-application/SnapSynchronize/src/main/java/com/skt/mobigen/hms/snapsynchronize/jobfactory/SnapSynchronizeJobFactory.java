package com.skt.mobigen.hms.snapsynchronize.jobfactory;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skt.mobigen.hms.snapsynchronize.context.Context;
import com.skt.mobigen.hms.snapsynchronize.service.IFSnapSynchronizeService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapAgreementJoinSyncService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapAgreementListSyncService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapManagerPluginSyncService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapManagerTaskSyncService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapPluginSyncService;
import com.skt.mobigen.hms.snapsynchronize.service.SnapTaskSyncService;

public class SnapSynchronizeJobFactory {
	private Logger logger = LoggerFactory.getLogger(SnapSynchronizeJobFactory.class);
	private Context context;

	public SnapSynchronizeJobFactory(Context context) {
		this.context = context;
	}

	public IFSnapSynchronizeService jobfactory(String json) {

		Map<String, Object> receive_data_map;
		
		try {
			receive_data_map = jsontoMap(json);
			logger.debug("json to map result : {}", receive_data_map);
		} catch (Exception e) {
			return null;
		}

		if (receive_data_map != null && !receive_data_map.isEmpty()) {
			
			if (receive_data_map.get("type").toString().equalsIgnoreCase("manager_plugin")) {
				return new SnapManagerPluginSyncService(context);
				
			} else if (receive_data_map.get("type").toString().equalsIgnoreCase("manager_task")) {
				return new SnapManagerTaskSyncService(context);
				
			} else if (receive_data_map.get("type").toString().equalsIgnoreCase("agreement_list")) {
				return new SnapAgreementListSyncService(context, receive_data_map);
				
			} else if (receive_data_map.get("type").toString().equalsIgnoreCase("agreement_management")) {
				return new SnapAgreementJoinSyncService(context, receive_data_map);
				
			} else if (receive_data_map.get("type").toString().equalsIgnoreCase("plugin")) {
				return new SnapPluginSyncService(context, receive_data_map);
				
			} else if (receive_data_map.get("type").toString().equalsIgnoreCase("task")) {
				return new SnapTaskSyncService(context, receive_data_map);
			} 
			
		} else {
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> jsontoMap(String json) throws Exception {
		return new ObjectMapper().readValue(json, Map.class);
	}

}
