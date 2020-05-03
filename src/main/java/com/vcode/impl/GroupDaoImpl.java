package com.vcode.impl;

import com.vcode.dao.GroupDao;
import com.vcode.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupDaoImpl implements GroupDao {
  private final MongoTemplate mongoTemplate;

  @Autowired
  public GroupDaoImpl(MongoTemplate mongoTemplate) { this.mongoTemplate = mongoTemplate; }

  @Override
  public void saveGroup(Group group) {
    mongoTemplate.save(group);
  }

  @Override
  public Group findGroupByName(String group_name) {
    Query query = new Query(Criteria.where("group_name").is(group_name));
    return mongoTemplate.findOne(query, Group.class);
  }

  @Override
  public void updateGroup(Group group) {
    Query query = new Query(Criteria.where("group_name").is(group.getGroup_name()));
    Update update = new Update().set("contests", group.getContests())
            .set("members",group.getMembers())
            .set("join_policy",group.getJoin_policy())
            .set("visible",group.isVisible())
            .set("owner",group.getOwner());
    mongoTemplate.updateFirst(query, update, Group.class);
  }

  @Override
  public void deleteGroupByName(String group_name) {
    Query query = new Query(Criteria.where("group_name").is(group_name));
    mongoTemplate.remove(query, Group.class);
  }

  @Override
  public boolean isExist(Group group) {
    Group g = findGroupByName(group.getGroup_name());
    return g != null;
  }

  @Override
  public List<Group> findAll() {
    return mongoTemplate.findAll(Group.class);
  }
}
