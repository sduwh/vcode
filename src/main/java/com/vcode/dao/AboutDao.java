package com.vcode.dao;

import com.vcode.entity.About;

public interface AboutDao {

    void updateAbout(About about);

    About getAbout();

    boolean isExist();
}
