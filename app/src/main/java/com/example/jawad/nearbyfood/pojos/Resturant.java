package com.example.jawad.nearbyfood.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Resturant {

    public String resturantId;
    public String resturantName;
    public String resturantAddress;
    public ArrayList<String> resturantPhotos = new ArrayList<>();
    public ArrayList<String> resturantMenuPhotos = new ArrayList<>();
    public String resturantLocationLat;
    public String resturantLocationLong;
    public int resturantNumberOfReviews = 0;
    public double resturantRating = 0.0;
    public ArrayList<String> resturantCuisineCategories = new ArrayList<>();
    public ArrayList<String> resturantQuickSearchCategories = new ArrayList<>();

    public ArrayList<String> resturantCuisineCategoriesNamesList = new ArrayList<>();
    public ArrayList<String> resturantQuickSearchCategoriesNamesList = new ArrayList<>();
    public String phoneNumber="";
    public String cuisineTypes="";

    public Resturant(String resturantId, String resturantName, String resturantAddress, ArrayList<String> resturantPhotos, ArrayList<String> resturantMenuPhotos, String resturantLocationLat, String resturantLocationLong, int resturantNumberOfReviews, double resturantRating, ArrayList<String> resturantCuisineCategories, ArrayList<String> resturantQuickSearchCategories, String phoneNumber) {
        this.resturantId = resturantId;
        this.resturantName = resturantName;
        this.resturantAddress = resturantAddress;
        this.resturantPhotos = resturantPhotos;
        this.resturantMenuPhotos = resturantMenuPhotos;
        this.resturantLocationLat = resturantLocationLat;
        this.resturantLocationLong = resturantLocationLong;
        this.resturantNumberOfReviews = resturantNumberOfReviews;
        this.resturantRating = resturantRating;
        this.resturantCuisineCategories = resturantCuisineCategories;
        this.resturantQuickSearchCategories = resturantQuickSearchCategories;
        this.phoneNumber = phoneNumber;
    }



    public Resturant() {
    }



    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("phoneNumber",phoneNumber);
        result.put("resturantId", resturantId);
        result.put("cuisineTypes",cuisineTypes);
        result.put("resturantName", resturantName);
        result.put("resturantAddress", resturantAddress);
        result.put("resturantPhotos", resturantPhotos);
        result.put("resturantMenuPhotos", resturantMenuPhotos);
        result.put("resturantLocationLat", resturantLocationLat);
        result.put("resturantLocationLong", resturantLocationLong);
        result.put("resturantCuisineCategories", resturantCuisineCategories);
        result.put("resturantQuickSearchCategories",resturantQuickSearchCategories);
        result.put("resturantCuisineCategoriesNamesList",resturantCuisineCategoriesNamesList);
        result.put("resturantQuickSearchCategoriesNamesList",resturantQuickSearchCategoriesNamesList);



        return result;
    }
    // [END post_to_map]

}
// [END post_class]
