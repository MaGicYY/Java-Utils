package com.zxy.utils;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaoxinyang
 * @date 2022/7/13
 */
@Slf4j
public class HttpUtil {

    public static <T> T get(RestTemplate restTemplate, String url, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, Class<T> responseType) {
        return request(restTemplate, url, headers, params, null, responseType, HttpMethod.GET);
    }


    public static <T> T post(RestTemplate restTemplate, String url, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, Class<T> responseType) {
        return request(restTemplate, url, headers, params, null, responseType, HttpMethod.POST);
    }

    public static <T> T postJson(RestTemplate restTemplate, String url, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, Object body, Class<T> responseType) {
        if (Objects.isNull(headers)) {
            headers = new LinkedMultiValueMap<>();
        }

        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }
        return request(restTemplate, url, headers, params, body, responseType, HttpMethod.POST);
    }

    public static <T> T request(RestTemplate restTemplate, String url, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, Object body, Class<T> responseType, HttpMethod method) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).
                build().encode().toUri();
        HttpEntity<?> request = new HttpEntity<>(body, headers);

        log.info("request begin, headers:[{}], params:[{}], body:[{}]", headers, params, body);
        long startMillis = System.currentTimeMillis();
        ResponseEntity<T> responseEntity = restTemplate.exchange(uri, method, request, responseType);
        log.info("request finish, cost:[{}]", System.currentTimeMillis() - startMillis);

        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            log.error("request failed, response:[{}]", responseEntity);
            throw new IllegalStateException("request remote source failed");
        }

        T resBody = responseEntity.getBody();
        log.info("response:[{}]", resBody);
        return resBody;
    }


}
