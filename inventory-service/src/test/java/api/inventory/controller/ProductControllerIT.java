package api.inventory.controller;

import api.inventory.commons.FileUtils;
import api.inventory.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
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
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/product/init_two_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    @Test
    @DisplayName("GET v1/products/90 - Returning throw NotFoundException, case id not found")
    @Order(3)
    void findById_ReturnsThrowNotFoundException_WhenProductIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-find-by-id-404.json");
        var id = 90L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .get(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/products/filterName?name=Mouse Logitech - Returning every products where name equals parameter")
    @Order(4)
    @Sql(value = "/sql/product/init_one_product.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listAllName_ReturnsProducts_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-filter-name-Mouse-Logitech-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("filterName", "Mouse Logitech")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/products/filterName?name= - Returning every products, case parameter null")
    @Sql(value = "/sql/product/init_two_products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Order(5)
    void listAllName_ReturnsProducts_WhenNameIsNull() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-all-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("filterName", "Teclado")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("GET v1/products/filterName?name= - Returning empty list, case parameter null")
    @Order(6)
    void listAllName_ReturnsProducts_WhenNameIsBlank() throws Exception {
        var response = fileUtils.readResourceFile("product/get-product-filter-name-empty-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .queryParam("filterName", " ")
                .get(URL)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @DisplayName("POST v1/products - Saving one product")
    @Order(7)
    @Sql(value = "/sql/product/clean_products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesProduct_WhenSuccessFull() throws Exception {
        var request = fileUtils.readResourceFile("product/post-product-request-200.json");
        var expectedResponse = fileUtils.readResourceFile("product/post-product-response-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(Matchers.equalTo(expectedResponse))
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("DELETE v1/products/1 - Deleting one product by id")
    @Order(8)
    @Sql("/sql/product/init_one_product.sql")
    void deleteById_removesProduct_WhenSuccessFull() {
        var productById = repository.findAll().getFirst().getId();

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", productById)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @DisplayName("DELETE v1/products/90 - Deleting one product by id not exists and throw NotFoundException")
    @Order(9)
    void deleteById_ThrowNotFoundException_WhenIdIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("product/delete-product-response-404.json");
        var id = 90L;

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                .pathParam("id", id)
                .delete(URL + "/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo(response))
                .log().all();
    }


}
