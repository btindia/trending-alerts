package com.phamousapps.trendalert.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamHelper {

	public static String readInputStream(InputStream inputStream)
			throws IOException {
		
		String responseLine;
		StringBuilder responseBuilder = new StringBuilder();

		if (inputStream == null) {
			return responseBuilder.toString();
		}

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));

		try {

			while ((responseLine = bufferedReader.readLine()) != null) {
				responseBuilder.append(responseLine);
			}

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}

		return responseBuilder.toString();
	}
}
