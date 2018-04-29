package br.com.coutinho.b2w.swapi.model;

import java.io.Serializable;
import java.util.List;

import br.com.coutinho.b2w.swapi.entity.Planet;

/**
 * Model class for planet search result
 * 
 * @author Rafael Coutinho
 *
 */
public class PlanetSearchResultModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3294559379684708747L;

	private Long count;
	
	private String next;
	
	private String previous;
	
	private List<Planet> results;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public List<Planet> getResults() {
		return results;
	}

	public void setResults(List<Planet> results) {
		this.results = results;
	}

}
