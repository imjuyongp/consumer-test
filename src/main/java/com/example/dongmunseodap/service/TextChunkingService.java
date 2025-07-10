package com.example.dongmunseodap.service;

import java.util.List;

public interface TextChunkingService {
	public List<String> chunkText(PDFTextExtractor.ExtractedContent content);
}
