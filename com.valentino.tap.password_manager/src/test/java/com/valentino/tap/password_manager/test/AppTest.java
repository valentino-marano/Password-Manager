package com.valentino.tap.password_manager.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.valentino.tap.password_manager.app.App;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private App app;
	@Before
	public void setup() {
		app = new App();
	}

	@Test
	public void testSayHello() {
		assertEquals("Hello", app.sayHello());
	}
}