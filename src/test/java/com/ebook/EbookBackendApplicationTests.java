package com.ebook;

import com.ebook.entity.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EbookBackendApplicationTests {

	@Test
	void contextLoads() {
		Brand brand = new Brand();
		brand.setId(1);
		brand.setNameBrand("Giáo Khoa");
	}

}
