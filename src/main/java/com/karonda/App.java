package com.karonda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@EnableAutoConfiguration
@RestController
public class App 
{
    @RequestMapping("/")
    public String home(){
        return "Hello World!";
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        SpringApplication.run(App.class, args);
    }
}
