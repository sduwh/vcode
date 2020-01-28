package com.vcode.domain;

import com.vcode.dao.VUserDao;
import com.vcode.entitiy.VUserEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class VUserDaoImpl implements VUserDao {
 
  @Resource
  private MongoTemplate mongoTemplate;
  
  @Override
  public void saveVUser(VUserEntity vuserEntity) {
    mongoTemplate.save(vuserEntity);
  }
  
  @Override
  public void removeVUser(Long Id) {
    mongoTemplate.remove(Id);
  }
  
  @Override
  public void updateVUser(VUserEntity vUserEntity) {
    Query query = new Query(Criteria.where("id").is(vUserEntity.getId()));
    Update update = new Update();
    update.set("nickname", vUserEntity.getNickname());
    update.set("account", vUserEntity.getAccount());
    update.set("email", vUserEntity.getEmail());
    update.set("password", vUserEntity.getPassword());
  
    mongoTemplate.updateFirst(query, update, VUserEntity.class);
  }
  
  @Override
  public VUserEntity findById(Long Id) {
    Query query = new Query(Criteria.where("id").is(Id));
    return mongoTemplate.findOne(query, VUserEntity.class);
  }
  
  @Override
  public VUserEntity findByAccount(String account) {
    Query query = new Query(Criteria.where("account").is(account));
    return mongoTemplate.findOne(query, VUserEntity.class);
  }
  
  @Override
  public boolean isExitVUser(String account) {
    VUserEntity user = findByAccount(account);
    return user != null;
  }
}
