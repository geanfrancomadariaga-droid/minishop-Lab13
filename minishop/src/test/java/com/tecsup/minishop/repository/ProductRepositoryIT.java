package com.tecsup.minishop.repository;

import com.tecsup.minishop.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {
    @Autowired private ProductRepository repository;

    @Test
    void guardaLaptopLenovo() {
        Product saved = repository.saveAndFlush(Product.builder()
                .name("Laptop Lenovo").price(2500.0).stock(10).build());
        assertThat(saved.getId()).isNotNull().isPositive();
        assertThat(saved.getName()).isEqualTo("Laptop Lenovo");
        assertThat(saved.getPrice()).isEqualTo(2500.0);
        assertThat(saved.getStock()).isEqualTo(10);
    }

    @Test
    void encuentraMouseLogitechPorId() {
        Product saved = repository.saveAndFlush(Product.builder()
                .name("Mouse Logitech").price(80.0).stock(20).build());
        assertThat(repository.findById(saved.getId())).hasValueSatisfying(product -> {
            assertThat(product.getName()).isEqualTo("Mouse Logitech");
            assertThat(product.getPrice()).isEqualTo(80.0);
            assertThat(product.getStock()).isEqualTo(20);
        });
    }

    @Test
    void retornaOptionalVacioParaIdInexistente() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void buscaTecladoIgnorandoMayusculas() {
        repository.save(Product.builder().name("Teclado mecánico").price(150.0).stock(5).build());
        repository.save(Product.builder().name("TECLADO inalámbrico").price(120.0).stock(8).build());
        repository.save(Product.builder().name("Monitor").price(500.0).stock(3).build());
        repository.flush();
        assertThat(repository.findByNameContainingIgnoreCase("tEcLaDo"))
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("Teclado mecánico", "TECLADO inalámbrico");
    }
}
