package br.com.coutinho.b2w.swapi.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@GetMapping(path = "/planets")
	public ResponseEntity<PlanetSearchResultModel> listPlanets(@PageableDefault(value = 10) Pageable pageRequest) {
		//Process and return
		try {
			return ResponseEntity.ok(planetService.findAll(pageRequest, ServletUriComponentsBuilder.fromCurrentRequest().toUriString()));
		} catch (ValidationException e) {
			LOGGER.error("Find all planets validation error", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(path = "/planets/{planetQuery}")
	public ResponseEntity<PlanetModel> getPlanetByQuery(@PathVariable("planetQuery") String planetQuery) {
		//Process
		PlanetModel planet = planetService.findPlanetByQuery(planetQuery);
		
		//Return
		if (planet != null) {
			return ResponseEntity.ok(planet);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping(path = "/planets")
	public ResponseEntity<PlanetModel> addPlanet(@RequestBody PlanetModel newPlanet) {
		//Process and return
		try {
			newPlanet = planetService.save(newPlanet);
			URI planetUrl = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newPlanet.getId()).toUri();
			return ResponseEntity.created(planetUrl).build();
		} catch (ValidationException e) {
			LOGGER.error("Validation error while trying to add planet.", e);
			return ResponseEntity.badRequest().build();
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Data integrity error while trying to add planet.", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping(path = "/planets/{planetId}")
	public ResponseEntity removePlanet(@PathVariable("planetId") Long planetId) {
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
