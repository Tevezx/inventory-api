package api.inventory.controller;

import api.inventory.commons.ProductUtils;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT {
    private final static String URL = "/v1/products";
    @LocalServerPort
    private int port;
    @Autowired
    private ProductUtils productUtils;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.port = port;
    }
}
