package com.springproject.springbootapp.service;

import com.springproject.springbootapp.Application;
import com.springproject.springbootapp.Roles;
import com.springproject.springbootapp.model.User;
import com.springproject.springbootapp.model.dto.UserDTO;
import com.springproject.springbootapp.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.properties"})
@DirtiesContext
public class UserControllerServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserControllerService userControllerService;

    @Before
    @Transactional
    public void createDatabase() {
        userRepository.deleteAll();

        User user1 = new User();
        user1.setLogin("testUser1");
        user1.setUser_role(Roles.gardener);
        userRepository.save(user1);

        User user2 = new User();
        user2.setLogin("testUser2");
        user2.setUser_role(Roles.owner);
        userRepository.save(user2);
    }


    @Test
    public void testFindUsers() {
        List<UserDTO> users = userControllerService.findAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testAddUser() {
        List<UserDTO> usersBefore = userControllerService.findAllUsers();
        int initial = usersBefore.size();
        User addedUser = userControllerService.addUser("newUser", Roles.owner);
        assertEquals("newUser", addedUser.getLogin());

        List<UserDTO> usersAfter = userControllerService.findAllUsers();
        int after = usersAfter.size();
        assertEquals(initial + 1, after);

        UserDTO newUser = userControllerService.findUser("newUser");
        assertEquals("newUser", newUser.getLogin());
    }

    @Test
    public void testNotExistingUser() {
        UserDTO user = userControllerService.findUser("notExisting");
        assertNull(user);
    }

    @After
    @Transactional
    public void clear() {
        userRepository.deleteAll();
    }
}