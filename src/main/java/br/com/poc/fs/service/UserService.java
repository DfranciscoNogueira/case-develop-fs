package br.com.poc.fs.service;

import br.com.poc.fs.models.User;
import br.com.poc.fs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findByUserName(String username) {
        return this.repository.findByUserName(username);
    }

    public Boolean existsByUserName(String username) {
        return this.repository.existsByUserName(username);
    }

    public Boolean existsByEmail(String email) {
        return this.repository.existsByEmail(email);
    }

    public User save(User user) {
        return this.repository.save(user);
    }

}
