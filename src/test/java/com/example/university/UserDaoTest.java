package com.example.university;

import com.example.university.db.DbManager;
import com.example.university.db.UserDao;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {


    @Before
    public void setUp() {
    }

    @Test
    public void shouldFindUserWithId1() {
        UserDao userDao = new UserDao();
        User user= userDao.find(1);
        Assert.assertNotNull(user);
    }

    @Test
    public void shouldDoCrud() {
        String email = "invalid_email--";
        User user = new User(email, "xx", "ff", "ll", Role.USER, "en");
        UserDao userDao = new UserDao();
        Assert.assertNull(userDao.find(email));
        userDao.update(user);
        Assert.assertNull(userDao.find(email, email));
        userDao.create(user);
        user = userDao.find(email);
        Assert.assertNotNull(user);
        user.setLang("ru");
        userDao.update(user);
        User user2 = new User();
        user2 = userDao.find(email);
        Assert.assertEquals("ru", user2.getLang());
        userDao.delete(user2);
        user2 = userDao.find(email);
        Assert.assertNull(user2);
    }

    @Test
    public void shouldReturnListForFindAll() {
        Assert.assertTrue(new UserDao().findAll().size() > 0);
    }

}
