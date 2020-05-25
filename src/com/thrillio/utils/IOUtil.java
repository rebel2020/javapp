package com.thrillio.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

public class IOUtil {
	public static void read(List<String> data, String fileName) {
		int count = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String s;
			while ((s = br.readLine()) != null) {
				data.add(s);
			}
		} catch (IOException e) {
			System.out.println("Error");
			e.getStackTrace();
		}
	}

	public static String read(InputStream in) {
		StringBuilder text = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line + '\n');
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return text.toString();
	}

	public static void write(String webpage, long id) {
		StringBuilder text = new StringBuilder();
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("pages/" + String.valueOf(id) + ".html"), "utf-8"))) {
			writer.write(webpage);
		}catch (FileNotFoundException e) {
			e.getStackTrace();
		}
		catch (Exception e) {
			e.getStackTrace();
		}
	}
	
}
