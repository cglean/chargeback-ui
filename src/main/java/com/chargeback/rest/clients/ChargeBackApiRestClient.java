package com.chargeback.rest.clients;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chargeback.vo.UsageRecord;

@FeignClient("metrics-service")
public interface ChargeBackApiRestClient {

	@RequestMapping(value="/metrics/getInstanceMetrics", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UsageRecord>> fetchInstanceMetrics();
	
	@RequestMapping(value="/metrics/getOrgList", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getOrgList();
		
	@RequestMapping(value="/metrics/getFreeResource/{resourceType}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getFreeResourceValue(@PathVariable String resourceType);
	
	@RequestMapping(value="/metrics/getSpaceList{orgName}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getSpaceList(@PathVariable String orgName);
	
}
