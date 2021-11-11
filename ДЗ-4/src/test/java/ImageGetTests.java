import dto.ImageResponse;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Story("Тесты на получение файла")
public class ImageGetTests extends BaseTest {

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

    @DisplayName("Получение файла авторизованным пользователем")
    @Test
    void getFileTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .get(Endpoints.API_IMAGE + uploadedImageHash);
    }

    @DisplayName("Получение файла неавторизованным пользователем")
    @Test
    void getFileUnAuthedTest() {
        given(requestSpecificationNoAuth, negativeResponseSpecificationUnauthorized)
                .get(Endpoints.API_IMAGE + uploadedImageHash);
    }

    @DisplayName("Пустой запрос на получение файла")
    @Test
    void getFileEmptyTest() {
        given(requestSpecificationWithAuth, negativeResponseSpecificationBadRequest)
                .get(Endpoints.API_IMAGE);
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }
}
