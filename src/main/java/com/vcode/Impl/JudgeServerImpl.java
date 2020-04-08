package com.vcode.Impl;

import com.vcode.common.RedisCode;
import com.vcode.dao.JudgeServerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author moyee
 * @version 1.0.0
 * @Description
 * @Date
 */
@Component
public class JudgeServerImpl implements JudgeServerDao {

  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  public JudgeServerImpl(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public List<String> getOJSupport() {
    String ojListStr = redisTemplate.opsForValue().get(RedisCode.OJ_LIST);
    List<String> ojList = new LinkedList<>();
    ojList.add("VCODE");
    if (ojListStr != null) {
      ojList.addAll(Arrays.asList(ojListStr.split(",")));
    }
    return ojList;
  }
}
