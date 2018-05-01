package br.com.coutinho.b2w.swapi.client.service;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.coutinho.b2w.swapi.client.model.SwapiPlanetModel;
import br.com.coutinho.b2w.swapi.client.model.SwapiPlanetSearchResultModel;

/**
 * Star Wars public API service
 * 
 * @author Rafael Coutinho
 *
 */
@Service
public class SwapiService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SwapiService.class);
	
	@Value("${services.url.swapi}")
	private String swapiUrl;
	
	@Value("${services.operation.swapi.planets}")
	private String planetOperation;
	
	/**
	 * Get a hash map with the number of appearances in films per planet
	 * 
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> getPlanetFilmQuantityHash() {
		HashMap<String, Integer> planetHash = new HashMap<String, Integer>();
		String apiUrl = swapiUrl + planetOperation;
		
		do {
			SwapiPlanetSearchResultModel result = getSwapiPlanetResult(apiUrl);
			if (result != null && result.getResults() != null) {
				for (SwapiPlanetModel planet : result.getResults()) {
					planetHash.put(planet.getName(), planet.getFilms().size());
				}
			}
			apiUrl = result.getNext();
		} while (StringUtils.isNotEmpty(apiUrl));
		
		return planetHash;
	}
	
	private SwapiPlanetSearchResultModel getSwapiPlanetResult(String apiUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
 
        HttpEntity<SwapiPlanetSearchResultModel> entity = new HttpEntity<SwapiPlanetSearchResultModel>(headers);
 
        RestTemplate restTemplate = new RestTemplate();
 
        ResponseEntity<SwapiPlanetSearchResultModel> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, SwapiPlanetSearchResultModel.class);
 
        HttpStatus statusCode = response.getStatusCode();
        LOGGER.debug("Response Status Code: " + statusCode);
 
        if (statusCode == HttpStatus.OK) {
        	SwapiPlanetSearchResultModel result = response.getBody();
 
            if (result != null) {
                return result;
            }
        }
        return null;
	}

}
