package com.casino.common.utils;

import com.casino.modules.admin.common.entity.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpUtils {
    public ResponseEntity<String> getUserRefreshToken(String url, String username, String apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ <
             */
            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();
            Member member = new Member();
            member.setUsername(username);
            HttpEntity<Member> request = new HttpEntity<>(member, headers);

            ResponseEntity<String> response = restTemplate.exchange(url + "?_method=PATCH", HttpMethod.POST, request, String.class);
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==getUserRefreshToken==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> createUser(String url, String username, String nickname, String apiKey) {
        System.out.println("HttpUtils==createUser==");
        System.out.println("*************** param info ***************");
        System.out.println("*** url : "+url);
        System.out.println("*** username : "+username);
        System.out.println("*** nickname : "+nickname);
        System.out.println("*** apiKey : "+apiKey);
        System.out.println("******************************************");

        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ <
             */
            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();

            Member member = new Member();
            member.setUsername(username);
            member.setNickname(nickname);
            HttpEntity<Member> request = new HttpEntity<>(member, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("HttpUtils==createUser========== api success");
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==createUser==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> getUserInfo(String url, String apikey){
        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
             * <
             */

            headers.add(HttpHeaders.AUTHORIZATION, apikey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Member> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==getUserInfo==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> userAddBalance(String url, String username, Float amount, String apiKey) {
        System.out.println("HttpUtils==userAddBalance==");
        System.out.println("*************** param info ***************");
        System.out.println("*** url : "+url);
        System.out.println("*** username : "+username);
        System.out.println("*** amount : "+amount);
        System.out.println("*** apiKey : "+apiKey);
        System.out.println("******************************************");

        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ <
             */
            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();

            Member member = new Member();
            member.setUsername(username);
            member.setAmount(amount);
            HttpEntity<Member> request = new HttpEntity<>(member, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("HttpUtils==userSubBalance========== api success");
            System.out.println(response);
            return response;
        } catch (HttpStatusCodeException e) {
            ResponseEntity<String> responseError = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());

            System.out.println("HttpUtils==userAddBalance==========Exception :");
            System.out.println("\tHttp URL:" + url);
            System.out.println("\tError response====>");
            System.out.println(responseError);
            System.out.println("\t************************************************");
            e.printStackTrace();
            return responseError;
        }
    }

    public static ResponseEntity<String> userSubBalance(String url, String username, Float amount, String apiKey) {
        System.out.println("HttpUtils==userSubBalance==");
        System.out.println("*************** param info ***************");
        System.out.println("*** url : "+url);
        System.out.println("*** username : "+username);
        System.out.println("*** amount : "+amount);
        System.out.println("*** apiKey : "+apiKey);
        System.out.println("******************************************");

        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ <
             */
            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();

            Member member = new Member();
            member.setUsername(username);
            member.setAmount(amount);
            HttpEntity<Member> request = new HttpEntity<>(member, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            System.out.println("HttpUtils==userSubBalance========== api success");
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==userSubBalance==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> getTransactionListSimple(String url, String apiKey) {
        System.out.println("HttpUtils==getTransactionListSimple==");
        System.out.println("*************** param info ***************");
        System.out.println("*** url : "+url);
        System.out.println("*** apiKey : "+apiKey);
        System.out.println("******************************************");

        try {
            HttpHeaders headers = new HttpHeaders();

            /*
             * set header content
             * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
             * <
             */

            headers.add(HttpHeaders.AUTHORIZATION, apiKey);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ >

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Member> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            System.out.println("HttpUtils==getTransactionListSimple========== api success");
            System.out.println(response);
            return response;
        } catch (Exception e) {
            System.out.println("HttpUtils==getTransactionListSimple==========Exception :");
            e.printStackTrace();
            return new ResponseEntity<>("Error!, Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
