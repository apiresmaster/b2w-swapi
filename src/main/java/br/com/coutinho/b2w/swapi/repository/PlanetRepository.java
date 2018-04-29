package br.com.coutinho.b2w.swapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coutinho.b2w.swapi.entity.Planet;

/**
 * Planet JPA repository
 * 
 * @author Rafael Coutinho
 *
 */
public interface PlanetRepository extends JpaRepository<Planet, Long> {
	
	/**
	 * Find a planet by name
	 * 
	 * @param name Planet name
	 * @return Planet
	 */
	Planet findByName(String name);

}
