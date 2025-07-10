package com.example.dongmunseodap.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VectorStoreServiceRealTest {

	@Autowired
	private VectorStoreService vectorStoreService;

	@Test
	void 벡터_저장_및_검색_테스트() throws InterruptedException {
		String documentId = "1";
		String originalText = "Hello, world!";
		vectorStoreService.saveChunks(documentId, List.of(originalText));


		Thread.sleep(1500); // 1~2초 딜레이

		List<Document> results = vectorStoreService.search("Hello");

		assertFalse(results.isEmpty());
		assertTrue(results.get(0).getText().contains("Hello"));
	}

}