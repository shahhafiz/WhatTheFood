package fsktm.edu.whatthefood;

import android.util.Log;

public class Item {

    private String imageURL,calories, name;

    public Item(){

    }

    public Item(String imageURL, String calories, String name) {
        this.imageURL = imageURL;
        this.calories = calories;
        Log.d("Calorie","test");
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }
}
