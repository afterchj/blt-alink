package com.tpadsz.after.service.impl;

import com.tpadsz.after.constants.MemcachedObjectType;
import com.tpadsz.after.dao.AlinkLoginDao;
import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import com.tpadsz.after.exception.AccountNotCorrectException;
import com.tpadsz.after.exception.AdminNotAllowedException;
import com.tpadsz.after.exception.PasswordNotCorrectException;
import com.tpadsz.after.exception.SystemAlgorithmException;
import com.tpadsz.after.service.AlinkLoginService;
import com.tpadsz.after.util.Encryption;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.UUID;


/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:24
 **/
@Service("alinkLoginService")
public class AlinkLoginServiceImpl implements AlinkLoginService {

    @Resource
    private AlinkLoginDao alinkLoginDao;

    @Autowired
    private XMemcachedClient client;

    @Override
    public AppUser loginByTpad(String input, String password, String inputFlag) throws SystemAlgorithmException,AccountNotCorrectException, PasswordNotCorrectException,AdminNotAllowedException {
        AppUser appUser = null;
        try {
            if("0".equals(inputFlag)) {
                appUser = alinkLoginDao.findUserByAccount(input);
            } else if("1".equals(inputFlag)){
                appUser = alinkLoginDao.findUserByMobile(input);
            } else if("2".equals(inputFlag)){
                appUser = alinkLoginDao.findUserByUname(input);
            }
        } catch (Exception e) {
            throw new SystemAlgorithmException();
        }
        if (appUser == null) {
            throw new AccountNotCorrectException();
        }
        Integer role_id = alinkLoginDao.findRoleIdByUid(appUser.getId());
        if(role_id!=4){
            throw new AdminNotAllowedException();
        }
        boolean isCorrect = checkPassword(password, appUser.getPwd(), appUser.getSalt());
        if (!isCorrect) {
            throw new PasswordNotCorrectException();
        }
        appUser.setPwd(null);
        appUser.setSalt(null);
        return appUser;
    }

    @Override
    public String generateToken(String uid) throws SystemAlgorithmException {
        String token = null;
        try {
            token = UUID.randomUUID().toString().replaceAll("-", "");
            String key = MemcachedObjectType.CACHE_TOKEN.getPrefix() + uid;
            client.set(key, 0, token);
        } catch (Exception e) {
            throw new SystemAlgorithmException();
        }
        return token;
    }

    @Override
    public Integer findRoleIdByUid(String uid) {
        return alinkLoginDao.findRoleIdByUid(uid);
    }


    @Override
    public boolean checkPassword(String actual, String expected, String salt) {
        if (StringUtils.isBlank(actual) || StringUtils.isBlank(expected) || StringUtils.isBlank(salt)) {
            return false;
        }
        String actualEncodingPassword = Encryption.encrypt(actual, salt);
        if (StringUtils.equals(actualEncodingPassword, expected)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveLoginLog(LoginLog loginLog) {
        alinkLoginDao.saveLoginLog(loginLog);
    }

    @Override
    public void loginOut(String uid) throws Exception {
        alinkLoginDao.saveLoginOutLog(uid);
    }

    @Override
    public AppUser findUserByMobile(String mobile) {
        return alinkLoginDao.findUserByMobile(mobile);
    }

}
