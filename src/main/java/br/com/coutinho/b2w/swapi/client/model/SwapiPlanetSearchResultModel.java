package br.com.coutinho.b2w.swapi.client.model;

import java.io.Serializable;
import java.util.List;

/**
 * Star Wars API - Planet search result model
 * 
 * @author Rafael Coutinho
 *
 */
public class SwapiPlanetSearchResultModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2942345593651928983L;
	
	private Long count;
	private String next;
	private String previous;
	private List<SwapiPlanetModel> results;

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

	public List<SwapiPlanetModel> getResults() {
		return results;
	}

	public void setResults(List<SwapiPlanetModel> results) {
		this.results = results;
	}
	
}
