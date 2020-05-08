package com.oliveiralucaspro.springmvcrest.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.VendorMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.VendorDTO;
import com.oliveiralucaspro.springmvcrest.controllers.v1.VendorController;
import com.oliveiralucaspro.springmvcrest.domain.Vendor;
import com.oliveiralucaspro.springmvcrest.repositories.VendorRepository;

class VendorServiceImplTest {

    private static final Long ID = 1L;

    private static final String NAME = "Name Test";

    private static final String URL = VendorController.BASE_URL + "/" + ID;

    @Mock
    VendorRepository vendorRepository;

    VendorServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

	service = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    void testGetAllVendors() {
	// given
	List<Vendor> vendorList = Arrays.asList(new Vendor(), new Vendor(), new Vendor());

	when(vendorRepository.findAll()).thenReturn(vendorList);
	// when
	List<VendorDTO> allVendors = service.getAllVendors();

	// then
	assertEquals(vendorList.size(), allVendors.size());
    }

    @Test
    void testGetVendorById() {
	// given
	Vendor vendor = new Vendor();
	vendor.setId(ID);
	vendor.setName(NAME);

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));
	// when
	VendorDTO vendorDTO = service.getVendorById(ID);

	// then
	assertEquals(vendor.getName(), vendorDTO.getName());
    }

    @Test()
    void testGetVendorByIdNotFound() {
	// given

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.empty());
	// when

	// then
	Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getVendorById(ID));
    }

    @Test
    void testCreateNewVendor() {
	// given
	Vendor vendor = new Vendor();
	vendor.setId(ID);
	vendor.setName(NAME);

	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	when(vendorRepository.save(any())).thenReturn(vendor);
	// when
	VendorDTO returnedDTO = service.createNewVendor(vendorDTO);

	// than
	assertEquals(vendor.getName(), returnedDTO.getName());
	assertEquals(String.format(VendorServiceImpl.ROOT_URL, vendor.getId()), returnedDTO.getVendorUrl());
	verify(vendorRepository, times(1)).save(any());

    }

    @Test
    void testSaveVendorById() {
	// given
	Vendor vendor = new Vendor();
	vendor.setId(ID);
	vendor.setName(NAME);

	Vendor savedVendor = new Vendor();
	savedVendor.setId(ID);
	savedVendor.setName(NAME + "2");

	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME + "2");

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));
	when(vendorRepository.save(any())).thenReturn(savedVendor);
	// when
	VendorDTO returnedVendorDTO = service.saveVendorById(ID, vendorDTO);

	// than
	assertEquals(savedVendor.getName(), returnedVendorDTO.getName());
	assertEquals(URL, returnedVendorDTO.getVendorUrl());
	verify(vendorRepository).findById(anyLong());
	verify(vendorRepository).save(any(Vendor.class));

    }

    @Test
    void testSaveVendorByIdNotFound() {
	// given

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.empty());
	// when

	// then
	Assertions.assertThrows(ResourceNotFoundException.class, () -> service.saveVendorById(ID, new VendorDTO()));
    }

    @Test
    void testPatchVendor() {
	// given
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	Vendor findById = new Vendor();
	findById.setId(ID);
	findById.setName("findById");

	Vendor saved = new Vendor();
	saved.setId(findById.getId());
	saved.setName(vendorDTO.getName());

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(findById));
	when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

	// when
	VendorDTO returned = service.patchVendor(ID, vendorDTO);

	// than
	assertEquals(saved.getName(), returned.getName());
	assertEquals(URL, returned.getVendorUrl());
	verify(vendorRepository).findById(anyLong());
	verify(vendorRepository).save(any(Vendor.class));

    }

    @Test
    void testPatchVendorNullName() {
	// given
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(null);

	Vendor findById = new Vendor();
	findById.setId(ID);
	findById.setName("findById");

	Vendor saved = new Vendor();
	saved.setId(findById.getId());
	saved.setName(findById.getName());

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(findById));
	when(vendorRepository.save(any(Vendor.class))).thenReturn(saved);

	// when
	VendorDTO returned = service.patchVendor(ID, vendorDTO);

	// than
	assertEquals(findById.getName(), returned.getName());
	assertEquals(URL, returned.getVendorUrl());
	verify(vendorRepository).findById(anyLong());
	verify(vendorRepository).save(any(Vendor.class));

    }

    @Test
    void testPatchVendorNotFound() {
	// given

	when(vendorRepository.findById(anyLong())).thenReturn(Optional.empty());
	// when

	// then
	Assertions.assertThrows(ResourceNotFoundException.class, () -> service.patchVendor(ID, new VendorDTO()));
    }

    @Test
    void testDeleteVendor() {
	service.deleteVendor(ID);
	verify(vendorRepository).deleteById(anyLong());
    }

    @Test
    void testDeleteVendorNotFound() {
	Assertions.assertThrows(ResourceNotFoundException.class, () -> service.deleteVendor(null));
    }

}
