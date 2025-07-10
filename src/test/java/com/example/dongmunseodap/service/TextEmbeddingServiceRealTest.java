package com.example.dongmunseodap.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TextEmbeddingServiceRealTest {

	@Autowired
	TextEmbeddingService textEmbeddingService;

	@Test
	void 입베딩_테스트_실제호출(){
		List<float[]> result=textEmbeddingService.embedChunks(List.of("Hello, world!"));
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1536, result.get(0).length, "임베딩 벡터의 길이는 1536이어야 합니다.");
	}
}