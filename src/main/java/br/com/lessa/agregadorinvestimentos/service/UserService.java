package br.com.lessa.agregadorinvestimentos.service;

import br.com.lessa.agregadorinvestimentos.controller.CreateUserDto;
import br.com.lessa.agregadorinvestimentos.controller.UpdateUserDto;
import br.com.lessa.agregadorinvestimentos.entity.User;
import br.com.lessa.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto){

        var entity = new User(null, createUserDto.username(), createUserDto.email(), createUserDto.password(), Instant.now(), null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId){
        return userRepository.findById(UUID.fromString(userId));
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public void updateUserById(String userId, UpdateUserDto updateUserDto){

        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){
            var user = userEntity.get();

            if(updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }
            if(updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);

        }
    }

    public void deleteById(String userId){
        var userExists = userRepository.existsById(UUID.fromString(userId));

        if (userExists){
            userRepository.deleteById(UUID.fromString(userId));
        }
    }
}
