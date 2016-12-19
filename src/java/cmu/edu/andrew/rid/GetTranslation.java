
package cmu.edu.andrew.rid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import org.bson.Document;


/**
 * 
 * This class sends an English phrase the to the Google Translate API and sends
 * back a JSON with the phrase translated to french. It logs the interaction in 
 * a MongoDB.
 *
 * @author RebeccaDilla
 * November 2016
 *
 *
 */
public class GetTranslation {

    public String translation(String english, Document log) {
        String translation = ""; //to hold the JSON containing the translation
        try {

            //Add the phrase to translate to the url 
            URL url = new URL("https://translation.googleapis.com/language/translate/v2?key=AIzaSyC3PP98M3b9Z0U1BRmbAgPhnmp2Hs9aDbY&source=en&target=fr&q=" + english);
            
            //Log the URL
            log.append("URL", url.toString());
            
            //Connect to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            //Log the time the connection was requested
            log.append("connectionRequestTime", new Date());
            conn.connect();
            
            //Log the time the connection was made
            log.append("timeConnected", new Date());

            //Read in the JSON from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                translation = translation+inputLine;
            }
            in.close();//close the reader


        } catch (MalformedURLException ex) {
            Logger.getLogger(GetTranslation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GetTranslation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetTranslation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return translation; //return the JSON containing the translation
    }
    
}
