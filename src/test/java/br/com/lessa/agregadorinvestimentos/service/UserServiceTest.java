package br.com.lessa.agregadorinvestimentos.service;


import br.com.lessa.agregadorinvestimentos.controller.CreateUserDto;
import br.com.lessa.agregadorinvestimentos.entity.User;
import br.com.lessa.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;


    @Nested
        class createUser {

            @Test
            @DisplayName("Should create user with success")
            void shouldCreateUserWithSuccess() {

                // Arrange
                var user = new User(
                        UUID.randomUUID(),
                        "Gabriel",
                        "gabriel@domain.com",
                        "12345",
                        null,
                        null);

                doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

                // Act
                var input = new CreateUserDto( "Gabriel", "gabriel@domain.com", "12345" );
                var output = userService.createUser(input);

                // Assert
                assertNotNull(output);
                var userCaptured = userArgumentCaptor.getValue();
                assertEquals(input.username(), userCaptured.getEmail());
            }
        }


    @Nested
        class GetUserById {

            @Test
            @DisplayName("Should get user by id with success")
            void shouldGetUserByIdWithSuccess() {

                var user = new User(UUID.randomUUID(), "Gabriel", "gabriel@domain.com", "12345", null, null);

                doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

                var output = userService.getUserById(user.getUserId().toString()); // Usado indiretamente para disparar a execucao do userRepository

                var uuidCaptured = uuidArgumentCaptor.getValue();

                assertTrue(output.isPresent());
                assertEquals(user.getUserId(), uuidCaptured);
            }

            @Test
            @DisplayName("Should not get user by id")
            void shouldNotGetUserById() {
                var user = new User(UUID.randomUUID(), "Gabriel", "gabriel@domain.com", "12345", null, null);

                doReturn(Optional.empty()).when(userRepository).findById(any());

                var output = userService.getUserById(user.getUserId().toString());

                verify(userRepository, times(1)).findById(any());

                assertFalse(output.isPresent());
                assertEquals(Optional.empty(), output);
            }


        }

    @Nested
        class GetAllUsers {

            @Test
            @DisplayName("Should Get All Users With success")
            void shouldGetAllUsersWithSuccess() {
                var user = new User(UUID.randomUUID(), "Gabriel", "gabriel@domain.com", "12345", null, null);

                when(userRepository.findAll()).thenReturn(List.of(user));

                var output = userService.getAllUsers();

                verify(userRepository, times(1)).findAll();

                assertFalse(output.isEmpty());
                assertEquals(1, output.size());
            }


    }

    @Test
    void updateUserById() {
    }

    @Nested
    class DeleteById{
        @Test
        @DisplayName("Should delete user by id with success")
        void shouldDeleteUserByIdIfUserExistsEqualTrue () {
            // Arrange
            when(userRepository.existsById(uuidArgumentCaptor.capture())).thenReturn(true);
           doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

           //Act
           var userId = UUID.randomUUID();
           userService.deleteById(userId.toString());

           assertEquals(userId, uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should not delete user because userExists is false")
        void shouldNotDeleteUserByIdIfUserExistsEqualFalse(){
            // Arrange
            when(userRepository.existsById(uuidArgumentCaptor.capture())).thenReturn(false);
            // Act
            var userId = UUID.randomUUID();
            userService.deleteById(userId.toString());

            verify(userRepository, never()).deleteById(any());
        }
    }
}