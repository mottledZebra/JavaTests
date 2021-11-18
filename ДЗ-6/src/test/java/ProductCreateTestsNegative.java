import com.github.javafaker.Faker;
import db.dao.ProductsMapper;
import dto.Product;
import enums.CategoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.ProductService;
import utils.DbUtils;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProductCreateTestsNegative {
    static ProductsMapper productsMapper;
    static Retrofit client;
    static ProductService productService;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
    }

    @DisplayName("Создание продукта с непустым id")
    @Test
    void postProductWithIdTest() throws IOException {
        product = new Product()
                .withId(1);
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустым заголовком")
    @Test
    void postProductWithNullTitleTest() throws IOException {
        product = new Product()
                .withTitle(null)
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустой ценой")
    @Test
    void postProductWithNullPriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(null)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с пустой категорией")
    @Test
    void postProductWithNullCategoryTitleTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(null);
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с нулевой ценой")
    @Test
    void postProductWithZeroPriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(0)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

    @DisplayName("Создание продукта с отрицательной ценой")
    @Test
    void postProductWithNegativePriceTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(-1)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(400));
        assertThat(response.isSuccessful(), equalTo(false));
    }

}
