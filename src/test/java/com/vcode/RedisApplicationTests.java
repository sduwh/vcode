package com.vcode;


import com.vcode.entity.Tag;
import com.vcode.entity.VUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

  @Autowired
  private RedisTemplate<String, String> strRedisTemplate;
  @Autowired
  private RedisTemplate<String, Serializable> serializableRedisTemplate;

  @Test
  public void testString() {
    strRedisTemplate.opsForValue().set("strKey", "vcode");
    System.out.println(strRedisTemplate.opsForValue().get("strKey"));
  }

  @Test
  public void testSerializable() {
    Tag tag = new Tag();
    tag.setName("vcode1");

    serializableRedisTemplate.opsForValue().set("tag", tag);
    Tag user2 = (Tag) serializableRedisTemplate.opsForValue().get("tag");
    if (user2 != null)
      System.out.println("tag:" + user2.getName());
    else
      System.out.println("null");
  }


}
