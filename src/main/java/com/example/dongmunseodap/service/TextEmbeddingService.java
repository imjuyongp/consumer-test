package com.example.dongmunseodap.service;

import java.util.List;

public interface TextEmbeddingService {
	List<float[]> embedChunks(List<String> chunks);
}
