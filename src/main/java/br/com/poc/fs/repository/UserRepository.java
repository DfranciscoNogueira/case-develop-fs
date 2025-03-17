package br.com.poc.fs.repository;

import br.com.poc.fs.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

}
