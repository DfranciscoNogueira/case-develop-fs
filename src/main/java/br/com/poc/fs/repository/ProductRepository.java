package br.com.poc.fs.repository;

import br.com.poc.fs.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIdIn(Set<String> ids);
    boolean existsByIdIn(Set<String> ids);
}
