package controller;

import com.tpadsz.after.entity.Group;
import com.tpadsz.after.service.GroupOperationService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-06 16:24
 **/
public class AlinkAdjustModuleControllerTest {

    static ApplicationContext ac = null;
    static {
        ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    GroupOperationService groupOperationService = ac.getBean("groupOperationService",GroupOperationService.class);

    @Test
    public void getMeshSerialNoTest(){
        Integer mid = groupOperationService.getMeshSerialNo
                ("12345678");
        System.out.println("mid:"+mid);
    }

    @Test
    public void saveGroupTest(){
        Group group = new Group();
        group.setGname("组4");
        group.setMid(1);
        groupOperationService.saveGroup(group);
        System.out.println("id:"+group.getId());
        String groupId = groupOperationService.getGroupIdById(group.getId());
        System.out.println("groupId:"+groupId);
    }

    @Test
    public void saveGroupLogTest(){
        groupOperationService.saveGroupLog("aaaa","0","1");
        System.out.println("success");
    }

    @Test
    public void updateGroupNameByGroupIdTest(){
        groupOperationService.updateGroupNameByGroupId("180002b13ff011e98908fa163eaa8896","白炽灯组");
        System.out.println("success");
    }

    @Test
    public void test(){
        List<Map<String,Object>> groupLists = groupOperationService.getGroupsByMeshId("12345678");
        for (Map<String,Object> gruopList:groupLists){
            for (Map.Entry<String,Object>  entry:gruopList.entrySet()) {
                if ("group_id".equals(entry.getKey())){
                    System.out.print("group_id=="+entry.getValue());
                }
                if ("id".equals(entry.getKey())){
                    System.out.print("id=="+entry.getValue());
                }
            }
            System.out.println(" ");
        }
    }
}
