package br.com.poc.fs.repository;

import br.com.poc.fs.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
