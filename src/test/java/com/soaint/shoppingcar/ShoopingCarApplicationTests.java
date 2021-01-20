package com.soaint.shoppingcar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.soaint.shoppingcar.models.entity.Producto;

@SpringBootTest
class ShoopingCarApplicationTests {

	@Test
	void contextLoads() {
		Producto r = new Producto();
		r.setName("A001");
		assertEquals(r.getName(), "A001");
	}

}
