package com.example.dongmunseodap.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PDFTextExtractor {

	//추출한 내용을 담는 클래스
	@Getter
	@Setter
	public static class ExtractedContent {
		private String text = "";
		private List<BufferedImage> images = new ArrayList<>();

		public boolean hasText() { return text != null && !text.trim().isEmpty(); }
		public boolean hasImages() { return images != null && !images.isEmpty(); }

		public int getTextLength() { return text != null ? text.length() : 0; }
		public int getImageCount() { return images != null ? images.size() : 0; }

		@Override
		public String toString() {
			return String.format("ExtractedContent{textLength=%d, imageCount=%d}",
				getTextLength(), getImageCount());
		}
	}

	public ExtractedContent extractedContent(File pdfFile) {
		ExtractedContent extractedContent = new ExtractedContent();

		try(PDDocument document = Loader.loadPDF(pdfFile)) {
			log.info("pdf load success : {} pages", document.getNumberOfPages());

			//텍스트 추출
			String extractedText=extractText(document);
			extractedContent.setText(extractedText);

			//TODO: 이미지 추출
			log.info("extracted text length: {}", extractedText.length());

		} catch (IOException e) {
			log.error("PDFTextExtractor.extractedContent() - error : {}", e.getMessage());
		}
		return extractedContent;
	}

	//PDF에서 텍스트 추출하는 메소드
	private String extractText(PDDocument document) throws IOException {
		PDFTextStripper textStripper=new PDFTextStripper();

		//추출 옵션 설정
		textStripper.setSortByPosition(true); //위치에 따라 정렬
		textStripper.setLineSeparator("\n"); //줄바꿈 문자 설정

		String fullText=textStripper.getText(document);

		//텍스트 정제
		String cleanedText = cleanExtractedText(fullText);

		log.debug("original text length: {}, cleaned text length: {}", fullText.length(), cleanedText.length());

		return cleanedText;
	}

	private String cleanExtractedText(String rawText) {
		if(rawText==null || rawText.trim().isEmpty()) {
			return "";
		}

		return rawText
			.replaceAll("[ \\t]+", " ") // 연속된 공백, 탭을 하나의 공백으로
			.replaceAll("\\n{3,}", "\n\n")// 줄바꿈 최대 2개
			.trim();
	}
}
