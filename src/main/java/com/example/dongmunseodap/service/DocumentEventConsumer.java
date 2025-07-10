package com.example.dongmunseodap.service;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.dongmunseodap.DocumentUploadedEvent;
import com.example.dongmunseodap.global.S3Downloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

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

		String traceId = event.getPayload() != null ? event.getTraceId() : null;
		String s3Url = event.getPayload().getS3Path();

		try (ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Downloader.downloadAsStream(s3Url)) {
			log.info("Successfully opened stream from S3: {}", s3Url);

			// 2. 텍스트 추출
			PDFTextExtractor.ExtractedContent content = pdfTextExtractor.extractedContent(s3ObjectStream);
			log.info("Extracted content: {}", content);

			if (!content.hasText()) {
				log.warn("No text extracted from the PDF.");
				return;
			}

			// 3. 텍스트 분할
			List<String> chunkedText = textChunkingService.chunkText(content);
			log.info("Chunked text: {}", chunkedText);

			if (chunkedText.isEmpty()) {
				log.warn("No text chunks created from the extracted content.");
				return;
			}

			// 4. 벡터 DB 저장
			vectorStoreService.saveChunks(traceId, chunkedText);
			log.info("Successfully saved document chunks to vector store for traceId: {}", traceId);

		} catch (Exception e) {
			log.error("Failed to process document from S3: {}", e.getMessage(), e);
		}
	}
}
