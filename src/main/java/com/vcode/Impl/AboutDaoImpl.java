package com.vcode.Impl;

import com.vcode.dao.AboutDao;
import com.vcode.entity.About;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class AboutDaoImpl implements AboutDao {

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public void updateAbout(About about) {
    About _about = getAbout();
    if (_about == null) {
      mongoTemplate.save(about);
    } else {
      Query query = new Query(Criteria.where("id").is(_about.getId()));
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
    return a.getDoc() != null;
  }
}
