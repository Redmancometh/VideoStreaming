package com.bcurry.videoserver.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.bcurry.videoserver.config.ConfigManager;
import com.bcurry.videoserver.config.pojo.ServerConfig;
import com.bcurry.videoserver.pojo.PutIODownloadRequest;
import com.bcurry.videoserver.pojo.PutIOSearchResponse;
import com.google.gson.Gson;

@RestController
@CrossOrigin
public class PutIOController {
	private Gson gson = new Gson();
	@Autowired
	ConfigManager<ServerConfig> serverCfg;
	@Autowired
	private PutIOAuthenticator auth;
	private HttpClient client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();

	@GetMapping("authCode")
	public void recentlyViewedInLibrary(@RequestParam(value = "code") String code) {
		System.out.println("AUTHING CODE: " + code);
		auth.authCallback(code);
	}

	@PostConstruct
	public void start() {
		// auth.auth();
		System.out.println("Done authorizing now listing files");
		// listFiles();
		searchFiles("Tony");
	}

	/**
	 * 
	 * @param libraryName
	 * @param fileName
	 * @return
	 * @return
	 */
	@GetMapping(value = "/putio/getVideo", produces = "video/mp4")
	public StreamingResponseBody getVideo(@RequestParam(value = "id") String id) {
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream outputStream) throws IOException {
				try (InputStream inStream = grabVideo(id)) {
					IOUtils.copy(inStream, outputStream);
				}
			}

		};
	}

	/**
	 * 
	 * @param libraryName
	 * @param fileName
	 * @return
	 */
	@GetMapping(value = "/putio/getVideos")
	private void getVideos(String id) {
		try {
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/files/list");
			builder.addParameter("oauth_token", serverCfg.getConfig().getPutioOauth());
			HttpGet get = new HttpGet(builder.build());
			HttpResponse resp = client.execute(get);
			String responseString = EntityUtils.toString(resp.getEntity());
			for (Header header : resp.getAllHeaders()) {
				System.out.println(header.toString());
			}
			System.out.println(responseString);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param libraryName
	 * @param fileName
	 * @return
	 */
	private InputStream grabVideo(String id) {
		try {
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/files/" + id + "/download");
			builder.addParameter("oauth_token", serverCfg.getConfig().getPutioOauth());
			URL url = new URL(builder.build().toString());
			System.out.println("URL: " + builder.build().toString());
			URLConnection conn = url.openConnection();
			Map<String, List<String>> map = conn.getHeaderFields();
			map.forEach((item, item2) -> System.out.println(item + "\n\t" + item2));
			return conn.getInputStream();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("putio/searchPutio")
	public PutIOSearchResponse searchFiles(@RequestParam(value = "query") String query) {
		try {
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/files/search");
			builder.addParameter("oauth_token", serverCfg.getConfig().getPutioOauth()).addParameter("query", query);
			HttpGet get = new HttpGet(builder.build());
			HttpResponse resp = client.execute(get);
			String responseString = EntityUtils.toString(resp.getEntity());
			PutIOSearchResponse json = gson.fromJson(responseString, PutIOSearchResponse.class);
			return json;
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
