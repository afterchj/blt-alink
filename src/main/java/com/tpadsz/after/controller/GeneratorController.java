package com.tpadsz.after.controller;

import com.tpadsz.after.service.AlinkLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.alibaba.fastjson.JSONObject;
import org.springframework.ui.ModelMap;
/**
 * Created by chenhao.lu on 2019/3/22.
 */

@Controller
@RequestMapping("/generator")
public class GeneratorController {
    @Resource
    private AlinkLoginService alinkLoginService;


        @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public String generator(@ModelAttribute("decodedParams") JSONObject params, ModelMap model) {
        try {
            int[] meshIds = generateAppCode2Disk();
            List<String> list = new ArrayList<>();
            for (int i = 1; i < 10001; i++) {
                list.add(String.valueOf(meshIds[i]));
//                alinkLoginService.insert(String.valueOf(meshIds[i]));
            }
            alinkLoginService.insertForeach(list);
        } catch (Exception e) {
        }
        model.put("result", "123");
        model.put("result_message", "成功");
        return null;
    }


    private static int[] generateAppCode2Disk() {
        int begin = 1;
        int end = 99999999;
        int count = begin + end;
        //生成1到99999999的所有整数
        int[] codes = new int[count + 1];
        for (int i = begin; i <= end; i++) {
            codes[i] = i;
        }
        //随机交换数据
        int index = 0;
        int tempCode = 0;
        Random random = new Random();
        for (int i = begin; i <= end; i++) {
            index = random.nextInt(count + 1);
            tempCode = codes[index];
            codes[index] = codes[i];
            codes[i] = tempCode;
        }
        //生成1000个文件,每个文件包含100000个appCode
        //生成1000个文件,每个文件包含100000个appCode
        StringBuilder sb = new StringBuilder();
        int flag = 100000;
//        System.out.println("***********开始**********");
//        try {
//            for(int i = 5000001; i <= end; i++){
//                sb.append(codes[i]).append("\n");
//                if(i == end || i%flag == 0){
//                    File folder = new File("C:/001work/meshId");
//                    if(!folder.isDirectory()){
//                        folder.mkdir();
//                    }
//                    if(i==end){
//                        i = end +1;
//                    }
//                    File file = new File("C:/001work/meshId/ID_"+(i/flag)+".txt");
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    BufferedWriter bw=new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
//                    bw.write(sb.toString());
//                    bw.flush();
//                    bw.close();
//                    sb = new StringBuilder();
////                    System.out.println("当前i值："+i+"第"+(i/flag)+"个文件生成成功！");
//                }
//            }
////            System.out.println("***********结束**********");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return codes;
    }


}