package com.cloud.accelerator.commons;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.Map;

public class FirestoreUtils {

    static Firestore db;

    public static Map<String, Object> getDocumentAsMap(String collectionName, String documentName) throws Exception {
        // [START firestore_data_get_as_map]
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        } else {
            System.out.println("No such document!");
        }
        return (document.exists()) ? document.getData() : null;
    }

    public static List<QueryDocumentSnapshot> getQueryResults(String collectionName, String documentName) throws Exception {
        //asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future =
                db.collection(collectionName).whereEqualTo(documentName, true).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            System.out.println(document.getId());
        }
        return documents;
    }

    public static List<QueryDocumentSnapshot> getAllDocuments(String collectionName) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection(collectionName).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println(document.getId());
        }
        return documents;
    }

}
