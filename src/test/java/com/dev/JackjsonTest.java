package com.dev;


import com.dev.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * HttpClient请求数据后是json字符串，需要我们自己把Json字符串反序列化为对象，我们会使用JacksonJson工具来实现。
 * JacksonJson是SpringMVC内置的json处理工具，其中有一个`ObjectMapper`类，可以方便的实现对json的处理：
 */
public class JackjsonTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    /*
    对象转json
     */
    @Test
    public void objToJson() throws JsonProcessingException {
        User user = new User();
        user.setUserName("zhangsan");
        user.setAge(18);
        String json = objectMapper.writeValueAsString(user);
        System.out.println("json = " + json);
    }

    /*
    json转普通对象
     */
    @Test
    public void jsonToPojo() throws IOException {
        // 序列化后的结果
        String json = "{\"userName\":\"zhangsan\",\"age\":18}";
        // 反序列化，接收两个参数：json数据，反序列化的目标类字节码
        User user = objectMapper.readValue(json, User.class);
        System.out.println("user.name = " + user.getUserName());
    }

    /*
    json和集合的转换
    json转集合比较麻烦，因为你无法同时把集合的class和元素的class同时传递到一个参数。
    因此Jackson做了一个类型工厂，用来解决这个问题
     */
    @Test
    public void jsonToCollection() throws IOException {
        User user = new User();
        user.setUserName("zhansan");
        user.setAge(16);
        //对象集合
        List<User> listUser = Arrays.asList(user, user);
        //集合转换json
        String json = objectMapper.writeValueAsString(listUser);
        System.out.println("json = " + json);
        //json转集合（较麻烦）
        List<User> list = objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        list.forEach(System.out::println);
    }

    /*
    json转任意复杂类型 和 任意类型转json
    当对象泛型关系复杂时，类型工厂也不好使了。这个时候Jackson提供了TypeReference来接收类型泛型，
    然后底层通过反射来获取泛型上的具体类型。实现数据转换。
     */
    @Test
    public void jsonToComplexObj() throws IOException {
        User user = new User();
        user.setUserName("zhansan");
        user.setAge(16);
        //对象集合
        List<User> listUser = Arrays.asList(user, user);
        //集合转换json
        String json = objectMapper.writeValueAsString(listUser);
        //json转换成任意对象！！！
        List<User> list = objectMapper.readValue(json, new TypeReference<List<User>>() {
        });
        list.forEach(System.out::println);
    }
}
