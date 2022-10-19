package ru.javawebinar.topjava.web;

import org.apache.taglibs.standard.lang.jstl.test.PageContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.InMemoryUserMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealServlet.class);
    private InMemoryUserMealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryUserMealRepository();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),Integer.valueOf(request.getParameter("calories")));
        LOGGER.info(userMeal.isNew() ? "Create {} " : "Update {}",userMeal);
        repository.save(userMeal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("forward to meals");
        String action = request.getParameter("action");
        if (action == null){
            request.setAttribute("mealList", MealsUtil.getWithExceeded(repository.getAll()
                    ,MealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        }else if (action.equals("delete")){
            int id = getId(request);
            LOGGER.info("Delete {}",id);
            repository.delete(id);
            response.sendRedirect("meals");
        }else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LocalDateTime.now(),"",1000) :
                    repository.get(getId(request));
            request.setAttribute("meal",meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request,response);
        }


    }

    private int getId(HttpServletRequest request){
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return paramId.matches("^\\d+$") ? Integer.parseInt(paramId) : 0;
    }
}
