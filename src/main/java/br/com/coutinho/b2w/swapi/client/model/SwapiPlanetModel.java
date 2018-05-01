package br.com.coutinho.b2w.swapi.client.model;

import java.io.Serializable;
import java.util.List;

/**
 * Star Wars API - Planet model
 * 
 * @author Rafael Coutinho
 *
 */
public class SwapiPlanetModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5423062559191461799L;
	
	private String name;
	private List<String> films;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFilms() {
		return films;
	}

	public void setFilms(List<String> films) {
		this.films = films;
	}

}
