package com.example.dongmunseodap.global;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Component
public class S3Downloader {


	private static final String DOWNLOAD_DIR = System.getProperty("user.dir") + File.separator + "downloads";
	private final S3Client s3Client;

	public S3Downloader(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public ResponseInputStream<GetObjectResponse> downloadAsStream(String s3Url) {
		URI uri = URI.create(s3Url);
		String bucket = uri.getHost();
		String key = uri.getPath().substring(1);

		return s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build()
		);
	}

	@Deprecated
	public File download(String s3Url, String outputFileName) {
		File dir = new File(DOWNLOAD_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String outputFilePath = DOWNLOAD_DIR + File.separator + outputFileName;

		URI uri = URI.create(s3Url);
		String bucket = uri.getHost();
		String key = uri.getPath().substring(1);

		File output = new File(outputFilePath);
		s3Client.getObject(
			GetObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build(),
			ResponseTransformer.toFile(output)
		);

		return output;
	}
}
