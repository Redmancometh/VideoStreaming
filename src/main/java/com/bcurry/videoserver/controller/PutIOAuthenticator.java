package com.bcurry.videoserver.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bcurry.videoserver.config.ConfigManager;
import com.bcurry.videoserver.config.pojo.ServerConfig;

@Controller
/**
 * TODO: Unified interface for all authenticators.
 * 
 * @author Redmancometh
 *
 */
public class PutIOAuthenticator {
	@Autowired
	ConfigManager<ServerConfig> serverCfg;

	private HttpClient client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();

	public void postAcctInfo() {
		try {
			System.out.println("POSTing acct info");
			ServerConfig cfg = serverCfg.getConfig();
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/oauth2/authenticate");
			builder.setParameter("client_id", cfg.getPutioClientId()).setParameter("response_type", "code")
					.setParameter("redirect_uri", "http://video.mcavenue.net:3000/code");
			URI uri = builder.build();
			List postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("name", cfg.getPutioUser()));
			postParameters.add(new BasicNameValuePair("password", cfg.getPutioPassword()));
			HttpPost post = new HttpPost(uri);
			post.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			HttpResponse resp = client.execute(post);
			System.out.println("PRINTING RESP");
			System.out.println(EntityUtils.toString(resp.getEntity()));
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}

	}

	public void authCallback(String code) {
		try {
			System.out.println("POSTing acct info");
			ServerConfig cfg = serverCfg.getConfig();
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/oauth2/access_token");
			builder.setParameter("code", cfg.getPutioClientId()).setParameter("client_id", cfg.getPutioClientId())
					.setParameter("response_type", code)
					.setParameter("redirect_uri", "http://video.mcavenue.net:3000/code")
					.setParameter("client_secret", cfg.getPutioClientSecret())
					.setParameter("grant_type", "authorization_code");
			URI uri = builder.build();
			List postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("name", cfg.getPutioUser()));
			postParameters.add(new BasicNameValuePair("password", cfg.getPutioPassword()));
			HttpPost post = new HttpPost(uri);
			post.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			HttpResponse resp = client.execute(post);
			for (Header header : resp.getAllHeaders()) {
				System.out.println(header.getName() + " \n\t" + header.getValue());

			}
			System.out.println(EntityUtils.toString(resp.getEntity()));
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void auth() {
		try {

			ServerConfig cfg = serverCfg.getConfig();
			System.out.println(cfg.getPutioUser() + " " + cfg.getPutioPassword());
			URIBuilder builder = new URIBuilder("https://api.put.io/v2/oauth2/authenticate");
			builder.setParameter("client_id", cfg.getPutioClientId()).setParameter("response_type", "code")
					.setParameter("redirect_uri", "http://video.mcavenue.net:3000");
			System.out.println("URL: " + builder.build());
			HttpGet get = new HttpGet(builder.build());
			HttpResponse resp = client.execute(get);
			postAcctInfo();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
