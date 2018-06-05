package com.samadeniyi.armdemo;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseException;

public class Logout {
    static public boolean logout(){
        boolean isLoggedOut = true;
        try{
        Firebase reference = new Firebase("https://armdemo-b8b6f.firebaseio.com/users");
        reference.child(UserDetails.username).child("isLoggedIn").setValue(false);
        }catch(FirebaseException e){
            System.out.println("" + e.toString());
            isLoggedOut = false;
        }
        return isLoggedOut;
    }
}
