package com.bcurry.videoserver.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

/**
 * Use this if you have a standard adapter conflicting like PathAdapter
 * 
 * @author Redmancometh
 *
 * @param <T>
 */
public class AdapterlessConfigManager<T> {

	@Getter
	private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED)
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES).setPrettyPrinting().create();
	private String fileName;
	private Class clazz;
	private T config;

	public AdapterlessConfigManager(String fileName, Class clazz) {
		super();
		this.fileName = fileName;
		this.clazz = clazz;
	}

	public void init() {
		initConfig();
	}

	public void writeConfig() {
		try (FileWriter w = new FileWriter("config" + File.separator + this.fileName)) {
			getGson().toJson(config, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void initConfig() {
		File f = new File("config");
		if (!f.exists()) {
			f.mkdir();
			URL inputUrl = getClass().getResource("/" + fileName);
			try {
				System.out.println("COPYING TO: " + new File("config/" + fileName));
				FileUtils.copyURLToFile(inputUrl, new File("config/" + fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (FileReader reader = new FileReader("config" + File.separator + fileName)) {
			System.out.println("CLASS: " + clazz);
			T conf = (T) getGson().fromJson(reader, clazz);
			this.config = conf;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public T getConfig() {
		return config;
	}

	public void setConfig(T config) {
		this.config = config;
	}

}
