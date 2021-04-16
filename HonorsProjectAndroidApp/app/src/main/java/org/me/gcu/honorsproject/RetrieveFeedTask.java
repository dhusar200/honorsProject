package org.me.gcu.honorsproject;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryFilter;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RetrieveFeedTask extends AsyncTask<Void, Void, Integer> {

    private int count = 0;
    private LocationDetails activity;
    private LocalDateTime timeNow;
    private List<Document> documents;
    private String location, finalTime;

    public RetrieveFeedTask(LocationDetails a, String location)
    {
        this.activity = a;
        this.location = location;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        // Improve by getting the table and sharing it between the activities instead of getting
        //it every single time anew
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                "IdentityPoolId", // Identity pool ID
                Regions.EU_WEST_2 // Region
        );

        AmazonDynamoDBClient dbClient = new AmazonDynamoDBClient(credentialsProvider);
        //For some reason region is not set correctly the first time
        dbClient.setRegion(Region.getRegion(Regions.EU_WEST_2));
        Log.e("MyTag",dbClient.getRegions().toString());
        Table dbTable = Table.loadTable(dbClient, "honorsS1717689");



        timeNow = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ss");
        if ( 0 <= Integer.parseInt(timeNow.format(format)) && Integer.parseInt(timeNow.format(format)) < 30)
        {
            //minus one hour as this is from europe, TODO convert to one time
            DateTimeFormatter finalformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:");
            finalTime = LocalDateTime.now().minusHours(1).minusMinutes(1).format(finalformat) + "4";
            Log.e("MyTagTime", finalTime);
            //40
        }
        else
        {
            //minus one hour as this is from europe, TODO convert to one time
            DateTimeFormatter finalformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:");
            finalTime = LocalDateTime.now().minusHours(1).format(finalformat) + "1";
            Log.e("MyTagTime", finalTime);
            //10
        }

        Log.e("MyTag", finalTime);

        final Expression expression2 = new Expression();
        expression2.setExpressionStatement("#p = :PK AND begins_with(#s, :SK)");
        expression2.withExpressionAttibuteValues(":PK", new Primitive(location));
        expression2.withExpressionAttibuteValues(":SK", new Primitive(finalTime));
        expression2.addExpressionAttributeNames("#p", "location");
        expression2.addExpressionAttributeNames("#s", "device # MAC # UUID");

        QueryOperationConfig queryOperationConfig = new QueryOperationConfig();
        queryOperationConfig.withKeyExpression(expression2);
        Search searchResult = dbTable.query(queryOperationConfig);

        documents = searchResult.getAllResults();
        Log.e("MyTag", String.valueOf(documents.size()));

        //if size is 0 then TODO
        if(documents.size() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(Document.toJson(documents.get(0)));
                Log.e("MyTag", jsonObject.getString("location"));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        count = documents.size();

        return documents.size();
    }

    public int getCount() {
        return count;
    }

    public List<Document> getDocuments()
    {
        return documents;
    }
}