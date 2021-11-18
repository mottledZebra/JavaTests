import com.github.javafaker.Faker;
import db.dao.ProductsMapper;
import dto.Product;
import enums.CategoryType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.ProductService;
import utils.DbUtils;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProductCreateTestsPositive {
    static ProductsMapper productsMapper;
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;
    int id;

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @DisplayName("Создание продукта с валидными данными")
    @Test
    void postProductTest() throws IOException {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);

        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();

        Integer countProductsAfter = DbUtils.countProducts(productsMapper);

        PrettyLogger.DEFAULT.log(response.toString());

        assertThat(countProductsAfter, equalTo(countProductsBefore + 1));

        assertThat(response.code(), equalTo(201));
        assertThat(response.isSuccessful(), equalTo(true));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @AfterEach
    void tearDown() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();

        assertThat(response.code(), equalTo(200));
        assertThat(response.isSuccessful(), equalTo(true));
    }
}
