package io.netty.cases.chapter.demo12;

/**
 * Created by 李林峰 on 2018/8/27.
 */
public class Address implements Cloneable{

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                '}';
    }
}
