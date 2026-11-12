package com.tecsup.minishop.controller;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository repository;

    @BeforeEach
    void limpiaBaseDeDatos() {
        repository.deleteAll();
    }

    @Test
    void postWebcamLogitechDevuelve201() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Webcam Logitech","price":180.0,"stock":12}
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value("Webcam Logitech"))
                .andExpect(jsonPath("$.price").value(180.0))
                .andExpect(jsonPath("$.stock").value(12));
        assertThat(repository.findAll()).singleElement().satisfies(product -> {
            assertThat(product.getName()).isEqualTo("Webcam Logitech");
            assertThat(product.getPrice()).isEqualTo(180.0);
            assertThat(product.getStock()).isEqualTo(12);
        });
    }

    @Test
    void getListaDevuelveDosProductos() throws Exception {
        repository.save(Product.builder().name("Disco SSD").price(250.0).stock(5).build());
        repository.save(Product.builder().name("RAM 16GB").price(180.0).stock(10).build());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Disco SSD", "RAM 16GB")));
    }

    @Test
    void getImpresoraHpPorId() throws Exception {
        Product saved = repository.save(Product.builder().name("Impresora HP").price(600.0).stock(3).build());
        mockMvc.perform(get("/api/products/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Impresora HP"))
                .andExpect(jsonPath("$.price").value(600.0))
                .andExpect(jsonPath("$.stock").value(3));
    }

    @Test
    void getId9999Devuelve500() throws Exception {
        mockMvc.perform(get("/api/products/9999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Producto no encontrado con id: 9999"));
    }
}
