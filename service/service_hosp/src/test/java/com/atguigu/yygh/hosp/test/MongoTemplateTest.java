package com.atguigu.yygh.hosp.test;

import com.atguigu.yygh.hosp.test.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author wqz
 * @date 2022/11/3 17:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insert() {
        User user = new User();
        user.setName("王五");
        user.setAge(20);
        user.setGender(false);
        //user.setId("636460b0b484463cf1bd2546");
        //mongoTemplate.insert(user);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);
        mongoTemplate.insertAll(users);
    }

    @Test
    public void save() {
        User user = new User();
        user.setName("张三");
        user.setAge(22);
        user.setId("636460b0b484463cf1bd2546");
        mongoTemplate.save(user);
    }

    @Test
    public void save2() {
        User byId = mongoTemplate.findById("1", User.class);
        byId.setName("三妹");
        mongoTemplate.save(byId);
    }

    @Test
    public void remove() {
//        User user = new User();
//        user.setName("李四");
//        user.setAge(19);
//        user.setGender(true);
//        user.setId("1");
//        mongoTemplate.remove(user);

        Query query = new Query(Criteria.where("name").is("李四"));
        mongoTemplate.remove(query, User.class);
    }

    @Test
    public void update() {
        Query query = new Query(Criteria.where("name").is("李四"));
        Update update = new Update();
        update.set("name", "张三");
        mongoTemplate.updateMulti(query, update, User.class);
    }

    @Test
    public void findAll() {
        List<User> list = mongoTemplate.findAll(User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void findById() {
        User user = mongoTemplate.findById("1", User.class);
        System.out.println("user = " + user);
    }

    @Test
    public void andFind() {
        Query query = new Query();
        Criteria criteria = Criteria.where("age").is(20).and("name").is("王五");
        query.addCriteria(criteria);
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void orFind() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Criteria criteria1 = Criteria.where("age").is(20);
        Criteria criteria2 = Criteria.where("name").is("王五");
        criteria.orOperator(criteria1, criteria2);
        query.addCriteria(criteria);
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void likeFind() {
        //Query query = new Query(Criteria.where("name").regex(".*三.*"));

        //Query query = new Query(Criteria.where("name").regex(Pattern.compile(".*三.*")));

        String format = String.format("%s%s%s", ".*", "三", ".*");
        Pattern pattern = Pattern.compile(format, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void page() {
        int pageNum = 1;
        int pageSize = 3;
        User userVo = new User();
        userVo.setName("三");

        Query query = new Query(Criteria.where("name").regex(".*" + userVo.getName() + ".*"));
        long total = mongoTemplate.count(query, User.class);
        query.skip((pageNum-1)*pageSize).limit(pageSize);
        List<User> list = mongoTemplate.find(query, User.class);

        System.out.println("total = " + total);
        list.forEach(System.out::println);
    }

}
