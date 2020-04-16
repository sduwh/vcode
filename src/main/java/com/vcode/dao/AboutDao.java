package com.vcode.dao;

import com.vcode.entity.About;

/**
 * @author moyee
 */
public interface AboutDao {

    void updateAbout(About about);

    About getAbout();

    boolean isExist();
}
