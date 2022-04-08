package api.positive;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Base;
import tasks.PojoProduct;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.RND.getRandomDouble;
import static tasks.RND.getRandomString;

public class ProductPositive extends Base {

    PojoProduct pojoProduct = new PojoProduct();
    private static final Double PRICE = getRandomDouble(3);

    @Test
    @DisplayName("Корректное создание продукта")
    public void createProduct() {

        ArrayList<Object> values = new ArrayList<>();
        values.add(getRandomString(3));
        values.add(getRandomString(10));
        values.add(getRandomString(20));

        for (Object validParam : values) {
            pojoProduct.setName(validParam);
            pojoProduct.setDescription(validParam);
            pojoProduct.setPrice(PRICE);
            String jsonString = gson.toJson(pojoProduct);

            Response responsePost = given()
                    .headers(headersAll())
                    .body(jsonString)
                    .when()
                    .post(BASE_URL + URI_PRODUCT)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().response();

            System.out.println("ResponseCreateProductPOST: \n" + responsePost.asString());
            System.out.println("Тестируем значение " + validParam);

            assertEquals(200, responsePost.statusCode(), "Код ответа не соответсвует");
            assertEquals(validParam, responsePost.jsonPath().getString("description"), "Description не соответсвует");
            assertEquals(validParam, responsePost.jsonPath().getString("name"), "Name не соответсвует");
            assertEquals(PRICE, responsePost.jsonPath().getDouble("price"), "Price не соответсвует");
        }
    }
}