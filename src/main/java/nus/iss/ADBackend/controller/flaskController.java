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

import net.minidev.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/flask")
public class flaskController {

	@RequestMapping("/recieveImgFromAndroid")
	public ResponseEntity recieveImgFromAndroid(@RequestBody byte[] imgByteArray) {
		System.out.println(imgByteArray.toString());
		String outputResponse = getPrediction(imgByteArray);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("outputResponse", outputResponse);
		return new ResponseEntity<>(jsonObj, HttpStatus.ACCEPTED);

	}

	private String getPrediction(byte[] imgByteArray) {
		HttpURLConnection conn = null;
		DataOutputStream os = null;
		String outputResponse = "";

		try {
			System.out.println("*************");
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
		System.out.println("#############");
		return outputResponse;

	}

//    
//    public String getPrediction(String imgByteString){
//        HttpURLConnection conn = null;
//        DataOutputStream os = null;
//        
//        try{
//            StringBuilder sb = new StringBuilder();
//            sb.append("data:image/png;base64,");
//            byte[] imageByteArray = file.getBytes();
//            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false)));
//            String image = sb.toString();
//            
//            JSONObject obj = new JSONObject();
//            obj.put("image", image);
//            URL url = new URL("http://127.0.0.1:5000/predict_api");
//
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty( "charset", "utf-8");
//            os = new DataOutputStream(conn.getOutputStream());
//            os.write(imageByteArray);
//
//            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//            String output;
//            while((output = br.readLine()) != null){
//                model.addAttribute("message", output);
//            }
//            conn.disconnect();
//
//
//        }
//
//        catch(IOException e){
//            e.getStackTrace();
//        }
//
//        return "upload";
//    }

}
