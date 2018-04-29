package br.com.coutinho.b2w.swapi.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.coutinho.b2w.swapi.entity.Planet;
import br.com.coutinho.b2w.swapi.exception.ValidationException;
import br.com.coutinho.b2w.swapi.model.PlanetSearchResultModel;
import br.com.coutinho.b2w.swapi.repository.PlanetRepository;

/**
 * Planet service
 * 
 * @author Rafael Coutinho
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PlanetService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlanetService.class);
	
	@Autowired
	private PlanetRepository planetRepository;
	
	public PlanetSearchResultModel findAll(Pageable pageRequest, String apiUrl) throws ValidationException {
		Page<Planet> listPlanets = planetRepository.findAll(pageRequest);
		
		//Validate
		if (pageRequest.getPageNumber() + 1 > listPlanets.getTotalPages()) {
			throw new ValidationException("Invalid page number");
		}
		
		//Build result
		PlanetSearchResultModel result = new PlanetSearchResultModel();
		result.setCount(listPlanets.getTotalElements());
		result.setResults(listPlanets.getContent());
		try {
			if (pageRequest.getPageNumber() > 0) {
				result.setPrevious(getPaginationUrl(apiUrl, pageRequest.getPageNumber()-1, pageRequest.getPageSize()));
			}
			if (pageRequest.getPageNumber() < listPlanets.getTotalPages()-1) {
				result.setNext(getPaginationUrl(apiUrl, pageRequest.getPageNumber()+1, pageRequest.getPageSize()));
			}
		} catch (URISyntaxException e) {
			throw new ValidationException("Invalid API URL");
		}
		
		return result;
	}
	
	private String getPaginationUrl(String url, Integer page, Integer size) throws URISyntaxException {
		StringBuffer urlBuffer = new StringBuffer(getUrlWithoutParameters(url));
		urlBuffer.append("?page=").append(page).append("&size=").append(size);
		
		return urlBuffer.toString();
	}
	
	private String getUrlWithoutParameters(String url) throws URISyntaxException {
	    URI uri = URI.create(url);
	    return new URI(uri.getScheme(),
	                   uri.getAuthority(),
	                   uri.getPath(),
	                   null,
	                   uri.getFragment()).toString();
	}
	
	public Planet findPlanetByQuery(String planetQuery) {
		try {
			Long planetId = Long.valueOf(planetQuery);
			return planetRepository.findOne(planetId);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Error while converting planet query to long. Trying to get planet by name.");
			return planetRepository.findByName(planetQuery);
		}
	}
	
	@Transactional
	public Planet save(Planet newPlanet) throws ValidationException {
		//Validate
		if (newPlanet == null || (newPlanet != null && newPlanet.getId() != null)) {
			throw new ValidationException("Invalid object to save");
		}
		return planetRepository.save(newPlanet);
	}

	@Transactional
	public void delete(Long planetId) {
		planetRepository.delete(planetId);
	}

}
