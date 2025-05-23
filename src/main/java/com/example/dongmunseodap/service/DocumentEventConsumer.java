package com.example.dongmunseodap.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.dongmunseodap.DocumentUploadedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentEventConsumer {

	@KafkaListener(topics = "document-uploaded", groupId = "document-processing-consumer")
	public void consume(DocumentUploadedEvent event) {
		log.info(">>> Received AVRO event: {}", event);
		//TODO: 메세지 처리 로직 구현
		/*
			1. s3에서 pdf 다운
			2. 페이지 단위? 특정 단위로 텍스트 분할
			3. 각 단위에 대해 텍스트 -> 벡터 embedding
			4. 벡터 DB(elastic search ?) 저장
		 */

	}

}
