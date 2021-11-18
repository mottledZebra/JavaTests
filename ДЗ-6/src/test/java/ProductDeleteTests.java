import com.github.javafaker.Faker;
import db.dao.ProductsMapper;
import db.model.Products;
import dto.Product;
import enums.CategoryType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.Matchers.*;

public class ProductDeleteTests {
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
    void setUp() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
    }

    @DisplayName("Удаление продукта с валидным id")
    @Test
    void deleteProductTest() throws IOException {
        Products dbProductBeforeDelete = productsMapper.selectByPrimaryKey((long)id);

        productsMapper.deleteByPrimaryKey((long)id);

        assertThat(dbProductBeforeDelete.getId(), equalTo((long)id));
        assertThat(productsMapper.selectByPrimaryKey((long)id), equalTo(null));
    }

    @DisplayName("Удаление продукта с невалидным id")
    @Test
    void deleteProductWrongIdTest() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(0).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), equalTo(false));
    }
}
