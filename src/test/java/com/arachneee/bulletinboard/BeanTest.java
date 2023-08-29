package com.arachneee.bulletinboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void test() {
		String[] beans = applicationContext.getBeanDefinitionNames();
		for (String bean : beans) {
			System.out.println(bean);
		}
	}
}
