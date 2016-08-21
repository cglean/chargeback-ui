package com.chargeback.controller;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chargeback.rest.clients.ChargeBackApiRestClient;
import com.chargeback.util.RestClientConstants;
import com.chargeback.vo.ChartVO;
import com.chargeback.vo.UsageRecord;

/**
 * This Controller gives the Chart Data after calling the metrics service to the
 * UI.
 * 
 * @author ambansal
 *
 */
@RestController
public class ChargeBackController {

	@Autowired
	ChargeBackApiRestClient metricsClient;

	// TODO
	@RequestMapping(value = "/getResourceDetailsSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	private String getSummary() {

		String jsonVal = "[  " + "{  " + "\"summary\":\"$1000\"," + "\"cpu\":\"$500\"," + "\"memory\":\"$400\","
				+ "\"disk\":\"$100\"," + "\"orgName\":\"Org-1\"" + "}," + "{ " + "\"summary\":\"$5000.00\","
				+ "\"cpu\":\"$1000\"," + "\"memory\":\"$3000\"," + "\"disk\":\"$1000\"," + "\"orgName\":\"Org-2\""
				+ "}," + "{  " + "\"summary\":\"$500.00\"," + "\"cpu\":\"$100\"," + "\"memory\":\"$350\","
				+ "\"disk\":\"$50\"," + "\"orgName\":\"Org-3\"" + "}" + "]";

		return jsonVal;

	}

	/**
	 * 
	 * @param usageType
	 * @param resourceType
	 * @param orgName
	 * @param space
	 * @return
	 */
	@RequestMapping(value = RestClientConstants.GET_RESOURCE_DETAILS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ChartVO getResourceUsage(@PathVariable String usageType, @PathVariable String resourceType,
			@PathVariable String orgName, @PathVariable String space) {

		final ResponseEntity<List<UsageRecord>> response = metricsClient.fetchInstanceMetrics();
		Function<ResponseEntity<List<UsageRecord>>, List<String>> usedResourceFunction = null;
		Function<ResponseEntity<List<UsageRecord>>, List<String>> appLabelFunction = null;

		if (resourceType.equals("MEM")) {
			usedResourceFunction = usedMemory -> response.getBody().stream()
					.filter(usageRecord -> (usageRecord.getOrgName().equals(orgName)
							&& usageRecord.getSpaceName().equals(space)))
					.map(usageRecord -> usageRecord.getMemory()).collect(Collectors.toList());

		} else if (resourceType.equals("CPU")) {
			usedResourceFunction = usedCPU -> response.getBody().stream()
					.filter(usageRecord -> (usageRecord.getOrgName().equals(orgName)
							&& usageRecord.getSpaceName().equals(space)))
					.map(usageRecord -> usageRecord.getCpu()).collect(Collectors.toList());

		} else if (resourceType.equals("DISK")) {
			usedResourceFunction = usedCPU -> response.getBody().stream()
					.filter(usageRecord -> (usageRecord.getOrgName().equals(orgName)
							&& usageRecord.getSpaceName().equals(space)))
					.map(usageRecord -> usageRecord.getDisk()).collect(Collectors.toList());

		} else {
			throw new RuntimeException("Please Select Resource Type from : CPU, DISK, MEM");
		}
		appLabelFunction = appLabel -> response.getBody().stream().filter(
				usageRecord -> (usageRecord.getOrgName().equals(orgName) && usageRecord.getSpaceName().equals(space)))
				.map(usageRecord -> usageRecord.getAppname().concat(" - ").concat(usageRecord.getInstanceIndex()))
				.collect(Collectors.toList());
		if (usageType.equals("UNUSED")) {
			if (!resourceType.equals("DISK")) {
				return getUnUsedResource(response, metricsClient.getFreeResourceValue(resourceType),
						usedResourceFunction, appLabelFunction);
			} else {
				throw new RuntimeException("Not able to get total disk usage as of now");
			}
		}
		return getParameterizedUsageDetails(response, usedResourceFunction, appLabelFunction);
	}

	@RequestMapping(value = RestClientConstants.GET_ORG_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getOrganizationNames() {

		return metricsClient.getOrgList().getBody();
	}

	@RequestMapping(value = RestClientConstants.GET_SPACE_LIST, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSpaceList(@PathVariable String orgName) {

		return metricsClient.getSpaceList(orgName).getBody();
	}

	private ChartVO getUnUsedResource(final ResponseEntity<List<UsageRecord>> response,
			final ResponseEntity<String> freeResourceResponse,
			Function<ResponseEntity<List<UsageRecord>>, List<String>> freeResourceFunction,
			Function<ResponseEntity<List<UsageRecord>>, List<String>> appLabelFunction) {
		final List<String> freeResource = freeResourceFunction.apply(response);
		final List<String> appLabel = appLabelFunction.apply(response);
		freeResource.add(freeResourceResponse.getBody());
		appLabel.add("Unutilised");
		ChartVO chartVO = new ChartVO();
		chartVO.setData(freeResource);
		chartVO.setLabel(appLabel);
		return chartVO;
	}

	private ChartVO getParameterizedUsageDetails(final ResponseEntity<List<UsageRecord>> response,
			Function<ResponseEntity<List<UsageRecord>>, List<String>> resourceUsedFunction,
			Function<ResponseEntity<List<UsageRecord>>, List<String>> appLabelFunction) {

		final List<String> resourceUsed = resourceUsedFunction.apply(response);
		final List<String> appLabel = appLabelFunction.apply(response);

		final ChartVO chartVO = new ChartVO();
		chartVO.setData(resourceUsed);
		chartVO.setLabel(appLabel);
		return chartVO;
	}
}