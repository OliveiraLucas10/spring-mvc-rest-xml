package com.oliveiralucaspro.springmvcrest.api.v1.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.oliveiralucaspro.springmvcrest.api.v1.model.VendorDTO;
import com.oliveiralucaspro.springmvcrest.domain.Vendor;

class VendorMapperTest {

    private static final String NAME = "Name Test";
    private static final long ID = 1L;
    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    void testVendorToVendorDTO() {
	// given
	Vendor vendor = new Vendor();
	vendor.setId(ID);
	vendor.setName(NAME);

	// when
	VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

	// then
	assertEquals(vendor.getName(), vendorDTO.getName());
    }

    @Test
    void testVendorDTOToVendor() {
	// given
	VendorDTO vendorDTO = new VendorDTO();
	vendorDTO.setName(NAME);

	// when
	Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

	// then
	assertEquals(vendorDTO.getName(), vendor.getName());
    }

}
