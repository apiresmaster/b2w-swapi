package br.com.coutinho.b2w.swapi.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.coutinho.b2w.swapi.exception.ValidationException;
import br.com.coutinho.b2w.swapi.model.PlanetModel;
import br.com.coutinho.b2w.swapi.model.PlanetSearchResultModel;
import br.com.coutinho.b2w.swapi.service.PlanetService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StarWarsController.class, secure = false)	
public class StarWarsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

    @MockBean(name="planetService")
    private PlanetService planetService;
    
    private static final String JSON_POST_PLANET = "{\r\n" + 
    		"	\"name\": \"Alderaan\",\r\n" + 
    		"	\"climate\": \"Rainny\",\r\n" + 
    		"	\"terrain\": \"Mountain\"\r\n" + 
    		"}";
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testListPlanetsRequestWithSuccess() throws Exception {
		when(planetService.findAll(any(Pageable.class), anyString())).thenReturn(new PlanetSearchResultModel());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/b2w-swapi/planets")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testListPlanetsRequestWithoutValidationError() throws Exception {
		when(planetService.findAll(any(Pageable.class), anyString())).thenThrow(new ValidationException(""));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/b2w-swapi/planets")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetPlanetRequestWithSuccess() throws Exception {
		when(planetService.findPlanetByQuery(anyString())).thenReturn(new PlanetModel());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/b2w-swapi/planets/1/")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetPlanetRequestNotFound() throws Exception {
		when(planetService.findPlanetByQuery(anyString())).thenReturn(null);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/b2w-swapi/planets/Teste/")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testPostPlanetRequestWithSuccess() throws Exception {
    	PlanetModel planetMockSave = new PlanetModel();
    	planetMockSave.setId(1L);
    	
		when(planetService.save(any(PlanetModel.class))).thenReturn(planetMockSave);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/b2w-swapi/planets/")
				.accept(MediaType.APPLICATION_JSON).content(JSON_POST_PLANET)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
		assertEquals("http://localhost/b2w-swapi/planets/1",
				response.getHeader(HttpHeaders.LOCATION));
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testPostPlanetRequestWithValidationError() throws Exception {
		when(planetService.save(any(PlanetModel.class))).thenThrow(new ValidationException(""));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/b2w-swapi/planets/")
				.accept(MediaType.APPLICATION_JSON).content(JSON_POST_PLANET)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testPostPlanetRequestWithDataIntegrityError() throws Exception {
		when(planetService.save(any(PlanetModel.class))).thenThrow(new DataIntegrityViolationException(""));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/b2w-swapi/planets/")
				.accept(MediaType.APPLICATION_JSON).content(JSON_POST_PLANET)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeletePlanetRequestWithSuccess() throws Exception {
    	doNothing().when(planetService).delete(anyLong());
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/b2w-swapi/planets/1")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeletePlanetRequestWithEmptyResultError() throws Exception {
    	doThrow(new EmptyResultDataAccessException(0)).when(planetService).delete(anyLong());
    	
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/b2w-swapi/planets/1")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}