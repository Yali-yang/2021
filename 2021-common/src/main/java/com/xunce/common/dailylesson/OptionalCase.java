package com.xunce.common.dailylesson;

import com.google.common.base.Strings;
import lombok.Data;
import java.util.Optional;

public class OptionalCase {
    public static void main(String[] args) {
        User user = getUser();
        // 获取User下Address下Country下的cityName
        // 问题：可能会NPE，所以需要判空，省去了多重if的判断
        Optional<String> optional = Optional.ofNullable(user)
                .map(User::getAddress)
                .map(Address::getCountry)
                .map(Country::getCityName);

        // 如果不判断是否为空，直接去get，为空的话会报NoSuchElementException
//        String s1 = optional.get();

        // 如果为空，那么获取的就是上海的值
        String 上海 = optional.orElse("上海");

        System.out.println();
        // 如果存在就打印
        optional.ifPresent(t -> System.out.println(Strings.lenientFormat("城市地址：%s", t)));

        // 不存在，就抛出异常，存在就获取值
        String s = optional.orElseThrow(() -> new RuntimeException());

    }

    @Data
    static class User {
        private String name;
        private Integer age;
        private String sex;
        private Address address;

    }

    @Data
    static class Address {
        private String addName;
        private Country country;
    }

    @Data
    static class Country {
        private String cityName;
        private String provinceName;
    }

    static User getUser(){
        Country country = new Country();
        country.setCityName("成都");
        country.setProvinceName("四川");

        Address address = new Address();
        address.setAddName("武侯区");
        address.setCountry(country);

        User user = new User();
        user.setName("向向");
        user.setAge(27);
        user.setSex("男");
        user.setAddress(address);

        return user;
    }

}
