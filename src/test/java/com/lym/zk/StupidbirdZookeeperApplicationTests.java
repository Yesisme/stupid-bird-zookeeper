package com.lym.zk;

import com.lym.zk.service.PigService;
import com.lym.zk.service.TiggerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StupidbirdZookeeperApplicationTests {

	@Test
	public void contextLoads() {
		AnnotationConfigApplicationContext cxt = new AnnotationConfigApplicationContext(StupidbirdZookeeperApplication.class);
		PigService pig = cxt.getBean(PigService.class);
		TiggerService tig = cxt.getBean(TiggerService.class);
	}

}
