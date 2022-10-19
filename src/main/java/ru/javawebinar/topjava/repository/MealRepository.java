package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.UserMeal;

import java.util.Collection;

public interface MealRepository {
    // null if not found, when updated
    UserMeal save(UserMeal meal);

    // false if not found
    boolean delete(int id);

    // null if not found
    UserMeal get(int id);

    Collection<UserMeal> getAll();
}
