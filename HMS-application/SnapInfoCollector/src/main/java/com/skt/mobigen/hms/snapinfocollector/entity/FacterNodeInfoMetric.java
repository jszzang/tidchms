package com.skt.mobigen.hms.snapinfocollector.entity;

public enum FacterNodeInfoMetric {
//	SERIALNUMBER("/dmi/product/serial_number"),
	SERIALNUMBER("/facter/dmi/product/serial_number"),
//	OS_NAME("/os/name"),
	OS_NAME("/facter/os/name"),
//	CPU_TYPE("/processors/models/0"),
	CPU_TYPE("/facter/processors/models/0"),
//	MANUFACTURE("/dmi/manufacturer"),
	MANUFACTURE("/facter/dmi/manufacturer"),
	model("/facter/dmi/product/name"),
//	MEMORY("/memory/system/total_bytes"),
	MEMORY("/facter/memory/system/total_bytes"),
//	CPU_CNT("/processors/count"),
	CPU_CNT("/facter/processors/count"),
//	iP("/networking/ip"),
	iP("/facter/networking/ip"),
	DISK("disks_total_size"),
//	HOST_NAME("/networking/hostname"),
	HOST_NAME("/facter/networking/hostname"),
//	OS_VERSION_MAJOR("/os/release/major"),
	OS_VERSION_MAJOR("/facter/os/release/major"),
//	OS_VERSION_MINOR("/os/release/minor"),
	OS_VERSION_MINOR("/facter/os/release/minor"),
//	NODE_TYPE("/is_virtual"),
	NODE_TYPE("/facter/is_virtual"),
//	OS_ARCHITECTURE("/os/architecture");
	OS_ARCHITECTURE("/facter/os/architecture");
	
	
	
	public String namespace="";
	
	FacterNodeInfoMetric(String str) {
		this.namespace = str;
	}
}
