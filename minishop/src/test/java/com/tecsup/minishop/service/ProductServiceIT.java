package com.tecsup.minishop.service;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceIT {
    @Autowired private ProductService service;
    @MockBean private ProductRepository repository;

    @Test
    void guardaAuricularesSonyValidos() {
        Product product = Product.builder().name("Auriculares Sony").price(200.0).stock(0).build();
        Product saved = Product.builder().id(1L).name("Auriculares Sony").price(200.0).stock(0).build();
        when(repository.save(product)).thenReturn(saved);
        assertThat(service.save(product)).isEqualTo(saved);
        verify(repository).save(product);
    }

    @Test
    void rechazaPrecioCero() {
        Product product = Product.builder().name("Producto").price(0.0).stock(1).build();
        assertThatIllegalArgumentException().isThrownBy(() -> service.save(product))
                .withMessage("El precio debe ser mayor a cero");
        verifyNoInteractions(repository);
    }

    @Test
    void rechazaStockNegativo() {
        Product product = Product.builder().name("Producto").price(10.0).stock(-1).build();
        assertThatIllegalArgumentException().isThrownBy(() -> service.save(product))
                .withMessage("El stock no puede ser negativo");
        verifyNoInteractions(repository);
    }

    @Test
    void devuelveListaDeDosProductos() {
        Product first = Product.builder().id(1L).name("Mouse").price(50.0).stock(4).build();
        Product second = Product.builder().id(2L).name("Teclado").price(100.0).stock(2).build();
        when(repository.findAll()).thenReturn(List.of(first, second));
        assertThat(service.findAll()).containsExactly(first, second);
        verify(repository).findAll();
    }

    @Test
    void lanzaExcepcionCuandoId99NoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Producto no encontrado con id: 99");
        verify(repository).findById(99L);
    }
}
