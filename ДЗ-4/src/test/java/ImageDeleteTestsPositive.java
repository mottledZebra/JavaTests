import dto.ImageResponse;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Story("Позитивные тесты на удаление файла")
public class ImageDeleteTestsPositive extends BaseTest{

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

    @DisplayName("Удаление загруженного файла")
    @Test
    void deleteFileTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }
}
