package br.com.coutinho.b2w.swapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coutinho.b2w.swapi.entity.Planet;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
	
	/**
	 * Find a planet by name
	 * 
	 * @param name Planet name
	 * @return Planet list
	 */
	List<Planet> findByName(String name);

}
