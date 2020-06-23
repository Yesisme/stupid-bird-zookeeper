package com.lym.zk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = "com.lym.zk.mapper")
public class StupidbirdZookeeperApplication {

	public static void main(String[] args) {

		SpringApplication.run(StupidbirdZookeeperApplication.class, args);
	}
}
