package com.oliveiralucaspro.springmvcrest.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oliveiralucaspro.springmvcrest.api.v1.mapper.VendorMapper;
import com.oliveiralucaspro.springmvcrest.api.v1.model.VendorDTO;
import com.oliveiralucaspro.springmvcrest.controllers.v1.VendorController;
import com.oliveiralucaspro.springmvcrest.domain.Vendor;
import com.oliveiralucaspro.springmvcrest.repositories.VendorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public static final String ROOT_URL = VendorController.BASE_URL + "/%s";

    @Override
    public List<VendorDTO> getAllVendors() {
	return vendorRepository.findAll().stream().map(this::getVendorDTOWithURL).collect(Collectors.toList());
    }

    private VendorDTO getVendorDTOWithURL(Vendor vendor) {
	VendorDTO dto = vendorMapper.vendorToVendorDTO(vendor);
	dto.setVendorUrl(String.format(ROOT_URL, vendor.getId()));
	return dto;
    }

    @Override
    public VendorDTO getVendorById(Long id) {
	return vendorRepository.findById(id).map(this::getVendorDTOWithURL).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
	return getVendorDTOWithURL(vendorRepository.save(vendorMapper.vendorDTOToVendor(vendorDTO)));
    }

    @Override
    public VendorDTO saveVendorById(Long id, VendorDTO vendorDTO) {
	return vendorRepository.findById(id).map(vendor -> {
	    Vendor updatedVendor = vendorMapper.vendorDTOToVendor(vendorDTO);
	    updatedVendor.setId(vendor.getId());
	    return getVendorDTOWithURL(vendorRepository.save(updatedVendor));
	}).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
	return vendorRepository.findById(id).map(vendor -> {
	    if (!Objects.isNull(vendorDTO.getName())) {
		vendor.setName(vendorDTO.getName());
	    }
	    return getVendorDTOWithURL(vendorRepository.save(vendor));
	}).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendor(Long id) {
	if (!Objects.isNull(id)) {
	    vendorRepository.deleteById(id);
	} else {
	    throw new ResourceNotFoundException();
	}

    }

}
