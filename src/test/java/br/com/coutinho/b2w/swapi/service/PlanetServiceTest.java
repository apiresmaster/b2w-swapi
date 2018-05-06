package br.com.coutinho.b2w.swapi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.coutinho.b2w.swapi.entity.Planet;
import br.com.coutinho.b2w.swapi.exception.ValidationException;
import br.com.coutinho.b2w.swapi.model.PlanetModel;
import br.com.coutinho.b2w.swapi.model.PlanetSearchResultModel;
import br.com.coutinho.b2w.swapi.repository.PlanetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlanetServiceTest {

    @MockBean(name="planetRepository")
    private PlanetRepository planetRepository;
    
    @Autowired
    private PlanetService planetService;
    
    @Test
    public void testListAllPlanetsWithSuccess() throws Exception {
    	Pageable pageable = getMockedPageable(1, 1);
    	List<Planet> content = getListPlanetMockedContent();
    	Page<Planet> listPlanets = new PageImpl<Planet>(content, pageable, Long.valueOf(content.size()));
		when(planetRepository.findAll(any(Pageable.class))).thenReturn(listPlanets);
		
		PlanetSearchResultModel result = planetService.findAll(pageable, "http://localhost/b2w-swapi/planets");

		assertNotNull(result);
		assertNotNull(result.getCount());
		assertEquals(Long.valueOf(content.size()), result.getCount());
    }
    
    @Test(expected = ValidationException.class)
    public void testListAllPlanetsWithValidationError() throws Exception {
    	Page<Planet> listPlanets = new PageImpl<Planet>(getListPlanetMockedContent());
		when(planetRepository.findAll(any(Pageable.class))).thenReturn(listPlanets);
		
		planetService.findAll(getMockedPageable(1, 1), "http://localhost/b2w-swapi/planets");
    }
    
    @Test
    public void testFindPlanetByIdWithSuccess() throws Exception {
    	PlanetModel model = new PlanetModel();
    	model.setId(1L);
    	model.setName("Alderaan");
		when(planetRepository.findOne(anyLong())).thenReturn(model.toEntity());
		
		PlanetModel result = planetService.findPlanetByQuery("1");
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getName());
		assertEquals(model.getId(), result.getId());
		assertEquals(model.getId(), result.getId());
		assertTrue(result.getFilmAppearancesNumber() > 0);
    }
    
    @Test
    public void testFindPlanetByNameWithSuccess() throws Exception {
    	PlanetModel model = new PlanetModel();
    	model.setId(2L);
    	model.setName("Teste");
		when(planetRepository.findByName(anyString())).thenReturn(model.toEntity());
		
		PlanetModel result = planetService.findPlanetByQuery("Teste");

		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getName());
		assertEquals(model.getId(), result.getId());
		assertEquals(model.getId(), result.getId());
		assertEquals(new Integer(0), result.getFilmAppearancesNumber());
    }
    
    @Test
    public void testFindPlanetByIdNotFound() throws Exception {
		when(planetRepository.findOne(anyLong())).thenReturn(null);
		
		assertNull(planetService.findPlanetByQuery("99999"));
    }
    
    @Test
    public void testSaveWithSuccess() throws Exception {
		when(planetRepository.save(any(Planet.class))).thenReturn(new PlanetModel().toEntity());
    	PlanetModel newPlanet = planetService.save(new PlanetModel());
    	
    	assertNotNull(newPlanet);
    }
    
    @Test(expected = ValidationException.class)
    public void testSaveWithErrorObjectNull() throws Exception {
		when(planetRepository.save(any(Planet.class))).thenReturn(new PlanetModel().toEntity());
    	planetService.save(null);
    }
    
    @Test(expected = ValidationException.class)
    public void testSaveWithErrorIdNotNull() throws Exception {
    	PlanetModel modelIdNotNull = new PlanetModel();
    	modelIdNotNull.setId(1L);
		when(planetRepository.save(any(Planet.class))).thenReturn(new PlanetModel().toEntity());
    	planetService.save(modelIdNotNull);
    }
    
    @Test
    public void testDelete() throws Exception {
    	doNothing().when(planetRepository).delete(anyLong());
    	planetService.delete(1L);
    	assertTrue(true);
    }

	private Pageable getMockedPageable(int pageNumber, int pageSize) {
		return new PageRequest(pageNumber, pageSize);
	}

	private List<Planet> getListPlanetMockedContent() {
		List<Planet> content = new ArrayList<Planet>();
    	content.add(new Planet());
    	content.add(new Planet());
    	content.add(new Planet());
		return content;
	}

}
