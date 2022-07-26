package nus.iss.ADBackend.controller;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;



@Controller
@RequestMapping("/flask")
public class flaskController {
    
    @RequestMapping("/")
    public String homepage(){

        return "upload";
    }


    @RequestMapping("/predict")
    public String getPrediction(@RequestParam("file") MultipartFile file, Model model){
        HttpURLConnection conn = null;
        DataOutputStream os = null;
        
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            byte[] imageByteArray = file.getBytes();
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageByteArray, false)));
            String image = sb.toString();
            
            JSONObject obj = new JSONObject();
            obj.put("image", image);
            URL url = new URL("http://127.0.0.1:5000/predict_api");

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            os = new DataOutputStream(conn.getOutputStream());
            os.write(imageByteArray);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while((output = br.readLine()) != null){
                model.addAttribute("message", output);
            }
            conn.disconnect();


        }

        catch(IOException e){
            e.getStackTrace();
        }

        return "upload";
    }

}
