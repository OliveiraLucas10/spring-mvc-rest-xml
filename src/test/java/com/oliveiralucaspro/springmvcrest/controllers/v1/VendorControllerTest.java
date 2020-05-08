package com.oliveiralucaspro.springmvcrest.controllers.v1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.oliveiralucaspro.springmvcrest.api.v1.model.VendorDTO;
import com.oliveiralucaspro.springmvcrest.controllers.RestResponseEntityExceptionHandler;
import com.oliveiralucaspro.springmvcrest.services.ResourceNotFoundException;
import com.oliveiralucaspro.springmvcrest.services.VendorService;
import com.oliveiralucaspro.springmvcrest.services.VendorServiceImpl;

class VendorControllerTest extends AbstractRestControllerTest {

    private static final String VENDOR_URL = VendorServiceImpl.ROOT_URL + "/1";

    private static final String NAME = "Name Test";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	mockMvc = MockMvcBuilders.standaloneSetup(controller)
		.setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    void testGetAllVendors() throws Exception {
	List<VendorDTO> vendorDTOs = Arrays.asList(new VendorDTO(), new VendorDTO());

	when(vendorService.getAllVendors()).thenReturn(vendorDTOs);

	mockMvc.perform(get(VendorController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    void testgetVendorById() throws Exception {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	when(vendorService.getVendorById(anyLong())).thenReturn(vendorDTO);

	mockMvc.perform(get(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo(NAME)));
    }

    @Test
    void testgetVendorByIdNotFound() throws Exception {
	when(vendorService.getVendorById(anyLong())).thenThrow(ResourceNotFoundException.class);

	mockMvc.perform(get(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewVendor() throws Exception {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	VendorDTO returnedVendorDTO = new VendorDTO();
	returnedVendorDTO.setName(vendorDTO.getName());
	returnedVendorDTO.setVendorUrl(VENDOR_URL);

	when(vendorService.createNewVendor(any())).thenReturn(returnedVendorDTO);

	mockMvc.perform(post(VendorController.BASE_URL).contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(vendorDTO))).andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", equalTo(NAME))).andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));
    }

    @Test
    void testSaveVendorById() throws Exception {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	VendorDTO returnedVendorDTO = new VendorDTO();
	returnedVendorDTO.setName(vendorDTO.getName());
	returnedVendorDTO.setVendorUrl(VENDOR_URL);

	when(vendorService.saveVendorById(anyLong(), any(VendorDTO.class))).thenReturn(returnedVendorDTO);

	mockMvc.perform(put(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(vendorDTO))).andExpect(status().isOk())
		.andExpect(jsonPath("$.name", equalTo(NAME))).andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));

    }

    @Test
    void testSaveVendorByIdNotFound() throws Exception {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	when(vendorService.saveVendorById(anyLong(), any(VendorDTO.class))).thenThrow(ResourceNotFoundException.class);

	mockMvc.perform(put(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(vendorDTO))).andExpect(status().isNotFound());
    }

    @Test
    void testPatchVendor() throws Exception {
	VendorDTO input = new VendorDTO();
	input.setName(NAME);

	VendorDTO returned = new VendorDTO();
	returned.setName(input.getName());
	returned.setVendorUrl(VENDOR_URL);

	when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returned);

	mockMvc.perform(patch(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(input))).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo(NAME)))
		.andExpect(jsonPath("$.vendor_url", equalTo(VENDOR_URL)));
    }

    @Test
    void testPatchVendorNotFound() throws Exception {
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenThrow(ResourceNotFoundException.class);

	mockMvc.perform(patch(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(vendorDTO))).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteVendor() throws Exception {
	mockMvc.perform(delete(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	verify(vendorService).deleteVendor(anyLong());
    }

}
