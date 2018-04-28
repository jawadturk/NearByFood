package com.example.jawad.nearbyfood.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class QuickSearchCategories {

    public String quickSearchCategoryName;
    public String categoryImage;
    public String quickSearchCategoryId;


    public QuickSearchCategories() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public QuickSearchCategories(String categoryName, String imageUrl, String categoryId) {
        this.quickSearchCategoryName = categoryName;
        this.categoryImage=imageUrl;
        this.quickSearchCategoryId = categoryId;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("quickSearchCategoryName", quickSearchCategoryName);
        result.put("categoryImage", categoryImage);
        result.put("quickSearchCategoryId", quickSearchCategoryId);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
