package com.skt.mobigen.hms.snapinfocollector.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.skt.mobigen.hms.snapinfocollector.context.Context;
import com.skt.mobigen.hms.snapinfocollector.util.HttpRequestUtil;
import com.skt.mobigen.hms.snapinfocollector.util.JsonParserUtil;

public class SnapTaskInfoCollectorService implements Callable<String> {
	private Logger logger = LoggerFactory.getLogger(SnapTaskInfoCollectorService.class);
	private Context context;

	public SnapTaskInfoCollectorService(Context context) {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String call() {
		logger.debug("snap task info collector start");
		SqlSession session = null;
		try {
			session = context.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			
			List<String> agreement_seq_list = session.selectList("get_agreement_seq_info");
			List<Map<String, Object>> insert_task_list = new ArrayList<>();
			Object last_up_time = getInsertTime();
			
			for (String agreement_seq : agreement_seq_list) {
				List<Map<String, Object>> connect_info_list = session.selectList("get_conncet_info_of_agreement", agreement_seq);
				Map<String, Object> connect_info_map = new HashMap<>();
				for (Map<String, Object> connect_info : connect_info_list) {
					connect_info_map.put(connect_info.get("member_name").toString().trim(), connect_info);
				}
				
				for (String host_name : connect_info_map.keySet()) {
					Map<String, Object> info_map = (Map<String, Object>) connect_info_map.get(host_name);
					
					// Test
//					info_map.put("member_host", "211.214.168.102");
//					if (host_name.equals("secure02")) {
//						info_map.put("rest_api_port", "3811");
//					} else if (host_name.equals("secure03")) {
//						info_map.put("rest_api_port", "3812");
//					} else if (host_name.equals("secure04")) {
//						info_map.put("rest_api_port", "3813");
//					} else {
//						info_map.put("rest_api_port", "3814");
//					}
					
					String p_url = "http://" + String.valueOf(info_map.get("member_host")).trim().concat(":").concat(String.valueOf(info_map.get("rest_api_port")));
					String task_url = p_url.concat("/v1/tasks");
					
					String response_str = null; 
					try {
//						logger.debug("try " + task_url + " Connect");
						response_str = HttpRequestUtil.sendRequest(task_url);
						if (response_str.isEmpty() || response_str == null || response_str.startsWith("404")) throw new Exception(task_url+ ": response is ".concat(response_str));
						
					} catch (Exception e) {
						logger.error(task_url + " " + ExceptionUtils.getMessage(e));
						continue;
					}
					
					Map<String, Object> response_map = JsonParserUtil.jsontoMap(response_str);;
					
					if (response_map != null) {
						Map<String, Object> response_body = (Map<String, Object>) response_map.get("body");
						List<Map<String, Object>> task_list = (List<Map<String, Object>>) response_body.get("ScheduledTasks");
						
						if (!task_list.isEmpty()) {
							for (Map<String, Object> task_info : task_list) {
								task_info.put("creation_timestamp", covertTimeformat(task_info.get("creation_timestamp")));
								task_info.put("last_run_timestamp", covertTimeformat(task_info.get("last_run_timestamp")));
								task_info.put("last_up_time", last_up_time);
								task_info.put("agreement_seq", agreement_seq);
							}
							insert_task_list.addAll(task_list);
						}
					}
//					break;
				}
			}
			
			if (!insert_task_list.isEmpty()) {
				Map<String, Object> insert_data_map = new HashMap<>();
				insert_data_map.put("task_list_info", insert_task_list);
				session.insert("insert_task_info", insert_data_map);
			}
			
			session.delete("delete_task_synchronization", last_up_time);
			session.commit();
		
			return "completed";
		} catch (Exception e) {
			return "Task Collector ".concat(ExceptionUtils.getMessage(e));
		} finally {
			if (session != null) session.close();
		}
		
	}
	
	private Object covertTimeformat(Object timestamp) {
		if (timestamp != null) {
			SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp currentTime = new Timestamp(Long.parseLong(String.valueOf(timestamp)) * 1000);

			return sdfCurrent.format(currentTime);
		}
		return null;
	}
	
	private Object getInsertTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return sdf.format(new Date().getTime());
	} 

}
