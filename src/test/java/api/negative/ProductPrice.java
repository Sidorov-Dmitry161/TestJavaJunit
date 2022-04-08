package api.negative;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Base;
import tasks.PojoProduct;

import java.util.ArrayList;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.RND.getRandomString;

public class ProductPrice extends Base {

    private static final String ERROR_NUMBER_LIMIT_VALUES = "[[Maximum number of integral digits is 7, maximum number of fractional digits is 2]]";
    private static final String ERROR_EMPTY_SPACE = "[[Value must be positive]]";
    private static final String ERROR_ARRAY = "Cannot deserialize value of type 'double' from Array value (token 'JsonToken.START_ARRAY')";
    private static final String DESCRIPTION = getRandomString(10);
    private static final String NAME = getRandomString(5);
    private String jsonString;
    PojoProduct pojoProduct = new PojoProduct();

    @Test
    @DisplayName("Тестирование поля price, получение ошибки 400. Передаем null, пустую строку, пробел, 0")
    public void createProductPriceNullEmptyStringAndSpace() {

        ArrayList<Object> values = new ArrayList<>();
        values.add(null);
        values.add("");
        values.add(" ");
        values.add(0);

        for (Object invalidParam : values) {
            pojoProduct.setName(NAME);
            pojoProduct.setDescription(DESCRIPTION);
            pojoProduct.setPrice(invalidParam);
            jsonString = gson.toJson(pojoProduct);

            Response responsePost = given()
                    .headers(headersAll())
                    .body(jsonString)
                    .when()
                    .post(BASE_URL + URI_PRODUCT)
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract().response();

            System.out.println("ResponseCreateProductPOST: \n" + responsePost.asString());
            System.out.println("Тестируем значение " + invalidParam);

            assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
            assertEquals(ERROR_EMPTY_SPACE, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
        }
    }

    @Test
    @DisplayName("Тестирование поля name, получение ошибки 400. Передаем массив")
    public void createProductPriceArray() {

        pojoProduct.setName(NAME);
        pojoProduct.setDescription(DESCRIPTION);
        pojoProduct.setPrice(Collections.EMPTY_LIST);
        jsonString = gson.toJson(pojoProduct);

        Response responsePost = given()
                .headers(headersAll())
                .body(jsonString)
                .when()
                .post(BASE_URL + URI_PRODUCT)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .extract().response();

        System.out.println("ResponseCreateProductPOST: \n" + responsePost.asString());

        assertEquals(422, responsePost.statusCode(), "Код ответа не соответсвует");
        assertEquals(ERROR_ARRAY, responsePost.jsonPath().getString("message"), "Текст сообщения об ошибке не соответсвует");
    }

    @Test
    @DisplayName("Тестирование поля name, получение ошибки 400. Ввод граничных значений")
    public void createProductPriceLimitValue() {

        ArrayList<Object> values = new ArrayList<>();
        values.add(1.234);
        values.add(12345678);
        values.add(1234567.899);

        for (Object invalidParam : values) {
            pojoProduct.setName(NAME);
            pojoProduct.setDescription(DESCRIPTION);
            pojoProduct.setPrice(invalidParam);
            jsonString = gson.toJson(pojoProduct);

            Response responsePost = given()
                    .headers(headersAll())
                    .body(jsonString)
                    .when()
                    .post(BASE_URL + URI_PRODUCT)
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract().response();

            System.out.println("ResponseCreateProductPOST: \n" + responsePost.asString());
            System.out.println("Тестируем значение " + invalidParam);

            assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
            assertEquals(ERROR_NUMBER_LIMIT_VALUES, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
        }
    }
}
