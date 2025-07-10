package com.example.dongmunseodap.service;

import java.util.List;

import org.springframework.ai.document.Document;

public interface VectorStoreService {
	void saveChunks(String traceId, List<String> chunks);

	List<Document> search(String text);
}
