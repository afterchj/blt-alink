package com.tpadsz.after.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpadsz.after.entity.FileRecord;
import com.tpadsz.after.entity.NewestFile;
import com.tpadsz.after.entity.UpdateInfo;
import com.tpadsz.after.entity.dd.ResultDict;
import com.tpadsz.after.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/update")
public class UpdateController extends BaseDecodedController {

    @Autowired
    UpdateService updateService;

    /**
     *ALINK灯联网升级接口
     * @param params appId,versionCode,userId
     * @param model
     */
    @RequestMapping(value = "/update_dlw",method = RequestMethod.POST)
    public void updateDlw(@ModelAttribute("decodedParams") JSONObject params, ModelMap model){
        UpdateInfo updateInfo ;
        String uid;
        String account;
        try {
            uid = params.getString("uid");
            account = updateService.findAccountByUid(uid);
            updateInfo= setUpdateInfo(params);
            model.put("result", ResultDict.SUCCESS.getCode());
            model.put("result_message", ResultDict.SUCCESS.getValue());
            model.put("account", account);
            model.put("data", updateInfo);
        } catch (Exception e) {
            e.printStackTrace();
            model.put("result", ResultDict.SYSTEM_ERROR.getCode());
            model.put("result_message", ResultDict.SYSTEM_ERROR.getValue());
        }
    }

    public UpdateInfo setUpdateInfo(JSONObject params){
        UpdateInfo updateInfo = new UpdateInfo();
        String appId = params.getString("appId");
        String versionCode = params.getString("versionCode");
        String pkg = params.getString("pkg");
        NewestFile newestFile = updateService.getNewestFile(appId,pkg);
        if (newestFile!=null && Integer.parseInt(versionCode) < newestFile.getVersionCode()) {
            FileRecord fileRecord = updateService.getFileRecords(appId, newestFile.getVersionCode(),newestFile.getPkg());
            updateInfo.setUpdate(true);
            updateInfo.setApkUrl(fileRecord.getPath());
            updateInfo.setVersionCode(fileRecord.getVersionCode());
            updateInfo.setPkg(fileRecord.getPkg());
            updateInfo.setVersion(fileRecord.getVersionName());
            updateInfo.setSize(fileRecord.getSize());
            updateInfo.setMd5(fileRecord.getMd5());
            updateInfo.setUpdateLog(fileRecord.getUpdateLog());
            updateInfo.setForce(fileRecord.getForceUpdate() == 1);
        } else {
            updateInfo.setUpdate(false);
        }
        updateInfo.setResultCode("000");
        return updateInfo;
    }
}
