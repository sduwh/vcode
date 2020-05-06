package com.vcode.controller;

import com.vcode.common.ResponseCode;
import com.vcode.dao.UserDao;
import com.vcode.entity.Response;
import com.vcode.util.KaisaUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author:Frank
 * @Date:2020/5/2 20:07
 */
@RestController
@RequestMapping("/email")
@PropertySource("classpath:email.properties")
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JavaMailSender javaMailSender;

    private UserDao userDao;

    @Autowired
    public EmailController(JavaMailSender javaMailSender, UserDao userDao) {
        this.javaMailSender = javaMailSender;
        this.userDao = userDao;
    }

    private int key = 3;
    @Value("${email-from}")
    String from;// = "hengyushan9952@163.com";
    @Value("${email-subject}")
    String subject;// = "注册vcode-platform邮箱验证";
    @Value("${email-content}")
    String content;// = "<html>您好!" + "<br/>" + "欢迎注册vcode，请点击链接完成验证" + "<br/>" + "</html>";
    @Value("${email-checkEmailPage}")
    String checkEmailPage;// = "http://localhost:8000/api/email/receive";

    /**
     * 向用户发送邮件的api，使用了Util中的方法加密了email地址
     *
     * @param email 用户邮件
     * @return
     */
    @GetMapping("/send")
    public Response sendEmail(@RequestParam(value = "email") String email) {
        Response response = new Response();

        String encryptKey = KaisaUtil.encryptKaisa(email, key);
        String UUID = new ObjectId().toHexString();
        String url = "<a href=\"" + checkEmailPage + "?email=" + encryptKey + "&&UUID=" + UUID + "\">" +
                checkEmailPage + "?email=" + encryptKey + "&UUID=" + UUID + "</a>";
        String to = email;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            //设置邮件内容
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content + url, true);

            javaMailSender.send(mimeMessage);
            HashMap<String, String> hashMap = new HashMap<>(1);
            hashMap.put("UUID", UUID);
            response.setCode(ResponseCode.SUCCESS);
            response.setMessage("send success");
            response.setData(hashMap);
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR);
            response.setMessage("send error");
            logger.error("发送邮件时发生异常了！", e);
        }
        return response;
    }

    /**
     * 存储email到Redis的api，解密和邮箱格式验证，Key存活时间30*60s
     *
     * @param map，map中应该包含email和UUID
     * @return
     */
    @PostMapping("/receive")
    public Response receiveEmail(@RequestBody @Valid Map<String, String> map) {
        Response response = new Response();
        String email = map.get("email");
        String UUID = map.get("UUID");
        //加密解密+验证邮箱格式，防止用户自行修改url中的data访问，
        String decryptKey = KaisaUtil.decryptKaisa(email, key);
        String emailFormat = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";
        if (!decryptKey.matches(emailFormat)) {
            response.setCode(ResponseCode.ERROR);
            response.setMessage("email format is error");
            logger.debug(response.getMessage());
            return response;
        }

        try {
            userDao.storeEmailInRedis(decryptKey, UUID);
            response.setCode(ResponseCode.SUCCESS);
            response.setMessage("store msg in Redis");
        } catch (Exception e) {
            response.setCode(ResponseCode.ERROR);
            response.setMessage("store msg in Redis Error");
            logger.error(response.getMessage());
        }
        return response;
    }

    /**
     * 查看email是否已经存储在Redis的api
     *
     * @param email
     * @return
     */
    @GetMapping("/check")
    public Response checkEmail(@RequestParam(value = "email") String email, @RequestParam(value = "UUID") String UUID) {
        Response response = new Response();
        if (userDao.checkEmailInRedis(email, UUID)) {
            response.setCode(ResponseCode.SUCCESS);
            response.setMessage("user is already checked");
        } else {
            response.setCode(ResponseCode.FAIL);
            response.setMessage("user is not checked");
        }
        return response;
    }
}
