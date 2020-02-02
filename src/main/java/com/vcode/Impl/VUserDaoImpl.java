package com.vcode.Impl;

import com.mongodb.client.result.UpdateResult;
import com.vcode.dao.VUserDao;
import com.vcode.entitiy.VUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

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
  public void updateUser(VUser user) {
    Query query = new Query(Criteria.where("id").is(user.getId()));
    Update update = new Update().set("nickname", user.getNickname()).set("email", user.getEmail());
    mongoTemplate.updateFirst(query,update,VUser.class);
  }
  
  @Override
  public void deleteUserById(Long id) {
    Query query = new Query(Criteria.where("id").is(id));
    mongoTemplate.remove(query, VUser.class);
  }
}
