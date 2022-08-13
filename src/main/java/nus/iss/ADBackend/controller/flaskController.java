package nus.iss.ADBackend.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.Service.WrongPredictionService;
import nus.iss.ADBackend.model.Ingredient;

@RestController
@RequestMapping("/flask")
public class flaskController {

	@Autowired
	private IngredientService ingredientService;

	@Autowired
	private WrongPredictionService wrongPredictionService;

	@RequestMapping("/recieveImgFromAndroid")
	public ResponseEntity<Object> recieveImgFromAndroid(@RequestBody byte[] imgByteArray) {
		System.out.println(imgByteArray.toString());
		String outputResponse = getPrediction(imgByteArray);
		String predictedItemName = stringCleaner(outputResponse);
		if(predictedItemName.equals("sweet potato leaf raw")){
			predictedItemName = predictedItemName.replace(" raw", "");
		}
		// System.out.println(predictedItemName);
		Ingredient ingredient = ingredientService.findIngredientByName(predictedItemName);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("outputResponse", ingredient);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Custom-Header", "foo");
		return new ResponseEntity<Object>(ingredient, headers, HttpStatus.OK);

	}

	@RequestMapping("/oopsModelGotItWrong")
	public ResponseEntity uploadingActualResultsFromUser(@RequestBody JSONObject response){
		try {
			String predicted = response.getAsString("predicted");
			String actual = response.getAsString("actual");
			String photoString = response.getAsString("photoString");
			// Save object into DB 
			// System.out.println("predicted: " + predicted);
			// System.out.println("actual: " + actual);
			wrongPredictionService.createWrongPrediction(actual, predicted, photoString);
			
			return new ResponseEntity<>(null, HttpStatus.ACCEPTED);

		}
		catch (Exception ex){
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
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

	private String stringCleaner(String category) {

		String tmpStr = " ";
		String regex = "(^[\"]+)|([\"]+$)";
		category = category.replaceAll(regex, "");
		String[] strArr = category.split("_");

		for (int i = 0; i < strArr.length; i++) {
			if (!strArr[i].equals("fruit") && !strArr[i].equals("vegetable")) {
				tmpStr = tmpStr + strArr[i].toLowerCase() + " ";
			}
		}
		return tmpStr.trim();
	}
}
