package api.inventory.controller;

import api.inventory.commons.FileUtils;
import api.inventory.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT {
    private final static String URL = "/v1/products";
    @Autowired
    private ProductRepository repository;
    @LocalServerPort
    private int port;
    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET v1/products - Returning every products cadastred")
    @Order(1)
    @Sql(value = "/sql/product/init_two_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnsAllProducts_WhenSuccessFull() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-all-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/products/1 - Returning product by id")
    @Order(2)
    @Sql(value = "/sql/product/init_one_product.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnsProduct_WhenProductIdIsFound() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-find-by-id-1-200.json");
        var productById = repository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", productById)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }
}
