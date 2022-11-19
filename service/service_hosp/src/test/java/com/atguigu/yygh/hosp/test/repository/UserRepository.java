package com.atguigu.yygh.hosp.test.repository;

import com.atguigu.yygh.hosp.test.pojo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author wqz
 * @date 2022/11/4 13:19
 */
public interface UserRepository extends MongoRepository<User, String> {
    //只可以自定义查询方法
    List<User> findByName(String name);

    List<User> findByNameLike(String name);

    List<User> findByGenderTrue();
}
