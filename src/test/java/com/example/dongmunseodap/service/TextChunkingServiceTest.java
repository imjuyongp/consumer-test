package com.example.dongmunseodap.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class TextChunkingServiceTest {
	private final TextChunkingService service=new TextChunkingServiceImpl();

	@Test
	void 빈_줄_기준으로_청킹(){
		PDFTextExtractor.ExtractedContent content=new PDFTextExtractor.ExtractedContent();
		content.setText("첫번째 문장\n\n두번째 문장\n\n세번째 문장");

		List<String> result= service.chunkText(content);
		assertEquals(3, result.size());
		assertEquals("첫번째 문장", result.get(0));
		assertEquals("두번째 문장", result.get(1));
		assertEquals("세번째 문장", result.get(2));
	}
}