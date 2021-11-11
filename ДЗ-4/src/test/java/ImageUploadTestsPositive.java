import dto.ImageResponse;
import io.qameta.allure.Story;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.io.File;

import static io.restassured.RestAssured.given;

@Story("Позитивные тесты на загрузку файла")
public class ImageUploadTestsPositive extends BaseTest {

    @DisplayName("Загрузка файла изображения")
    @Test
    void uploadFileImageTest() {
        MultiPartSpecification multiPartSpecWithFile = new MultiPartSpecBuilder(new File(Endpoints.PATH_TO_IMAGE))
                .controlName("image")
                .build();

        RequestSpecification requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", Endpoints.IMAGE_TYPE_FILE)
                .addFormParam("name", Endpoints.IMAGE_NAME)
                .addMultiPart(multiPartSpecWithFile)
                .build();

        uploadedImageHash = given(requestSpecificationWithAuthAndMultipartImage, positiveResponseSpecification)
                .post(Endpoints.API_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(ImageResponse.class)
                .getData().getId();
    }

    @DisplayName("Загрузка файла в формате base64")
    @Test
    void uploadFileBase64Test() {
        uploadedImageHash = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(Endpoints.API_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(Endpoints.API_IMAGE + uploadedImageHash);
    }
}
