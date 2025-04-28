package br.com.lessa.agregadorinvestimentos.controller;


import br.com.lessa.agregadorinvestimentos.entity.User;
import br.com.lessa.agregadorinvestimentos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){
        var UserId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + UserId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId){
        var user = userService.getUserById(userId);
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
        } else{
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<Void> updateUserByID(@PathVariable("userID") String userId, @RequestBody UpdateUserDto updateUserDto ){
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }


}
