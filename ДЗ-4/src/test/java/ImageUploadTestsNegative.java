import io.qameta.allure.Story;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Story("Негативные тесты на загрузку файла")
public class ImageUploadTestsNegative extends BaseTest{

    @DisplayName("Загрузка файла неавторизованным пользователем")
    @Test
    void uploadFileUnAuthedTest() {
        RequestSpecification requestSpecificationNoAuthWithBase64 = new RequestSpecBuilder()
                .addFormParam("type", Endpoints.IMAGE_TYPE_BASE64)
                .addFormParam("name", Endpoints.IMAGE_NAME)
                .addMultiPart(base64MultiPartSpec)
                .build();

        given(requestSpecificationNoAuthWithBase64, negativeResponseSpecificationUnauthorized)
                .post(Endpoints.API_UPLOAD);
    }

    @DisplayName("Загрузка файла неверного формата")
    @Test
    void uploadFileFailFormatTest() {
        RequestSpecification requestSpecificationWithAuthWrongType = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "text")
                .addFormParam("name", Endpoints.IMAGE_NAME)
                .addMultiPart(base64MultiPartSpec)
                .build();

        given(requestSpecificationWithAuthWrongType, negativeResponseSpecificationBadRequest)
                .post(Endpoints.API_UPLOAD);
    }

    @DisplayName("Пустой запрос на загрузку файла")
    @Test
    void uploadEmptyFileTest() {
        RequestSpecification requestSpecificationWithAuthEmptyFile = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", Endpoints.IMAGE_TYPE_BASE64)
                .addFormParam("name", Endpoints.IMAGE_NAME)
                .build();

        given(requestSpecificationWithAuthEmptyFile, negativeResponseSpecificationBadRequest)
                .post(Endpoints.API_UPLOAD);
    }
}
