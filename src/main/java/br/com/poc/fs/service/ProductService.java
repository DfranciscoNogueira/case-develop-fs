package br.com.poc.fs.service;

import br.com.poc.fs.exception.NoSuchElementFoundException;
import br.com.poc.fs.models.Product;
import br.com.poc.fs.payload.request.ProductRequest;
import br.com.poc.fs.payload.response.ProductResponse;
import br.com.poc.fs.repository.CategoryRepository;
import br.com.poc.fs.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductResponse> listAll() {
        List<Product> products = this.productRepository.findAll();
        return products.stream().map(this::convertToDto).toList();
    }

    public ProductResponse getById(String id) {
        Optional<Product> entity = this.productRepository.findById(id);
        return entity.map(product -> this.modelMapper.map(product, ProductResponse.class)).orElse(null);
    }

    public ProductResponse save(ProductRequest dto) {

        Optional.ofNullable(dto.getIdCategory()).ifPresent(idCategory -> {
            if (!this.categoryRepository.existsById(idCategory))
                throw new NoSuchElementFoundException(String.format("Category with id %s not found", idCategory));
        });

        Product request = this.modelMapper.map(dto, Product.class);
        Product entitySave = this.productRepository.save(request);

        return this.modelMapper.map(entitySave, ProductResponse.class);
    }

    public ProductResponse update(ProductResponse dto) {

        Optional.ofNullable(dto.getId()).ifPresent(idProduct -> {
            if (!this.productRepository.existsById(idProduct))
                throw new NoSuchElementFoundException(String.format("Product with id %s not found", idProduct));
        });

        Optional.ofNullable(dto.getCategory()).ifPresent(category -> {
            if (Objects.nonNull(category.getId()) && !this.categoryRepository.existsById(category.getId()))
                throw new NoSuchElementFoundException(String.format("Category with id %s not found", category.getId()));
        });

        Product request = this.modelMapper.map(dto, Product.class);
        Product entitySave = this.productRepository.save(request);

        return this.modelMapper.map(entitySave, ProductResponse.class);
    }

    public void delete(String id) {
        this.productRepository.deleteById(id);
    }

    private ProductResponse convertToDto(Product product) {
        return this.modelMapper.map(product, ProductResponse.class);
    }

}
