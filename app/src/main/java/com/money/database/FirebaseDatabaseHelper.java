package com.money.database;

import com.fivestar.models.Transaction;
import com.fivestar.models.contracts.TransactionContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dev on 26.09.2016.
 */
public class FirebaseDatabaseHelper {

    public static void saveNewObjectToFirebase(Object entity, String firebaseEntityName){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        String entityID =  databaseReference
                .child(firebaseEntityName).push().getKey();

        databaseReference
                .child(firebaseEntityName)
                .child(entityID)
                .setValue(entity);
    }

}
