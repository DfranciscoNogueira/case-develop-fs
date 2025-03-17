package br.com.poc.fs.service;

import br.com.poc.fs.models.User;
import br.com.poc.fs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserName() {

        User mockUser = new User();
        mockUser.setUserName("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(mockUser);

        User result = userService.findByUserName("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    void testExistsByUserName() {

        when(userRepository.existsByUserName("testUser")).thenReturn(true);

        Boolean result = userService.existsByUserName("testUser");

        assertTrue(result);
        verify(userRepository, times(1)).existsByUserName("testUser");
    }

    @Test
    void testExistsByEmail() {

        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        Boolean result = userService.existsByEmail("test@email.com");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("test@email.com");
    }

    @Test
    void testSave() {

        User user = new User();
        user.setUserName("newUser");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("newUser", result.getUserName());
        verify(userRepository, times(1)).save(user);
    }

}
