import dto.Category;
import enums.CategoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.CategoryService;
import utils.PrettyLogger;
import utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryTests {
    static Retrofit client;
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        categoryService = client.create(CategoryService.class);
    }

    @DisplayName("Получение категории по валидному id")
    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService.getCategory(id).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(200));
        assertThat(response.isSuccessful(), equalTo(true));
        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
        response.body().getProducts().forEach(product ->
                assertThat(product.getCategoryTitle(), equalTo(CategoryType.FOOD.getTitle())));
    }

    @DisplayName("Получение категории по невалидному id")
    @Test
    void getCategoryByFailIdTest() throws IOException {
        Response<Category> response = categoryService.getCategory(4).execute();

        PrettyLogger.DEFAULT.log(response.toString());
        assertThat(response.code(), equalTo(404));
        assertThat(response.isSuccessful(), equalTo(false));
    }
}
