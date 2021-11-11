import dto.ImageResponse;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Story("Негативные тесты на удаление файла")
public class ImageDeleteTestsNegative extends BaseTest{

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

    @DisplayName("Удаление файла неавторизованным пользователем")
    @Test
    void deleteFileUnAuthedTest() {
        given(requestSpecificationNoAuth, negativeResponseSpecificationUnauthorized)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }

    @DisplayName("Пустой запрос на удаление файла")
    @Test
    void deleteEmptyFileTest() {
        given(requestSpecificationWithAuth, negativeResponseSpecificationBadRequest)
                .delete(Endpoints.API_IMAGE);
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }
}
