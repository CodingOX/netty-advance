package io.netty.cases.chapter.demo12;

/**
 * Created by 李林峰 on 2018/8/27.
 */
public class CloneTest {

    public static void main(String [] args) throws Exception
    {
//        testClone();
        testCloneV2();
    }

    static void testClone() throws Exception
    {
        UserInfo user = new UserInfo();
        user.setAge(10);
        Address address = new Address();
        address.setCity("BeiJing");
        user.setAddress(address);
        System.out.println("The old value is : " + user);
        UserInfo cloneUser = (UserInfo)user.clone();
        cloneUser.setAge(20);
        cloneUser.getAddress().setCity("NanJing");
        System.out.println("The new value is : " + user);
    }

    static void testCloneV2() throws Exception
    {
        UserInfoV2 user = new UserInfoV2();
        user.setAge(10);
        Address address = new Address();
        address.setCity("BeiJing");
        user.setAddress(address);
        System.out.println("The old value is : " + user);
        UserInfoV2 cloneUser = (UserInfoV2)user.clone();
        cloneUser.setAge(20);
        cloneUser.getAddress().setCity("NanJing");
        System.out.println("The new value is : " + user);
    }
}
