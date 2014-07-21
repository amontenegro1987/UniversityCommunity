package com.bionic.socialNetwork.logic;

import com.bionic.socialNetwork.dao.impl.UserDaoImpl;
import com.bionic.socialNetwork.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class RegistrationTest {

    User user;
    String login = "regTestLogin";

    @Before
    public void addUserTest(){
        Registration registration = new Registration();
        registration.addUser("name", "surname",login,"surname");
    }


    @Test
    public void registerUserTest() throws Exception {
        UserDaoImpl userDao = new UserDaoImpl();
        user = new User();
        user = userDao.selectByLogin(login);
        assertNotNull(userDao.selectByLogin(login));

    }
    @After
    public void deleteCreatedUserTest() throws Exception {
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.delete(user);
        assertNull(userDao.selectByLogin(login));
    }

}