package com.lynxit.contactswrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.net.URI;
import java.util.*;

import com.facebook.react.*;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.ViewManager;

public class ContactsWrapper extends ReactContextBaseJavaModule implements ActivityEventListener {

    private static final int CONTACT_REQUEST = 1;
    private static final String E_CONTACT_CANCELLED = "E_CONTACT_CANCELLED";
    private static final String E_CONTACT_NO_DATA = "E_CONTACT_NO_DATA";
    private static final String E_CONTACT_NO_EMAIL = "E_CONTACT_NO_EMAIL";
    private static final String E_CONTACT_EXCEPTION = "E_CONTACT_EXCEPTION";
    private Promise mContactsPromise;
    private Activity mCtx;


    public ContactsWrapper(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "ContactsWrapper";
    }

    @ReactMethod
    public void getEmail(Promise contactsPromise) {
        mContactsPromise = contactsPromise;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        mCtx = getCurrentActivity();
        if (intent.resolveActivity(mCtx.getPackageManager()) != null) {
            mCtx.startActivityForResult(intent, CONTACT_REQUEST);
        }


    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode != CONTACT_REQUEST) {
            return;
        }

        String email = null;
        switch (resultCode) {
            case (Activity.RESULT_OK):
                try {
                    Uri contactUri = intent.getData();


                    // get the contact id from the Uri
                    String id = contactUri.getLastPathSegment();

                    // query for everything email
                   Cursor cursor = mCtx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id },
                            null);

                    int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                    // let's just get the first email
                    if (cursor.moveToFirst()) {
                        email = cursor.getString(emailIdx);
                        mContactsPromise.resolve(email);
                        return;
                    } else {
                        mContactsPromise.reject(E_CONTACT_NO_EMAIL, "No email found for contact");
                        return;
                    }


                /*
                    //First get ID
                    String id = null;
                    int idx;
                    Cursor cursor = mCtx.getContentResolver().query(contactUri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                        id = cursor.getString(idx);
                    } else {
                        mContactsPromise.reject(E_CONTACT_NO_DATA, "Contact Data Not Found");
                        return;
                    }

                    // Build the Entity URI.
                    Uri.Builder b = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id).buildUpon();
                    b.appendPath(ContactsContract.Contacts.Entity.CONTENT_DIRECTORY);
                    contactUri = b.build();

                    // Create the projection (SQL fields) and sort order.
                    String[] projection = {
                            ContactsContract.Contacts.Entity.RAW_CONTACT_ID,
                            ContactsContract.Contacts.Entity.DATA1,
                            ContactsContract.Contacts.Entity.MIMETYPE };
                    String sortOrder = ContactsContract.Contacts.Entity.RAW_CONTACT_ID + " ASC";
                    cursor = mCtx.getContentResolver().query(contactUri, projection, null, null, sortOrder);

                    String mime;
                    int mimeIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.MIMETYPE);
                    int dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1);
                    if (cursor.moveToFirst()) {
                        do {
                            mime = cursor.getString(mimeIdx);
                            if (mime.equalsIgnoreCase(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                                email = cursor.getString(dataIdx);
                            }

                        } while (cursor.moveToNext());
                    }

                    if(email != null) {
                        mContactsPromise.resolve(email);
                        return;
                    } else {
                        mContactsPromise.reject(E_CONTACT_NO_EMAIL, "No Email Address Found For User");
                        return;
                    }*/

                } catch (Exception e) {
                    mContactsPromise.reject(E_CONTACT_EXCEPTION, e.getMessage());
                    return;
                }
            default:
                mContactsPromise.reject(E_CONTACT_CANCELLED, "Cancelled");
                return;
        }
    }
}
