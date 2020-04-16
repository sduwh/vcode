package com.vcode.impl;

import com.vcode.dao.TagDao;
import com.vcode.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TagDaoImpl implements TagDao {

  private final MongoTemplate mongoTemplate;

  @Autowired
  public TagDaoImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void saveTag(Tag tag) {
    mongoTemplate.save(tag);
  }

  @Override
  public Tag findTagByName(String name) {
    Query query = new Query(Criteria.where("name").is(name));
    return mongoTemplate.findOne(query, Tag.class);
  }

  @Override
  public void updateTag(Tag tag) {
    Query query = new Query(Criteria.where("name").is(tag.getName()));
    Update update = new Update().set("problems", tag.getProblems());
    mongoTemplate.updateFirst(query, update, Tag.class);
  }

  @Override
  public void deleteTagByName(String name) {
    Query query = new Query(Criteria.where("name").is(name));
    mongoTemplate.remove(query, Tag.class);
  }

  @Override
  public boolean isExist(Tag tag) {
    Tag t = findTagByName(tag.getName());
    return t != null;
  }

  @Override
  public List<Tag> findAll() {
    return mongoTemplate.findAll(Tag.class);
  }
}
