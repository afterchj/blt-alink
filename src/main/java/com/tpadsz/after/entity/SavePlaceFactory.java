package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-08-13 14:12
 **/
public class SavePlaceFactory {

    public static PlaceSave savePlace(String uid,String meshId,String pname,Integer placeId){
        PlaceSave placeSave = new PlaceSave();
        placeSave.setMeshId(meshId);
        placeSave.setPlaceId(placeId);
        placeSave.setUid(uid);
        placeSave.setPname(pname);
        return placeSave;
    }
}
