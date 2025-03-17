package br.com.poc.fs.service;

import br.com.poc.fs.models.Product;
import br.com.poc.fs.payload.request.ProductRequest;
import br.com.poc.fs.payload.response.ProductResponse;
import br.com.poc.fs.repository.CategoryRepository;
import br.com.poc.fs.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAll() {

        Product product1 = new Product("1", "Product 1", 1, BigDecimal.TEN, null);
        Product product2 = new Product("2", "Product 2", 1, BigDecimal.TEN, null);
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        when(modelMapper.map(product1, ProductResponse.class)).thenReturn(new ProductResponse("1", "Product 1", BigDecimal.TEN));
        when(modelMapper.map(product2, ProductResponse.class)).thenReturn(new ProductResponse("2", "Product 2", BigDecimal.TEN));

        List<ProductResponse> result = productService.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetById_WhenProductExists() {

        String productId = "1";
        Product product = new Product(productId, "Product 1", 1, BigDecimal.TEN, null);
        ProductResponse response = new ProductResponse(productId, "Product 1", BigDecimal.TEN);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.getById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Product 1", result.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetById_WhenProductDoesNotExist() {

        String productId = "1";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductResponse result = productService.getById(productId);

        assertNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testSave_WhenCategoryExists() {

        ProductRequest request = new ProductRequest("Product 1", BigDecimal.TEN, "description", 1, null);
        Product product = new Product("Product", "Product 1", 1, BigDecimal.TEN, null);
        ProductResponse response = new ProductResponse("1", "Product 1", BigDecimal.TEN);

        when(categoryRepository.existsById(request.getIdCategory())).thenReturn(true);
        when(modelMapper.map(request, Product.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.save(request);

        assertNotNull(result);
        assertEquals("Product 1", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdate_WhenProductExists() {

        ProductResponse request = new ProductResponse("1", "Updated Product", BigDecimal.TEN);
        Product product = new Product("1", "Updated Product", 1, BigDecimal.TEN, null);
        ProductResponse response = new ProductResponse("1", "Updated Product", BigDecimal.TEN);

        when(productRepository.existsById(request.getId())).thenReturn(true);
        when(categoryRepository.existsById(any())).thenReturn(true);
        when(modelMapper.map(request, Product.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product, ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.update(request);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository, times(1)).existsById(request.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDelete() {
        String productId = "1";

        doNothing().when(productRepository).deleteById(productId);

        productService.delete(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}
