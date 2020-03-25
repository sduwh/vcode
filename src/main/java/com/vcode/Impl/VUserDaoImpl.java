package com.vcode.Impl;

import com.vcode.dao.VUserDao;
import com.vcode.entity.VUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VUserDaoImpl implements VUserDao {
  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public void saveUser(VUser user) {
    mongoTemplate.save(user);
  }

  @Override
  public VUser findUserByUserAccount(String account) {
    Query query = new Query(Criteria.where("account").is(account));
    return mongoTemplate.findOne(query, VUser.class);
  }

  @Override
  public String updateUser(VUser user) {
    Query query = new Query(Criteria.where("account").is(user.getAccount()));
    Update update = new Update()
            .set("nickname", user.getNickname())
            .set("email", user.getEmail())
            .set("role", user.getRole());
    mongoTemplate.updateFirst(query, update, VUser.class);
    return null;
  }

  @Override
  public void deleteUserByAccount(String account) {
    Query query = new Query(Criteria.where("account").is(account));
    mongoTemplate.remove(query, VUser.class);
  }

  @Override
  public List<VUser> findAdmins(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query(Criteria.where("role").is("admin"));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, VUser.class);
  }

  @Override
  public List<VUser> findUsers(int page, int size, String search) {
    Pageable pageableRequest = PageRequest.of(page, size);
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("title").regex(".*" + search + ".*"));
    }
    query.with(pageableRequest);
    return mongoTemplate.find(query, VUser.class);
  }

  @Override
  public long count(String search) {
    Query query = new Query();
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, VUser.class);
  }

  @Override
  public long countAdmins(String search) {
    Query query = new Query(Criteria.where("role").is("admin"));
    if (search.length() > 0) {
      query.addCriteria(Criteria.where("nickname").regex(".*" + search + ".*"));
    }
    return mongoTemplate.count(query, VUser.class);
  }

  @Override
  public boolean isNicknameExist(VUser user) {
    Query query = new Query(Criteria.where("nickname").is(user.getNickname()).and("account").ne(user.getAccount()));
    return mongoTemplate.exists(query, VUser.class);
  }

  @Override
  public boolean isEmailExist(VUser user) {
    Query query = new Query(Criteria.where("email").is(user.getEmail()).and("account").ne(user.getAccount()));
    return mongoTemplate.exists(query, VUser.class);
  }
}
