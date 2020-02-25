package com.vcode.controller;

import com.vcode.common.ResponseCode;
import com.vcode.dao.AboutDao;
import com.vcode.entity.About;
import com.vcode.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/about")
public class AboutController {
    @Autowired
    private AboutDao aboutDao;

    @PostMapping("/doc")
    public Response editDoc(@RequestBody @Valid About about) {

        Response response = new Response();

        aboutDao.updateAbout(about);

        return response;
    }

    @GetMapping("/doc")
    public Response getDoc() {

        Response response = new Response();

        if (!aboutDao.isExist()) {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("doc is not exist");
            response.setData("");
        } else {
            response.setData(aboutDao.getAbout().getDoc());
        }
        return response;
    }
}
