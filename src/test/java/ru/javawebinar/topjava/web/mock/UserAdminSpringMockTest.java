package ru.javawebinar.topjava.web.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.user.AdminRestController;

@ContextConfiguration({"classpath:spring/spring-app.xml"})
@RunWith(SpringRunner.class)
public class UserAdminSpringMockTest {

    @Autowired
    private AdminRestController controller;


    @Test
    public void testCreate() {
        controller.create(new User(null,"Name","email@ya.ru","pass-test", Role.USER));
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception{
        controller.delete(0);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() {
        controller.delete(7);
    }
}
