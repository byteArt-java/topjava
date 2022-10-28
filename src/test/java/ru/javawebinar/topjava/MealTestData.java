package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

public class MealTestData {
    public static final Meal mealOne = new Meal(1,
            LocalDateTime.of(2015,6,1,12,0),
            "lunch",510);
    public static final Meal mealTwo = new Meal(2,
            LocalDateTime.of(2015,6,1,21,0),
            "dinner",1500);
    public static final Meal mealThree = new Meal(3,
            LocalDateTime.of(2015,6,1,15,0),
            "dinner",1500);

    public static Meal newMeal(){
        return new Meal(4,LocalDateTime.of(2015,7,5,10,0,0),
                "breakfast",700);
    }

    public static void asserMatch(Meal actual,Meal expected){
        assertThat(actual).isEqualTo(expected);
    }
}
