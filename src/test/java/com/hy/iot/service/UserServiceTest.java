package com.hy.iot.service;

import com.hy.iot.entity.CheckHistory;
import com.hy.iot.entity.User;
import com.hy.iot.enums.UserType;
import com.hy.iot.repository.CheckHistoryRepository;
import com.hy.iot.repository.ItemRepository;
import com.hy.iot.repository.UserItemRepository;
import com.hy.iot.repository.UserRepository;
import io.github.cdimascio.japierrors.ApiError;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CheckHistoryRepository checkHistoryRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserItemRepository userItemRepository;

    private List<User> users = Arrays.asList(
            new User("1111", "Leo", UserType.STUDENT, Boolean.FALSE),
            new User("2222", "Lee", UserType.STUDENT, Boolean.FALSE)
    );

    @Test
    public void test_get_all_user() {
        val example = Example.of(new User(), ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreNullValues());
        val pageable = PageRequest.of(0, 50);
        val pageImpl = new PageImpl<>(users, pageable, 10);
        Mockito.when(userRepository.findAll(example, pageable))
                .thenReturn(pageImpl);

        val page = userService.getAll(50, 0, null, null, null);

        Assert.assertNotNull(page);
        Assert.assertEquals(users, page.getContent());
        Assert.assertEquals(50 , page.getSize());
        Assert.assertEquals(0, page.getNumber());
    }

    @Test
    public void test_check_user_by_code() {
        val user = users.stream().findFirst().get();
        val history = new CheckHistory(user);
        Mockito.when(userRepository.findUserByCodeAndIsChecked(user.getCode(), Boolean.FALSE))
                .thenReturn(user);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);
        Mockito.when(checkHistoryRepository.save(history))
                .thenReturn(history);

        val result = userService.checkByCode(user.getCode());

        Assert.assertNotNull(result);
        Assert.assertEquals(user, result);
        Assert.assertEquals(history.getUser(), user);
        Assert.assertTrue(result.getIsChecked());
    }


    @Test
    public void test_add_student() throws ApiError {
        val user = new User("3333", "Lii", UserType.STUDENT, Boolean.FALSE);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        val result = userService.addUser("Lii", "3333", UserType.STUDENT, null, null);

        Assert.assertNotNull(result);

        Assert.assertEquals(UserType.STUDENT, result.getType());
        Assert.assertFalse(result.getIsChecked());

    }
}
