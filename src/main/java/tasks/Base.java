package tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Base {

    protected String token = "";
    protected String ADMIN_USER_NAME = "admin";
    protected String ADMIN_USER_PASSWORD = "admin";
    protected static final String BASE_URL = "http://localhost:80";
    protected static final String URI_PRODUCT = "/product";
    protected static final String URI_USER_TOKEN = "/user/token";
    PojoProduct pojoProduct = new PojoProduct();

    //Выполняем запрос на получение токена
    @BeforeEach
    public void jws() {
        pojoProduct.setUsername(ADMIN_USER_NAME);
        pojoProduct.setPassword(ADMIN_USER_PASSWORD);
        String jsonString = gson.toJson(pojoProduct);

        Response responsePost = given()
                .contentType(ContentType.JSON)
                .body(jsonString)
                .when()
                .post(BASE_URL + URI_USER_TOKEN)
                .then()
                .extract().response();
        token = responsePost.jsonPath().getString("jwt");
    }

    //Заполняем заголовки + jws токен
    public Map<String, String> headersAll() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    //конвертируем наш POJO.PojoProduct в Json
    public Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
}
