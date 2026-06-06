package com.project.ecommerce.service;

import com.project.ecommerce.dto.AddressDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Address;
import com.project.ecommerce.model.User;
import com.project.ecommerce.repository.AddressRepository;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor

public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address=modelMapper.map(addressDTO,Address.class);
        List<Address> addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress= addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddress() {
        List<Address> addressList=addressRepository.findAll();
       List<AddressDTO> addressDTOS= addressList.stream().map(
                address ->  modelMapper.map(address, AddressDTO.class)

        ).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address=addressRepository.findById(addressId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Address","addressId",addressId)
                );
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressByUser(User user) {
        List<Address> addressList=user.getAddresses();
        List<AddressDTO> addressDTOList=addressList.stream().map(
                address -> modelMapper.map(address,AddressDTO.class)
        ).toList();

        return addressDTOList;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));

        address.setStreet(addressDTO.getStreet());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setPinCode(addressDTO.getPinCode());

        Address updatedAddress = addressRepository.save(address);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address=addressRepository.findById(addressId).orElseThrow(
                ()-> new ResourceNotFoundException("Address","addressId",addressId)
        );
        User user=address.getUser();
        user.getAddresses().removeIf(address1 -> address1.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(address);
        return "Address deleted successfully for " + address.getStreet() + " " + address.getBuildingName() ;
    }

}
