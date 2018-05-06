package br.com.coutinho.b2w.swapi.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.coutinho.b2w.swapi.entity.Planet;
import br.com.coutinho.b2w.swapi.exception.ValidationException;
import br.com.coutinho.b2w.swapi.model.PlanetModel;
import br.com.coutinho.b2w.swapi.model.PlanetSearchResultModel;
import br.com.coutinho.b2w.swapi.repository.PlanetRepository;

/**
 * Planet service
 * 
 * @author Rafael Coutinho
 *
 */
@Component
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PlanetService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlanetService.class);
	
	@Autowired
	private PlanetRepository planetRepository;
	
	@Autowired
	private SwapiCacheService swapiCacheService;
	
	/**
	 * Find all planets in database
	 * 
	 * @param pageRequest Pageable configuration
	 * @param apiUrl String with API URL
	 * @return PlanetSearchResultModel
	 * @throws ValidationException
	 */
	public PlanetSearchResultModel findAll(Pageable pageRequest, String apiUrl) throws ValidationException {
		Page<Planet> listPlanets = planetRepository.findAll(pageRequest);
		
		//Validate
		if (listPlanets.hasContent() && pageRequest.getPageNumber() + 1 > listPlanets.getTotalPages()) {
			throw new ValidationException("Invalid page number");
		}
		
		//Build result
		PlanetSearchResultModel result = new PlanetSearchResultModel();
		result.setCount(listPlanets.getTotalElements());
		result.setResults(getPlanetEntityToModelList(listPlanets.getContent()));
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
	
	private List<PlanetModel> getPlanetEntityToModelList(List<Planet> planetList) {
		if (planetList != null) {
			List<PlanetModel> modelList = new ArrayList<PlanetModel>();
			for (Planet planet : planetList) {
				PlanetModel model = planet.toModel();
				model.setFilmAppearancesNumber(swapiCacheService.getFilmAppearancesByPlanetName(model.getName()));
				modelList.add(model);
			}
			return modelList;
		}
		return null;
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
	
	/**
	 * Find a planet by a query search
	 * 
	 * @param planetQuery String with a search query
	 * @return PlanetModel
	 */
	public PlanetModel findPlanetByQuery(String planetQuery) {
		Planet planet;
		try {
			Long planetId = Long.valueOf(planetQuery);
			planet = planetRepository.findOne(planetId);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Error while converting planet query to long. Trying to get planet by name.");
			planet = planetRepository.findByName(planetQuery);
		}
		if (planet != null) {
			PlanetModel model = planet.toModel();
			model.setFilmAppearancesNumber(swapiCacheService.getFilmAppearancesByPlanetName(model.getName()));
			return model;
		}
		return null;
	}
	
	/**
	 * Save a new planet in database
	 * 
	 * @param newPlanet New planet to be saved
	 * @return Planet
	 * @throws ValidationException
	 */
	@Transactional
	public PlanetModel save(PlanetModel newPlanet) throws ValidationException {
		//Validate
		if (newPlanet == null || (newPlanet != null && newPlanet.getId() != null)) {
			throw new ValidationException("Invalid object to save");
		}
		return planetRepository.save(newPlanet.toEntity()).toModel();
	}

	/**
	 * Delete a planet in database by ID
	 * 
	 * @param planetId ID of a planet to be deleted
	 */
	@Transactional
	public void delete(Long planetId) {
		planetRepository.delete(planetId);
	}

}
