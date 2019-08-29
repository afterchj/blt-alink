package com.tpadsz.after.util.factory;

import com.tpadsz.after.entity.PlaceSave;
import org.springframework.stereotype.Component;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-26 14:38
 **/
@Component
public class PlaceBeanUtils {

    public PlaceSave setPlaceSave(String uid,String meshId,String pname,Integer placeId){
        PlaceSave placeSave = new PlaceSave();
        placeSave.setUid(uid);
        placeSave.setMeshId(meshId);
        placeSave.setPlaceId(placeId);
        placeSave.setPname(pname);
        return placeSave;
    }

}
