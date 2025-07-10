package com.example.dongmunseodap.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextChunkingServiceImpl implements TextChunkingService {

	@Override
	public List<String> chunkText(PDFTextExtractor.ExtractedContent content) {
		String fullText = content.getText();
		if (fullText == null || fullText.isBlank()) return List.of();

		//REFACTOR: 추후 의미를 담을 수 있는 청크 단위로 분할
		return Arrays.stream(fullText.split("\\n\\n"))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList();

	}
}
