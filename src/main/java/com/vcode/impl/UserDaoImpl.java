package com.vcode.impl;

import com.vcode.dao.UserDao;
import com.vcode.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author moyee
 */
@Component
public class UserDaoImpl implements UserDao {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public UserDaoImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveUser(User user) {
    mongoTemplate.save(user);
  }

  @Override
  public User findUserByUserAccount(String account) {
    Query query = new Query(Criteria.where("account").is(account));
    return mongoTemplate.findOne(query, User.class);
  }

  @Override
  public String updateUser(User user) {
    Query query = new Query(Criteria.where("account").is(user.getAccount()));
    Update update = new Update()
            .set("nickname", user.getNickname())
            .set("email", user.getEmail())
            .set("role", user.getRole());
    mongoTemplate.updateFirst(query, update, User.class);
    return null;
  }

  @Override
  public void deleteUserByAccount(String account) {
    Query query = new Query(Criteria.where("account").is(account));
    mongoTemplate.remove(query, User.class);
  }

  @Override
  public List<User> findAdmins(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query(Criteria.where("role").is("admin"));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, User.class);
  }

  @Override
  public List<User> findUsers(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, User.class);
  }

  @Override
  public long count(String search) {
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, User.class);
  }

  @Override
  public long countAdmins(String search) {
    Query query = new Query(Criteria.where("role").is("admin"));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, User.class);
  }

  @Override
  public boolean isNicknameExist(User user) {
    Query query = new Query(Criteria.where("nickname").is(user.getNickname()).and("account").ne(user.getAccount()));
    return mongoTemplate.exists(query, User.class);
  }

  @Override
  public boolean isEmailExist(User user) {
    Query query = new Query(Criteria.where("email").is(user.getEmail()).and("account").ne(user.getAccount()));
    return mongoTemplate.exists(query, User.class);
  }
}
