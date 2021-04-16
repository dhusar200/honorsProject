package org.me.gcu.honorsproject;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Expression;
import com.amazonaws.mobileconnectors.dynamodbv2.document.QueryOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Search;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class RetrieveLocationsTask extends AsyncTask<Object, Void, Object> {

    private int count = 0;
    private MainActivity activity;
    private List<Document> documents;
    private final LinkedList<Locations> locationsList = new LinkedList<>();

    public RetrieveLocationsTask(MainActivity a)
    {
        this.activity = a;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
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

        final Expression expression2 = new Expression();
        expression2.setExpressionStatement("#p = :PK");
        expression2.withExpressionAttibuteValues(":PK", new Primitive("AllLocations"));
        expression2.addExpressionAttributeNames("#p", "location");

        QueryOperationConfig queryOperationConfig = new QueryOperationConfig();
        queryOperationConfig.withKeyExpression(expression2);
        Search searchResult = dbTable.query(queryOperationConfig);

        documents = searchResult.getAllResults();
        Locations location = null;

        for (int i = 0; i < documents.size(); i++)
        {
            location = new Locations();
            try {
                JSONObject jsonObject = new JSONObject(Document.toJson(documents.get(i)));
                location.setName(jsonObject.getString("name"));
                location.setGlat(Float.parseFloat(jsonObject.getString("Glat")));
                location.setGlong(Float.parseFloat(jsonObject.getString("Glong")));
                location.setMaxCapacity(Integer.parseInt(jsonObject.getString("maxCapacity")));
                String[] temp = jsonObject.getString("device # MAC # UUID").split("#");
                location.setContinent(temp[0]);
                location.setCountry(temp[1]);
                location.setState(temp[2]);
                location.setCity(temp[3]);
                location.setPostCode(temp[4]);
                location.setStreetName(temp[5]);
                location.setNumber(temp[6]);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            locationsList.add(location);
        }
        Log.e("MyTag", locationsList.toString());

        count = locationsList.size();

        return locationsList;
    }

    public int getCount() {
        return count;
    }

    public List<Locations> getLocations()
    {
        return locationsList;
    }
}