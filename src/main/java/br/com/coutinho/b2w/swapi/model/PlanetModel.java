package br.com.coutinho.b2w.swapi.model;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import br.com.coutinho.b2w.swapi.entity.Planet;

/**
 * Model class for planet
 * 
 * @author Rafael Coutinho
 *
 */
public class PlanetModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1488387109218032123L;

	private Long id;
	private String name;
	private String climate;
	private String terrain;
	private Integer filmAppearancesNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClimate() {
		return climate;
	}

	public void setClimate(String climate) {
		this.climate = climate;
	}

	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}

	public Integer getFilmAppearancesNumber() {
		return filmAppearancesNumber;
	}

	public void setFilmAppearancesNumber(Integer filmAppearancesNumber) {
		this.filmAppearancesNumber = filmAppearancesNumber;
	}
	
	public Planet toEntity() {
		Planet entity = new Planet();
		BeanUtils.copyProperties(this, entity);
		return entity;
	}

}
