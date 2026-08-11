package api.inventory.repository;

import api.inventory.commons.ProductUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(ProductUtils.class)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductUtils productUtils;

    @Test
    @DisplayName("Saving product when success ful")
    @Order(1)
    @Sql("/sql/product/init_one_product.sql")
    void save_CreatesProduct_WhenSuccessFul() {
        var product = productUtils.newProduct();
        var productSave = repository.save(product);

        Assertions.assertThat(productSave).hasNoNullFieldsOrProperties();
        Assertions.assertThat(product).isEqualTo(productSave);
        Assertions.assertThat(productSave.getId()).isNotNull().isPositive();
    }
}