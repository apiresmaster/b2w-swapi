package br.com.coutinho.b2w.swapi.service;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import br.com.coutinho.b2w.swapi.client.service.SwapiService;

/**
 * Star Wars API information cache service
 * 
 * @author Rafael Coutinho
 *
 */
@Component
public class SwapiCacheService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SwapiCacheService.class);
	private static final String CACHE_KEY = "planetHashMap";
	
	private LoadingCache<String, HashMap<String, Integer>> planetAppearancesInFilmsCache;
	
	@Autowired
	private SwapiService swapiService;
	
	/**
	 * Init method to build planet cache on app startup
	 */
	@PostConstruct
	public void init() {
		LOGGER.debug("SwapiCacheService - Init - {}", "BEGIN");
		planetAppearancesInFilmsCache = CacheBuilder.newBuilder()
	       .maximumSize(100)
	       .expireAfterWrite(1, TimeUnit.HOURS)
	       .build(new CacheLoader<String, HashMap<String, Integer>>() {
					@Override
					public HashMap<String, Integer> load(String key) throws Exception {
						LOGGER.debug("SwapiCacheService - Loading planet hash map by key: {}", key);
						return swapiService.getPlanetFilmQuantityHash();
					}
	           }
	       );
		try {
			planetAppearancesInFilmsCache.get(CACHE_KEY);
		} catch (ExecutionException e) {
			LOGGER.error("Error while trying to get film appearances by planet. Cache not loaded. {}", e.getMessage());
		}
		LOGGER.debug("SwapiCacheService - Init - {}", "END");
	}
	
	/**
	 * Get film appearances from cache by planet name
	 * 
	 * @param planetName Planet name
	 * @return Integer
	 */
	public Integer getFilmAppearancesByPlanetName(String planetName) {
		try {
			Integer filmAppearances = planetAppearancesInFilmsCache.get(CACHE_KEY).get(planetName);
			if (filmAppearances != null) {
				return filmAppearances;
			}
		} catch (ExecutionException e) {
			LOGGER.error("Error while trying to get film appearances by planet. Returning zero.  {}", e.getMessage());
		}
		return 0;
	}

}
