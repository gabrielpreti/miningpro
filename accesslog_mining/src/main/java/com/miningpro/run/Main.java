package com.miningpro.run;

import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by gsantiago on 1/31/15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        SpringCamelContext sprintContext = new SpringCamelContext(new ClassPathXmlApplicationContext(
                "applicationContext.xml"));
        sprintContext.start();
        sprintContext.startAllRoutes();

        System.out.println("Started context");

        org.apache.camel.main.Main main = new org.apache.camel.main.Main();
        main.enableHangupSupport();
        main.run();

    }
}
