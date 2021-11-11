import dto.ImageResponse;
import io.qameta.allure.Story;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Story("Тесты на изменение информации о файле")
public class ImageUpdateTests extends BaseTest {

    @BeforeEach
    void setUp() {
        uploadedImageHash = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(Endpoints.API_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class)
                .getData().getId();
    }

    @DisplayName("Добавление файла в избранное")
    @Test
    void favoriteFileTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .post(Endpoints.API_IMAGE + uploadedImageHash + "/favorite");
    }

    @DisplayName("Обновление заголовка файла")
    @Test
    void updateFileTitleTest() {
        RequestSpecification requestSpecificationWithAuthAndNewTitle = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", Endpoints.IMAGE_TITLE)
                .build();

        given(requestSpecificationWithAuthAndNewTitle, positiveResponseSpecification)
                .post(Endpoints.API_IMAGE + uploadedImageHash);
    }

    @DisplayName("Обновление информации о файле неавторизованным пользователем")
    @Test
    void updateFileUnAuthedTest() {
        given(requestSpecificationNoAuth, negativeResponseSpecificationUnauthorized)
                .post(Endpoints.API_IMAGE + uploadedImageHash);
    }

    @DisplayName("Обновление информации файла с пустым запросом")
    @Test
    void updateEmptyFileInformationTest() {
        given(requestSpecificationWithAuth, negativeResponseSpecificationBadRequest)
                .post(Endpoints.API_IMAGE);
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }
}
