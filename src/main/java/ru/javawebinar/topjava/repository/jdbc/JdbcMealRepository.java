package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {
    private final static BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate).withTableName("meals").
                usingGeneratedKeyColumns("id");


    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().
                addValue("id",meal.getId()).
                addValue("dateTime",meal.getDateTime()).
                addValue("description",meal.getDescription()).
                addValue("calories",meal.getCalories()).
                addValue("user_id",userId);
        if (meal.isNew()){
            Number newKey = insertMeal.executeAndReturnKey(mapSqlParameterSource);
            meal.setId(newKey.intValue());
        }else if (namedParameterJdbcTemplate.update("UPDATE meals SET dateTime=:dateTime," +
                "description=:description,calories=:calories WHERE id=:id",mapSqlParameterSource) == 0){
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=?",id) == 1;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> queryList = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_Id=?", ROW_MAPPER,
                id,userId);
        return queryList.isEmpty() ? null : queryList.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id=?",ROW_MAPPER,userId).
                stream().sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE id=?",ROW_MAPPER,userId).
                stream().
                filter(meal-> Util.isBetweenHalfOpen(meal.getDateTime(),startDateTime,endDateTime)).
                sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }
}
