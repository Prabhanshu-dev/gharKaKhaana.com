package org.gharKaKhaana.vendor.application;

import org.gharKaKhaana.vendor.application.dto.VendorProfileRequest;
import org.gharKaKhaana.vendor.application.dto.VendorProfileResponse;
import org.gharKaKhaana.vendor.common.exception.VendorProfileAlreadyExistsException;
import org.gharKaKhaana.vendor.domain.Vendor;
import org.gharKaKhaana.vendor.infrastructure.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorServiceImpl vendorService;

    private VendorProfileRequest request;

    @BeforeEach
    void setUp() {
        request = new VendorProfileRequest();
        request.setKitchenName("Test Kitchen");
        request.setOwnerName("Test Owner");
        request.setAddress("123 Test St");
        request.setPhone("1234567890");
    }

    @Test
    void testCreateProfile_Success() {
        Long userId = 1L;
        when(vendorRepository.existsByUserId(userId)).thenReturn(false);

        Vendor savedVendor = Vendor.builder()
                .id(10L)
                .userId(userId)
                .kitchenName(request.getKitchenName())
                .ownerName(request.getOwnerName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .rating(0.0)
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorProfileResponse response = vendorService.createProfile(userId, request);

        assertNotNull(response);
        assertEquals("Test Kitchen", response.getKitchenName());
        assertEquals(userId, response.getUserId());
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void testCreateProfile_AlreadyExists() {
        Long userId = 1L;
        when(vendorRepository.existsByUserId(userId)).thenReturn(true);

        assertThrows(VendorProfileAlreadyExistsException.class, () -> {
            vendorService.createProfile(userId, request);
        });

        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}
