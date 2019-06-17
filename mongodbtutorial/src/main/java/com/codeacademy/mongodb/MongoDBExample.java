package com.codeacademy.mongodb;

import com.codeacademy.mongodb.util.MongoDBUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBExample {

    public static void main(String[] theory) {

        MongoClient mongoClient = MongoDBUtil.createMongoClient();
        MongoDatabase database = mongoClient.getDatabase("tutorial");
        MongoCollection<Document> collection = database.getCollection("firstCollection");

        System.out.println(collection.countDocuments());
    }

}
