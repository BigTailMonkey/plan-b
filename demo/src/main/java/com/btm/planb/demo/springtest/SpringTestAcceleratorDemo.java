package com.btm.planb.demo.springtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.btm.planb.demo.springtest","com.btm.planb.springtest.accelerator"})
public class SpringTestAcceleratorDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestAcceleratorDemo.class, args);
    }
}
