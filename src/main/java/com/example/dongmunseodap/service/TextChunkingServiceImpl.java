package com.example.dongmunseodap.service;

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

		List<String> chunks = new java.util.ArrayList<>();
		final int maxChars = 2000; // Conservative cap for token limits
		final int overlapChars = 300;

		int start = 0;
		while (start < fullText.length()) {
		    int end = Math.min(start + maxChars, fullText.length());
		    String chunk = fullText.substring(start, end);
		    chunks.add(chunk);
		    if (end == fullText.length()) break;
		    start += (maxChars - overlapChars);
		}
		return chunks;
	}
}
