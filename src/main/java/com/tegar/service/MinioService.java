package com.tegar.service;

import java.io.InputStream;

public interface MinioService {

	String uploadImg(String source, InputStream file, String contentType) throws Exception;
	
}
