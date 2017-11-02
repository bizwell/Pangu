package com.joindata.inf.boot.sterotype;


import org.springframework.http.*;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;


/**
 * 增强ResponseEntity的build，因为ResponseEntity必须确定statusCode才返回BodyBuilder。
 *
 * @author wangliqiu
 */
public class ResponseEntityX {


	public static BodyBuilder of() {
		return new DefaultBuilder();
	}


	/**
	 * 替换ResponseEntity.BodyBuilder
	 */
	public interface BodyBuilder extends ResponseEntity.HeadersBuilder<BodyBuilder> {

		BodyBuilder status(HttpStatus status);

		BodyBuilder contentLength(long contentLength);

		BodyBuilder contentType(MediaType contentType);

		<T> ResponseEntity<T> body(T body);

		BodyBuilder ok();

	}


	/**
	 * 改造ResponseEntity.DefaultBuilder
	 */
	public static class DefaultBuilder implements BodyBuilder {

		private HttpStatus statusCode;

		private final HttpHeaders headers = new HttpHeaders();


		public BodyBuilder status(HttpStatus status) {
			statusCode = status;
			return this;
		}

		public BodyBuilder header(String headerName, String... headerValues) {
			for (String headerValue : headerValues) {
				this.headers.add(headerName, headerValue);
			}
			return this;
		}

		public BodyBuilder headers(HttpHeaders headers) {
			if (headers != null) {
				this.headers.putAll(headers);
			}
			return this;
		}

		public BodyBuilder allow(HttpMethod... allowedMethods) {
			this.headers.setAllow(new LinkedHashSet<>(Arrays.asList(allowedMethods)));
			return this;
		}

		public BodyBuilder contentLength(long contentLength) {
			this.headers.setContentLength(contentLength);
			return this;
		}

		public BodyBuilder contentType(MediaType contentType) {
			this.headers.setContentType(contentType);
			return this;
		}

		public BodyBuilder eTag(String eTag) {
			if (eTag != null) {
				if (!eTag.startsWith("\"") && !eTag.startsWith("W/\"")) {
					eTag = "\"" + eTag;
				}
				if (!eTag.endsWith("\"")) {
					eTag = eTag + "\"";
				}
			}
			this.headers.setETag(eTag);
			return this;
		}

		public BodyBuilder lastModified(long date) {
			this.headers.setLastModified(date);
			return this;
		}

		public BodyBuilder location(URI location) {
			this.headers.setLocation(location);
			return this;
		}

		public BodyBuilder cacheControl(CacheControl cacheControl) {
			String ccValue = cacheControl.getHeaderValue();
			if (ccValue != null) {
				this.headers.setCacheControl(cacheControl.getHeaderValue());
			}
			return this;
		}

		public BodyBuilder varyBy(String... requestHeaders) {
			this.headers.setVary(Arrays.asList(requestHeaders));
			return this;
		}


		public BodyBuilder ok() {
			status(HttpStatus.OK);
			return this;
		}

		public ResponseEntity<Void> build() {
			return body(null);
		}


		public <T> ResponseEntity<T> body(T body) {
			return new ResponseEntity<>(body, this.headers, this.statusCode);
		}

	}


}
