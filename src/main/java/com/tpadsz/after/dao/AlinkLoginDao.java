package com.tpadsz.after.dao;

import com.tpadsz.after.entity.AppUser;
import com.tpadsz.after.entity.LoginLog;
import org.apache.ibatis.annotations.Param;

/**
 * @program: blt-light
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 11:32
 **/
public interface AlinkLoginDao {

    AppUser findUserByAccount(String account);

    void saveLoginLog(LoginLog loginLog);

    void saveLoginOutLog(String uid) throws Exception;

    AppUser findUserByMobile(String mobile);

    void insert(@Param("mname")String mname, @Param("mesh_id")String mesh_id, @Param("pwd")String pwd, @Param("uid")String uid, @Param("project_id")String project_id);
}
