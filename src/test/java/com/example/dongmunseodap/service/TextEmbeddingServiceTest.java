package com.example.dongmunseodap.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.embedding.EmbeddingModel;

class TextEmbeddingServiceTest {

	@Test
	void 임베딩_테스트_호출X(){
		EmbeddingModel mockModel = Mockito.mock(EmbeddingModel.class);
		TextEmbeddingService service = new TextEmbeddingServiceImpl(mockModel);


		List<String > chunks = List.of(
			"첫번째 청크",
			"두번째 청크",
			"세번째 청크"
		);
		List<float[]> mockEmbeddings = List.of(
			new float[]{0.1f, 0.2f, 0.3f},
			new float[]{0.4f, 0.5f, 0.6f},
			new float[]{0.7f, 0.8f, 0.9f}
		);

		Mockito.when(mockModel.embed(chunks)).thenReturn(mockEmbeddings);

		List<float[]> result = service.embedChunks(chunks);

		Mockito.verify(mockModel).embed(chunks);
		Assertions.assertEquals(3, result.size());
		Assertions.assertArrayEquals(new float[]{0.1f, 0.2f, 0.3f}, result.get(0));
		Assertions.assertArrayEquals(new float[]{0.4f, 0.5f, 0.6f}, result.get(1));
		Assertions.assertArrayEquals(new float[]{0.7f, 0.8f, 0.9f}, result.get(2));
	}
}