package br.com.poc.fs.service;

import br.com.poc.fs.enums.ERole;
import br.com.poc.fs.models.Role;
import br.com.poc.fs.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName_WhenRoleExists() {

        ERole roleName = ERole.ADMIN;
        Role mockRole = new Role();
        mockRole.setName(roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(mockRole));

        Optional<Role> result = roleService.findByName(roleName);

        assertTrue(result.isPresent());
        assertEquals(roleName, result.get().getName());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void testFindByName_WhenRoleDoesNotExist() {

        ERole roleName = ERole.USER;
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findByName(roleName);

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByName(roleName);
    }
}
