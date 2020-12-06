package dao;

import models.DepartmentNews;
import models.GeneralNews;

import java.util.List;

public interface GeneralNewsDao {
    //create
    void addGeneralNews(GeneralNews generalnews);


    //read
    List<GeneralNews> getAll();
    // List<News> getAllReviewsByRestaurant(int restaurantId);
    // List<News> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId);
    GeneralNews findById(int id);
    //update
    //omit for now

    //delete
    void deleteById(int id);
    void clearAll();
}
