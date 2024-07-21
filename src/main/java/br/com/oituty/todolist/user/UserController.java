package br.com.oituty.todolist.user;

import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var usar = this.userRepository.findByUsername(userModel.getUsername());
        if (usar != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

       var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
       userModel.setPassword(passwordHashred);

       var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @GetMapping("/{id}")
    public Optional<UserModel> getById(@RequestBody UUID id) {
        var userFinded = this.userRepository.findById(id);
        return userFinded;
    }

}
