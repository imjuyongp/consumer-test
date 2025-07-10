package com.example.dongmunseodap.service;

import java.io.File;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.dongmunseodap.DocumentUploadedEvent;
import com.example.dongmunseodap.global.S3Downloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentEventConsumer {

	private final S3Downloader s3Downloader;
	private final PDFTextExtractor pdfTextExtractor;
	private final TextChunkingService textChunkingService;
	private final VectorStoreService vectorStoreService;

	@KafkaListener(topics = "document-uploaded", groupId = "document-processing-consumer")
	public void consume(DocumentUploadedEvent event) {
		log.info(">>> Received AVRO event: {}", event);
		/*
			1. s3에서 pdf 다운
			2. 페이지 단위? 특정 단위로 텍스트 분할
			3. 각 단위에 대해 텍스트 -> 벡터 embedding
			4. 벡터 DB(elastic search ?) 저장
		 */
		String documentId = event.getPayload() != null ? event.getPayload().getDocumentId() : null;

		// 1. s3에서 pdf 다운
		String s3Url = event.getPayload().getS3Path();
		String localPath = event.getPayload().getFileName();
		File downloadedFile = null;
		try {
			downloadedFile=s3Downloader.download(s3Url,localPath);
			log.info("Downloaded PDF from S3: {}", s3Url);
		} catch (Exception e) {
			log.error("Failed to download PDF from S3: {}", e.getMessage());
		}

		if (downloadedFile != null && downloadedFile.exists()) {
			log.info("Downloaded file exists at: {}", downloadedFile.getAbsolutePath());

		}
		else {
			log.error("Downloaded file does not exist or failed to download.");
		}
		//2. 텍스트 추출
		//전체 내용
		PDFTextExtractor.ExtractedContent content=pdfTextExtractor.extractedContent(downloadedFile);
		log.info("Extracted content: {}", content);
		if (!content.hasText()) {
			log.warn("No text extracted from the PDF.");
			return;
		}
		List<String> chunkedText = textChunkingService.chunkText(content);
		log.info("Chunked text: {}", chunkedText);
		if (chunkedText.isEmpty()) {
			log.warn("No text chunks created from the extracted content.");
			return;
		}

		//descripted: 임베딩은 vectorstoreService에서 자동 처리
		// List<float[]> embeddedChunks = textEmbeddingService.embedChunks(chunkedText);
		// log.info("Embedded chunks: {}", embeddedChunks);
		// if (embeddedChunks.isEmpty()) {
		// 	log.warn("No embeddings created from the text chunks.");
		// 	return;
		// }

		//4. 벡터 DB 저장
		vectorStoreService.saveChunks(documentId, chunkedText);
	}

}
