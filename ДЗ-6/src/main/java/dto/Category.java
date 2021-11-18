package dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Category {
    private Integer id;
    private String title;
    private ArrayList<Product> products;
}
