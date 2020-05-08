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

import com.oliveiralucaspro.springmvcrest.api.v1.model.CustomerDTO;
import com.oliveiralucaspro.springmvcrest.controllers.RestResponseEntityExceptionHandler;
import com.oliveiralucaspro.springmvcrest.services.CustomerService;
import com.oliveiralucaspro.springmvcrest.services.ResourceNotFoundException;

class CustomerControllerTest extends AbstractRestControllerTest {

    private static final String CUSTOMER_URL_1 = "/api/v1/customer/1";

    private static final String LAST_NAME = "Favela";

    private static final String FIRST_NAME = "Pedro";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	mockMvc = MockMvcBuilders.standaloneSetup(customerController)
		.setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
	// given
	CustomerDTO customerDTO1 = new CustomerDTO();
	customerDTO1.setFirstName(FIRST_NAME);
	customerDTO1.setLastName(LAST_NAME);

	CustomerDTO customerDTO2 = new CustomerDTO();
	customerDTO2.setFirstName("Serio");
	customerDTO2.setLastName("Mariola");

	List<CustomerDTO> customerDTOList = Arrays.asList(customerDTO1, customerDTO2);
	when(customerService.getAllCustomers()).thenReturn(customerDTOList);

	mockMvc.perform(get(CustomerController.BASE_URL).accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    void testGetCustomerById() throws Exception {
	// given
	CustomerDTO customerDTO1 = new CustomerDTO();
	customerDTO1.setFirstName(FIRST_NAME);
	customerDTO1.setLastName(LAST_NAME);

	when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO1);

	mockMvc.perform(get(CustomerController.BASE_URL + "/2").accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
	// given
	CustomerDTO customer = new CustomerDTO();
	customer.setFirstName(FIRST_NAME);
	customer.setLastName(LAST_NAME);

	CustomerDTO returnDTO = new CustomerDTO();
	returnDTO.setFirstName(customer.getFirstName());
	returnDTO.setLastName(customer.getLastName());
	returnDTO.setCustomerUrl(CUSTOMER_URL_1);

	when(customerService.createNewCustomer(customer)).thenReturn(returnDTO);

	// when/then
	mockMvc.perform(post(CustomerController.BASE_URL).accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON).content(asJsonString(customer)))
		.andExpect(status().isCreated()).andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
		.andExpect(jsonPath("$.customer_url", equalTo(CUSTOMER_URL_1)));
    }

    @Test
    void testUpdateCustomer() throws Exception {
	// given
	CustomerDTO customer = new CustomerDTO();
	customer.setFirstName(FIRST_NAME);
	customer.setLastName(LAST_NAME);

	CustomerDTO returnDTO = new CustomerDTO();
	returnDTO.setFirstName(customer.getFirstName());
	returnDTO.setLastName(customer.getLastName());
	returnDTO.setCustomerUrl(CUSTOMER_URL_1);

	when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

	// when/then
	mockMvc.perform(put(CustomerController.BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON).content(asJsonString(customer))).andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
		.andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)))
		.andExpect(jsonPath("$.customer_url", equalTo(CUSTOMER_URL_1)));
    }

    @Test
    void testPatchCustomer() throws Exception {

	// given
	CustomerDTO customer = new CustomerDTO();
	customer.setFirstName(FIRST_NAME);

	CustomerDTO returnDTO = new CustomerDTO();
	returnDTO.setFirstName(customer.getFirstName());
	returnDTO.setLastName(LAST_NAME);
	returnDTO.setCustomerUrl(CUSTOMER_URL_1);

	when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

	mockMvc.perform(patch(CustomerController.BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON).content(asJsonString(customer))).andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)))
		.andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)))
		.andExpect(jsonPath("$.customer_url", equalTo(CUSTOMER_URL_1)));
    }

    @Test
    void testDeleteCustomer() throws Exception {

	mockMvc.perform(delete(CustomerController.BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	verify(customerService).deleteCustomerById(anyLong());
    }

    @Test
    void testNotFoundException() throws Exception {

	when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

	mockMvc.perform(get(CustomerController.BASE_URL + "/222").accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

}
