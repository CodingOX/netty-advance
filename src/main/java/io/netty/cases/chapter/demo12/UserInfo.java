package io.netty.cases.chapter.demo12;

/**
 * Created by 李林峰 on 2018/8/27.
 */
public class UserInfo implements Cloneable{

    private int age;

    private Address address;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", address=" + address +
                '}';
    }
}
