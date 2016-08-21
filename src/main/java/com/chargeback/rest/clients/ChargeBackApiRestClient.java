package com.chargeback.rest.clients;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chargeback.util.RestClientConstants;
import com.chargeback.vo.UsageRecord;

/**
 * 
 * @author amit
 *
 */
@FeignClient(RestClientConstants.METRICS_SERVICE)
public interface ChargeBackApiRestClient {

	
	@RequestMapping(value=RestClientConstants.INSTANCE_METRICS, method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UsageRecord>> fetchInstanceMetrics();
	
	@RequestMapping(value=RestClientConstants.ORG_LIST, method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getOrgList();
		
	@RequestMapping(value=RestClientConstants.FREE_RESOURCE, method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getFreeResourceValue(@PathVariable String resourceType);
	
	@RequestMapping(value=RestClientConstants.SPACE_LIST, method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getSpaceList(@PathVariable String orgName);
	
}
