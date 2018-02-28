package com.skt.mobigen.hms.snapinfocollector.context;

import org.apache.ibatis.session.SqlSessionFactory;

public class Context {
	private String snap_connect_ip;
	private int snap_connect_port;
	private String rest_proto;
	private SqlSessionFactory sqlSessionFactory;
	private String influx_connect_ip;
	private int influx_connect_port;
	private String influx_user;
	private String influx_password;
	private String influx_DB;
	private String process_interval_time;

	public String getProcess_interval_time() {
		return process_interval_time;
	}

	public void setProcess_interval_time(String process_interval_time) {
		this.process_interval_time = process_interval_time;
	}

	public String getInflux_user() {
		return influx_user;
	}

	public void setInflux_user(String influx_user) {
		this.influx_user = influx_user;
	}

	public String getInflux_password() {
		return influx_password;
	}

	public void setInflux_password(String influx_password) {
		this.influx_password = influx_password;
	}

	public String getInflux_DB() {
		return influx_DB;
	}

	public void setInflux_DB(String influx_DB) {
		this.influx_DB = influx_DB;
	}

	public String getInflux_connect_ip() {
		return influx_connect_ip;
	}

	public void setInflux_connect_ip(String influx_connect_ip) {
		this.influx_connect_ip = influx_connect_ip;
	}

	public int getInflux_connect_port() {
		return influx_connect_port;
	}

	public void setInflux_connect_port(String influx_connect_port) {
		this.influx_connect_port = Integer.parseInt(influx_connect_port);
	}

	public String getRest_proto() {
		return rest_proto;
	}

	public void setRest_proto(String rest_proto) {
		this.rest_proto = rest_proto;
	}

	public String getSnap_connect_ip() {
		return snap_connect_ip;
	}

	public void setSnap_connect_ip(String snap_connect_ip) {
		this.snap_connect_ip = snap_connect_ip;
	}

	public int getSnap_connect_port() {
		return snap_connect_port;
	}

	public void setSnap_connect_port(String snap_connect_port) {
		this.snap_connect_port = Integer.parseInt(snap_connect_port);
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
}
