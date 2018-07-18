package com.nicholaslocicero.focus.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableEntityLinks;

@EnableEntityLinks
@SpringBootApplication
public class AttendanceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AttendanceApplication.class, args);
  }
}
