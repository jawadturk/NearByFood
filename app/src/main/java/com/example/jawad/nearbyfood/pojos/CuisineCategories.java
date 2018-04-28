package com.example.jawad.nearbyfood.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class CuisineCategories {

    public String cuisineCategoryName;
    public String categoryImage;
    public String cuisineCategoryId;


    public CuisineCategories() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public CuisineCategories(String categoryName, String imageUrl, String categoryId) {
        this.cuisineCategoryName = categoryName;
        this.categoryImage=imageUrl;
        this.cuisineCategoryId = categoryId;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cuisineCategoryName", cuisineCategoryName);
        result.put("categoryImage", categoryImage);
        result.put("cuisineCategoryId", cuisineCategoryId);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
