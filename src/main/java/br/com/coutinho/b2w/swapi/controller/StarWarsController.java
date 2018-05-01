package br.com.coutinho.b2w.swapi.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.coutinho.b2w.swapi.exception.ValidationException;
import br.com.coutinho.b2w.swapi.model.PlanetModel;
import br.com.coutinho.b2w.swapi.model.PlanetSearchResultModel;
import br.com.coutinho.b2w.swapi.service.PlanetService;

/**
 * Spring web service controller for B2W Star Wars rest API
 * 
 * @author Rafael Coutinho
 *
 */
@RestController
@RequestMapping("/b2w-swapi")
public class StarWarsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StarWarsController.class);
	
	@Autowired
	private PlanetService planetService;

	@RequestMapping(value = "/planets", method = RequestMethod.GET)
	public ResponseEntity<PlanetSearchResultModel> listPlanets(@PageableDefault(value = 10) Pageable pageRequest, HttpServletRequest req) {
		//Process and return
		try {
			return ResponseEntity.ok(planetService.findAll(pageRequest, req.getRequestURL().toString()));
		} catch (ValidationException e) {
			LOGGER.error("Find all planets validation error", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@RequestMapping(value = "/planets/{planetQuery}", method = RequestMethod.GET)
	public ResponseEntity<PlanetModel> getPlanetByQuery(@PathVariable("planetQuery") String planetQuery) {
		//Process
		PlanetModel planet = planetService.findPlanetByQuery(planetQuery);
		
		//Return
		if (planet != null) {
			return ResponseEntity.ok(planet);
		}
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/planets", method = RequestMethod.POST)
	public ResponseEntity<PlanetModel> addPlanet(@RequestBody PlanetModel newPlanet, HttpServletRequest req) {
		//Process and return
		try {
			newPlanet = planetService.save(newPlanet);
			URI planetUrl = URI.create(req.getRequestURL().toString() + newPlanet.getId() + "/").resolve(req.getContextPath());
			return ResponseEntity.created(planetUrl).build();
		} catch (ValidationException e) {
			LOGGER.error("Validation error while trying to add planet.", e);
			return ResponseEntity.badRequest().build();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Data integrity error while trying to add planet.", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@RequestMapping(value = "/planets/{planetId}", method = RequestMethod.DELETE)
	public ResponseEntity removePlanet(@PathVariable("planetId") Long planetId) {
		//Validate
		if (planetId == null) {
			return ResponseEntity.badRequest().build();
		}

		//Process and return
		try {
			planetService.delete(planetId);
			return ResponseEntity.noContent().build();
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Planet id not found.");
			return ResponseEntity.badRequest().build();
		}
	}

}
