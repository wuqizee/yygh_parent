package com.atguigu.yygh.hosp.test;

import com.atguigu.yygh.hosp.test.pojo.User;
import com.atguigu.yygh.hosp.test.repository.UserRepository;
import com.atguigu.yygh.model.hosp.Hospital;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wqz
 * @date 2022/11/4 13:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insert() {
        User user = new User();
        user.setName("jack");
        user.setGender(true);
        user.setAge(38);
        User insert = userRepository.insert(user);
        System.out.println("insert = " + insert);
    }

    @Test
    public void save() {
        User user = new User();
        user.setId("2");
        user.setName("joker");
        user.setAge(38);
        userRepository.save(user);
    }

    @Test
    public void saveAll() {
        User user = new User();
        user.setName("faker");
        user.setAge(17);
        user.setGender(false);
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);
        userRepository.saveAll(users);
    }

    @Test
    public void deleteById() {
        //userRepository.deleteById("2");

        User user = new User();
        user.setId("2");
        user.setName("faker");//没用,还是只根据id删
        userRepository.delete(user);
    }

    @Test
    public void update() {
        User user = userRepository.findById("2").get();
        user.setName("xiaohu");
        userRepository.save(user);
    }

    @Test
    public void find() {
        User user = userRepository.findById("1").get();
        System.out.println("user = " + user);
        long count = userRepository.count();
        System.out.println("count = " + count);
        List<User> list = userRepository.findAll();
        list.forEach(System.out::println);
    }

    @Test
    public void queryFind() {
        User user = new User();
        user.setId("2");
        Example<User> example = Example.of(user);
        List<User> list = userRepository.findAll(example);
        list.forEach(System.out::println);
    }

    @Test
    public void likeFind() {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();

        User user = new User();
        user.setName("三");
        Example<User> example = Example.of(user, exampleMatcher);

        List<User> list = userRepository.findAll(example);
        list.forEach(System.out::println);
    }

    @Test
    public void pageFind() {
        User user = new User();
        user.setGender(false);
        Example<User> example = Example.of(user);
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("age")));
        Page<User> page = userRepository.findAll(example, pageable);
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent());
    }

    @Test
    public void testMethod() {
        List<User> list1 = userRepository.findByGenderTrue();
        System.out.println("list1 = " + list1);
        List<User> list2 = userRepository.findByName("张三");
        System.out.println("list2 = " + list2);
        List<User> list3 = userRepository.findByNameLike("三");
        System.out.println("list3 = " + list3);
    }

}
