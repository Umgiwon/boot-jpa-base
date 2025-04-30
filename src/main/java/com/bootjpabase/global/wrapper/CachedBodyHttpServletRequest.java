package com.bootjpabase.global.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * * HttpServletRequest 안의 requestBody 내용을 캐싱하기 위한 클래스
 * * request body는 inputStream에 의해 기본적으로 한 번만 읽을 수 있기 때문에
 * * 해당 클래스를 통해 요청 내용을 메모리에 저장하고 재사용 가능하게 한다.
 */
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    // request body 데이터 담을 배열 초기화
    private final byte[] cachedBody;

    /**
     * 요청 inputStream 데이터를 읽어서 캐싱
     * @param request
     * @throws IOException
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);

        // inputStream 데이터를 바이트로 읽는다
        cachedBody = request.getInputStream().readAllBytes();
    }

    /**
     * 캐싱된 Body 데이터를 반환한다.
     * @return 캐싱된 body 데이터의 inputStream
     */
    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(cachedBody);
    }

    /**
     * 캐싱된 body 데이터를 반환한다.
     * @return 캐싱된 body 데이터의 BufferedReader
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * inner class
     * 캐싱된 body 데이터를 inputSream으로 제공하는 이너 클래스
     */
    private static class CachedBodyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream byteArrayInputStream;

        /**
         * 바이트 배열 기반으로 byteArrayInputStream 생성
         * @param cachedBody (캐싱된 body 데이터)
         */
        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0; // 읽을 데이터가 없으면 true
        }

        @Override
        public boolean isReady() {
            return true; // 언제든 읽을 수 있는 준비 완료 상태
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // empty
        }

        @Override
        public int read() {
            return byteArrayInputStream.read(); // 바이트 데이터를 읽는다.
        }
    }
}
