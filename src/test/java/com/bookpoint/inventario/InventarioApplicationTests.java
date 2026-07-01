package com.bookpoint.inventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventarioApplicationTest {

    @Test
    void contextLoads() {
    }
@Test
	void mainEjecutaAplicacion() {
		InventarioApplication.main(new String[] {});
	}

}