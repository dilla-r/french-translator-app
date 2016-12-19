
package cmu.edu.andrew.rid;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 * This Web Service uses the Google Translate API to translate a phrase
 * submitted in English to French. It also logs transaction with the web service
 * using Mongo DB and displays the logs in a dashboard.
 * 
 * November 2016
 * @author Rebecca Dilla
 */
@WebServlet(name = "FrenchTranslate", urlPatterns = {"/FrenchTranslate", "/dashboard"})
public class FrenchTranslate extends HttpServlet {

    GetTranslation gt = new GetTranslation();

    /**    
     * Handles the HTTP <code>GET</code> method.
     * The method takes the phrase from the get request and returns
     * a French translation. It also logs the request in a Mongo DB, and 
     * launches a dashboard to view the logs. 
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //Code from MongoDB Driver Quick Tour
        //http://mongodb.github.io/mongo-java-driver/3.2/driver/getting-started/quick-tour/
        //This code connects to the Mongo DB to log the interaction and display the 
        //dashboard
        MongoClientURI connectionString = new MongoClientURI("mongodb://dist:systems@ds157667.mlab.com:57667/frenchtranslator");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase db = mongoClient.getDatabase("frenchtranslator");
        
        //Retrieves the log Collection from MongoDB
        MongoCollection<Document> logs = db.getCollection("logs");

        //Load the dashboard if requested
        if (request.getServletPath().equals("/dashboard")) {
            //Build up a string with the log files from the MongoDB
            String logString = "<h2>LOGS</h2>";

            //Add each Document in the Collection to the String
            MongoCursor<Document> cursor = logs.find().iterator();
            try {
                while (cursor.hasNext()) {
                    logString = logString + cursor.next().toJson() + "<br/><br/>";
                    // System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }
            
            //Set the LOGS attribute to the String
            request.setAttribute("LOGS", logString);
            //Forward to dashboard view
            String nextView =  "index.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);

        } else {

            //If a translation was requested, return a translation
            //and store a log of the interaction
            
            //Create a new log Document
            Document singleLog = new Document();
            
            //Log the time the request was recieved
            singleLog.append("requestRecievedTime", new Date());
            
            //Retrieve the English phrase to translate
            String toTranslate = request.getParameter("phrase");

            //Log the phrase to be translate
            singleLog.append("toTranslate", toTranslate);
            
            //Pass the phrase and log to the GetTranslation class. Returns a JSON
            //with the translated phrase
            String french = gt.translation(toTranslate, singleLog);
            
            //Send the JSON as a response
            response.setContentType("application/json");
            response.getWriter().write(french);
            
            //Log the time the response was sent
            singleLog.append("responseSentTime", new Date().getTime());
            
            //Log the JSON that was sent
            singleLog.append("responseData", french);
            
            //Log the length of the response String
            singleLog.append("responseLength", french.length());
            
            //Insert the log document into the Collection
            logs.insertOne(singleLog);
        }

    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
