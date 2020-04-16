package com.vcode.impl;

import com.vcode.dao.AboutDao;
import com.vcode.entity.About;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author moyee
 */
@Component
public class AboutDaoImpl implements AboutDao {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public AboutDaoImpl(MongoTemplate mongoTemplate){
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void updateAbout(About about) {
    About dbAbout = getAbout();
    if (dbAbout == null) {
      mongoTemplate.save(about);
    } else {
      Query query = new Query(Criteria.where("id").is(dbAbout.getId()));
      Update update = new Update().set("doc", about.getDoc());
      mongoTemplate.updateFirst(query, update, About.class);
    }
  }

  @Override
  public About getAbout() {
    Query query = new Query();
    return mongoTemplate.findOne(query, About.class);
  }

  @Override
  public boolean isExist() {
    About a = getAbout();
    return a != null;
  }
}
