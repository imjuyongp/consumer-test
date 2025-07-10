package com.example.dongmunseodap.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PineConeVectorStoreService implements VectorStoreService {
	private final VectorStore vectorStore;
	@Override
	public void saveChunks(String traceId, List<String> chunks){
		List<Document> documents= IntStream.range(0, chunks.size())
			.mapToObj(i -> {
				Map<String, Object> metadata = new HashMap<>();
				metadata.put("trace_id", traceId);
				metadata.put("chunk_index", i);
				return new Document(chunks.get(i), metadata);
			})
			.toList();
		vectorStore.add(documents);
	}

	@Override
	public List<Document> search(String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		List<Document> results = vectorStore.similaritySearch(text);
		return results.stream()
				.filter(doc -> doc.getText() != null && !doc.getText().isBlank())
				.toList();
	}
}
