package com.wangboot.core.test;

import java.net.URI;
import lombok.Generated;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

/**
 * MockMVC 测试客户端
 *
 * @author wwtg99
 */
@Generated
public class TestClient {

  @Getter private final MockMvc mockMvc;

  public TestClient(MockMvc mvc) {
    this.mockMvc = mvc;
  }

  public ResultActions get(String url) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions get(URI uri) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions get(String url, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions get(URI uri, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions post(String url, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions post(URI uri, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions post(String url, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions post(URI uri, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions put(String url, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions put(URI uri, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions put(String url, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions put(URI uri, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions patch(String url, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions patch(URI uri, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions patch(String url, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.patch(url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions patch(URI uri, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.patch(uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(String url, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(String url) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(URI uri, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(URI uri) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(String url, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions delete(URI uri, String json, HttpHeaders headers) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.delete(uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions multipart(String url, MockMultipartFile file) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.multipart(url).file(file).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions multipart(URI uri, MockMultipartFile file) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.multipart(uri).file(file).accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions multipart(String url, MockMultipartFile file, HttpHeaders headers)
      throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.multipart(url)
                .file(file)
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions multipart(URI uri, MockMultipartFile file, HttpHeaders headers)
      throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.multipart(uri)
                .file(file)
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, String url, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, String url) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, URI uri) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, URI uri, String json) throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, String url, String json, HttpHeaders headers)
      throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, url)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }

  public ResultActions request(HttpMethod method, URI uri, String json, HttpHeaders headers)
      throws Exception {
    return this.mockMvc
        .perform(
            MockMvcRequestBuilders.request(method, uri)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andDo(MockMvcResultHandlers.print());
  }
}
