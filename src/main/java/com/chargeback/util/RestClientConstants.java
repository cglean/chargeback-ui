package com.chargeback.util;

public class RestClientConstants {

	private RestClientConstants(){
		
	}
	public static final String METRICS_SERVICE = "metrics-service";
	public static final String INSTANCE_METRICS = "/metrics/getInstanceMetrics";
	public static final String ORG_LIST = "/metrics/getOrgList";
	public static final String FREE_RESOURCE = "/metrics/getFreeResource/{resourceType}";
	public static final String SPACE_LIST = "/metrics/getSpaceList{orgName}";
	public static final String GET_RESOURCE_DETAILS = "/getResourceDetails/{usageType}/{resourceType}/{orgName:.+}/{space:.+}";
	public static final String GET_ORG_LIST = "/getOrgList";
	public static final String GET_SPACE_LIST ="/getSpaceList/{orgName:.+}";
}
