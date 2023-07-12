package com.foodfetish.picker;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.utils.ProductLister;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductListerTest {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("uk_UA"));
        List<String> input = new ProductLister().getList();
        List<FoodProduct> list = new ArrayList<>();
        input.forEach(s->{
            String [] arr = s.replaceAll("\\ ", "").strip().split(":");
            System.out.println(arr[0]);
                FoodProduct el = new FoodProduct(arr[0].strip(),
                        Double.valueOf(arr[1].strip()),
                        Double.valueOf(arr[2].strip()),
                        Double.valueOf(arr[3].strip()));
                list.add(el);
        });
    }

}
