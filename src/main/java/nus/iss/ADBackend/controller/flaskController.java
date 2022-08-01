package nus.iss.ADBackend.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestParam;
import net.minidev.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/flask")
public class flaskController {

	@RequestMapping("/recieveImgFromAndroid")
	public ResponseEntity<String> recieveImgFromAndroid(@RequestBody byte[] imgByteArray) {
		System.out.println(imgByteArray.toString());
		String outputResponse = getPrediction(imgByteArray);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("outputResponse", outputResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Custom-Header", "foo");
		return new ResponseEntity<>(outputResponse, headers, HttpStatus.OK);

	}

	private String getPrediction(byte[] imgByteArray) {
		HttpURLConnection conn = null;
		DataOutputStream os = null;
		String outputResponse = "";

		try {
			URL url = new URL("http://127.0.0.1:5000/predict_api");

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("charset", "utf-8");
			os = new DataOutputStream(conn.getOutputStream());
			os.write(imgByteArray);

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				outputResponse += output;
			}
			conn.disconnect();
		} catch (IOException e) {
			conn.disconnect();
			e.getStackTrace();
		}
		return outputResponse;

	}
}
