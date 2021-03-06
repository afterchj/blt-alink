package com.tpadsz.after.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Data 注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
 @Setter ：注解在属性上；为属性提供 setting 方法
 @Getter ：注解在属性上；为属性提供 getting 方法
 @Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
 @NoArgsConstructor ：注解在类上；为类提供一个无参的构造方法
 @AllArgsConstructor ：注解在类上；为类提供一个全参的构造方法
 @Cleanup : 可以关闭流
 @Builder ： 被注解的类加个构造者模式
 @Synchronized ： 加个同步锁
 @SneakyThrows : 等同于try/catch 捕获异常
 @NonNull : 如果给参数加个这个注解 参数为null会抛出空指针异常
 @Value : 注解和@Data类似，区别在于它会把所有成员变量默认定义为private final修饰，并且不会生成set方法。
 @toString：注解在类上；为类提供toString方法（可以添加排除和依赖）；
 官方文档https://projectlombok.org/features/index.html
 *
 * @program: blt-alink
 * @description: LomBok测试
 * @author: Mr.Ma
 * @create: 2019-05-08 10:19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LombokTest {

    private Integer id;
    private String name;
    private String sex;

//    public static void main(String[] args) {
//        LombokTest lombokTest = new LombokTest();
//        lombokTest.setId(1);
//        lombokTest.setName("校长");
//        lombokTest.setSex("男");
//        System.out.println("id: "+lombokTest.getId());
//        System.out.println("name: "+lombokTest.getName());
//        System.out.println("sex: "+lombokTest.getSex());
//
//        System.out.println(lombokTest);
//        System.out.println("校长".equals(lombokTest.getName()));
//        System.out.println("xiaozhang".equals(lombokTest.getName()));
//
//        LombokTest lombokTest1 = new LombokTest(1,"xiaoxiao1","男");
//
//    }
}
