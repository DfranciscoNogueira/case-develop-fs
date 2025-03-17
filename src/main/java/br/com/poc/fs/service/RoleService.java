package br.com.poc.fs.service;

import br.com.poc.fs.enums.ERole;
import br.com.poc.fs.models.Role;
import br.com.poc.fs.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Optional<Role> findByName(ERole name) {
        return this.repository.findByName(name);
    }

}
