package br.com.coutinho.b2w.swapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import br.com.coutinho.b2w.swapi.model.PlanetModel;

/**
 * Planet JPA entity
 * 
 * @author Rafael Coutinho
 *
 */
@Entity
@Table(name = "planet")
public class Planet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "climate", nullable = false)
	private String climate;

	@Column(name = "terrain", nullable = false)
	private String terrain;

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
	
	public PlanetModel toModel() {
		PlanetModel model = new PlanetModel();
		BeanUtils.copyProperties(this, model);
		return model;
	}
	
}
