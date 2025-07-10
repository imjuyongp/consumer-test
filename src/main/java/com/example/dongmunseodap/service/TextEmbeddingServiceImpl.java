package com.example.dongmunseodap.service;

import java.util.List;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TextEmbeddingServiceImpl implements TextEmbeddingService{
	private final EmbeddingModel embeddingModel;

	@Override
	public List<float[]> embedChunks(List<String> chunks){
		if(chunks == null || chunks.isEmpty()) return List.of();
		return embeddingModel.embed(chunks);
	}
}
