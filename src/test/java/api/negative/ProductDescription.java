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
import static tasks.RND.getRandomDouble;
import static tasks.RND.getRandomString;

public class ProductDescription extends Base {

    private static final String ERROR_NUMBER_LIMIT_VALUES = "[[Length must be between 3 and 20 characters.]]";
    private static final String ERROR_NULL = "[[Field is mandatory.]]";
    private static final String ERROR_EMPTY_STRING = "[[Field is mandatory., Length must be between 3 and 20 characters.]]";
    private static final String ERROR_ARRAY = "Cannot deserialize value of type 'java.lang.String' from Array value (token 'JsonToken.START_ARRAY')";
    private static final Double PRICE = getRandomDouble(4);
    private static final String NAME = getRandomString(5);
    private String jsonString;
    PojoProduct pojoProduct = new PojoProduct();

    @Test
    @DisplayName("Тестирование поля description, получение ошибки 400. Передаем пустую строку и пробел")
    //Тут плавающий баг. Текст сообщения периодически меняется. Заведен дефект №1
    public void createProductDescriptionEmptyString() {

        ArrayList<Object> values = new ArrayList<>();
        values.add("");
        values.add(" ");

        for (Object invalidParam : values) {
            pojoProduct.setName(NAME);
            pojoProduct.setDescription(invalidParam);
            pojoProduct.setPrice(PRICE);
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
            System.out.println("Тестируем значение " + values);

            assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
            assertEquals(ERROR_EMPTY_STRING, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
        }
    }

    @Test
    @DisplayName("Тестирование поля description, получение ошибки 400. Передаем число и граничные значения")
    public void createProductDescriptionNumberAndLimitValues() {

        ArrayList<Object> values = new ArrayList<>();
        values.add(3);
        values.add(getRandomString(2));
        values.add(getRandomString(21));

        for (Object invalidParam : values) {
            pojoProduct.setName(NAME);
            pojoProduct.setDescription(invalidParam);
            pojoProduct.setPrice(PRICE);
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
            System.out.println("Тестируем значение " + values);

            assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
            assertEquals(ERROR_NUMBER_LIMIT_VALUES, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
        }
    }

    @Test
    @DisplayName("Тестирование поля description, получение ошибки 400. Не передаем description")
    public void createProductDescriptionWithOutParam() {

        pojoProduct.setName(NAME);
        pojoProduct.setPrice(PRICE);
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

        assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
        assertEquals(ERROR_NULL, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
    }

    @Test
    @DisplayName("Тестирование поля description, получение ошибки 400. Передаем null")
    public void createProductDescriptionNull() {

        pojoProduct.setName(NAME);
        pojoProduct.setPrice(PRICE);
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

        assertEquals(400, responsePost.statusCode(), "Код ответа не соответсвует");
        assertEquals(ERROR_NULL, responsePost.jsonPath().getString("errors.messages"), "Текст сообщения об ошибке не соответсвует");
    }

    @Test
    @DisplayName("Тестирование поля description, получение ошибки 400. Передаем массив")
    public void createProductDescriptionArray() {

        pojoProduct.setName(NAME);
        pojoProduct.setDescription(Collections.EMPTY_LIST);
        pojoProduct.setPrice(PRICE);
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

}
