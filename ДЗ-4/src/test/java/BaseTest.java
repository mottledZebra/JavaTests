import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public abstract class BaseTest {
    static String uploadedImageHash;

    static String encodedFile;

    static MultiPartSpecification base64MultiPartSpec;

    static RequestSpecification requestSpecificationWithAuth;
    static RequestSpecification requestSpecificationNoAuth;
    static RequestSpecification requestSpecificationWithAuthWithBase64;

    static ResponseSpecification positiveResponseSpecification;
    static ResponseSpecification negativeResponseSpecificationBadRequest;
    static ResponseSpecification negativeResponseSpecificationUnauthorized;

    static Properties properties = new Properties();
    static String token;
    static String userName;
    static String clientId;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        userName = properties.getProperty("userName");
        clientId = properties.getProperty("clientId");

        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecificationNoAuth = new RequestSpecBuilder()
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", Endpoints.IMAGE_TYPE_BASE64)
                .addFormParam("name", Endpoints.IMAGE_NAME)
                .addMultiPart(base64MultiPartSpec)
                .build();

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        negativeResponseSpecificationBadRequest = new ResponseSpecBuilder()
                .expectBody("status", equalTo(400))
                .expectBody("success", is(false))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(400)
                .build();

        negativeResponseSpecificationUnauthorized = new ResponseSpecBuilder()
                .expectBody("status", equalTo(401))
                .expectBody("success", is(false))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(401)
                .build();
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(Endpoints.PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
