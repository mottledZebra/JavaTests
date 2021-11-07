import io.qameta.allure.Story;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

@Story("Image API tests")
public class ImageTests extends BaseTest{

    private final String PATH_TO_IMAGE = "src/test/resources/f_222772.jpg";
    private final String DATA_TYPE = "base64";
    private final String IMAGE_NAME = "CoolImage";
    private final String IMAGE_TITLE = "new base64 image";
    static String encodedFile;
    static String uploadedImageHash;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
    }

    @DisplayName("Загрузка файла в формате base64")
    @Test
    void uploadFileTest() {
        uploadedImageHash = given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .multiPart("type", DATA_TYPE)
                .multiPart("name", IMAGE_NAME)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @DisplayName("Добавление файла в избранное")
    @Test
    void favoriteFileTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedImageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Проверка присутствия файла в избранном")
    @Test
    void getFavoriteFileTest() {
        given()
                .header("Authorization", clientId)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.favorite", equalTo(true))
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", uploadedImageHash)
                .prettyPeek();
    }

    @DisplayName("Обновление заголовка файла")
    @Test
    void updateFileTitleTest() {
        given()
                .headers("Authorization", token)
                .multiPart("title", IMAGE_TITLE)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}", uploadedImageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Удаление загруженного файла")
    @Test
    void deleteFileTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .delete("https://api.imgur.com/3/image/{imageHash}", uploadedImageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Загрузка файла неавторизованным пользователем")
    @Test
    void uploadFileUnAuthedTest() {
        given()
                .multiPart("image", encodedFile)
                .multiPart("type", DATA_TYPE)
                .multiPart("name", IMAGE_NAME)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(401);
    }

    @DisplayName("Загрузка файла неверного формата")
    @Test
    void uploadFileFailFormatTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .multiPart("type", "text")
                .multiPart("name", IMAGE_NAME)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @DisplayName("Загрузка пустого файла")
    @Test
    void uploadEmptyFileTest() {
        given()
                .headers("Authorization", token)
                .multiPart("type", DATA_TYPE)
                .multiPart("name", IMAGE_NAME)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @DisplayName("Обновление информации файла с пустым запросом")
    @Test
    void updateEmptyFileInformationTest() {
        given()
                .headers("Authorization", token)
                .multiPart("title", IMAGE_TITLE)
                .when()
                .post("https://api.imgur.com/3/image/")
                .prettyPeek()
                .then()
                .statusCode(400);
    }

    @DisplayName("Удаление файла неавторизованным пользователем")
    @Test
    void deleteFileUnAuthedTest() {
        given()
                .when()
                .delete("https://api.imgur.com/3/image/{imageHash}", uploadedImageHash)
                .prettyPeek()
                .then()
                .statusCode(401);
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
