package com.example.rentfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rentfinder.db";
    private static final int DATABASE_VERSION = 6;

    public DBHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // =====================================================
    // CREATE DATABASE
    // =====================================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // USERS
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "email TEXT UNIQUE," +
                        "password TEXT," +
                        "role TEXT)"
        );

        // PROPERTIES
        db.execSQL(
                "CREATE TABLE properties (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "owner_email TEXT," +
                        "property_name TEXT," +
                        "location TEXT," +
                        "rent TEXT," +
                        "room_type TEXT," +
                        "food TEXT," +
                        "wifi TEXT," +
                        "ac TEXT," +
                        "description TEXT," +
                        "photo1 TEXT," +
                        "photo2 TEXT," +
                        "photo3 TEXT," +
                        "status TEXT)"
        );

        // INQUIRIES
        db.execSQL(
                "CREATE TABLE inquiries (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "property_id INTEGER," +
                        "student_email TEXT," +
                        "question TEXT," +
                        "status TEXT)"
        );
    }

    // =====================================================
    // DATABASE UPGRADE
    // =====================================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        /*
         * Development version.
         *
         * WARNING:
         * Existing local data will be deleted
         * when database version changes.
         */

        db.execSQL(
                "DROP TABLE IF EXISTS inquiries"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS properties"
        );

        db.execSQL(
                "DROP TABLE IF EXISTS users"
        );

        onCreate(db);
    }

    // =====================================================
    // REGISTER USER
    // =====================================================

    public boolean registerUser(
            String name,
            String email,
            String password,
            String role) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("role", role);

        long result =
                db.insert(
                        "users",
                        null,
                        values
                );

        return result != -1;
    }

    // =====================================================
    // CHECK EMAIL
    // =====================================================

    public boolean isEmailRegistered(
            String email) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id FROM users WHERE email=?",
                        new String[]{
                                email
                        }
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        return exists;
    }

    // =====================================================
    // LOGIN
    // =====================================================

    public boolean checkLogin(
            String email,
            String password,
            String role) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id FROM users " +
                                "WHERE email=? " +
                                "AND password=? " +
                                "AND role=?",
                        new String[]{
                                email,
                                password,
                                role
                        }
                );

        boolean result =
                cursor.moveToFirst();

        cursor.close();

        return result;
    }

    // =====================================================
    // GET USER NAME
    // =====================================================

    public String getUserName(
            String email) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT name FROM users WHERE email=?",
                        new String[]{
                                email
                        }
                );

        String name = "";

        if (cursor.moveToFirst()) {

            name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "name"
                            )
                    );
        }

        cursor.close();

        return name;
    }

    // =====================================================
    // ADD PROPERTY
    // =====================================================

    public boolean addProperty(
            String ownerEmail,
            String propertyName,
            String location,
            String rent,
            String roomType,
            String food,
            String wifi,
            String ac,
            String description,
            String photo1,
            String photo2,
            String photo3) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "owner_email",
                ownerEmail
        );

        values.put(
                "property_name",
                propertyName
        );

        values.put(
                "location",
                location
        );

        values.put(
                "rent",
                rent
        );

        values.put(
                "room_type",
                roomType
        );

        values.put(
                "food",
                food
        );

        values.put(
                "wifi",
                wifi
        );

        values.put(
                "ac",
                ac
        );

        values.put(
                "description",
                description
        );

        values.put(
                "photo1",
                photo1
        );

        values.put(
                "photo2",
                photo2
        );

        values.put(
                "photo3",
                photo3
        );

        // New property is pending
        values.put(
                "status",
                "Pending"
        );

        long result =
                db.insert(
                        "properties",
                        null,
                        values
                );

        return result != -1;
    }

    // =====================================================
    // GET OWNER PROPERTIES
    // =====================================================

    public Cursor getOwnerProperties(
            String ownerEmail) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM properties " +
                        "WHERE owner_email=? " +
                        "ORDER BY id DESC",
                new String[]{
                        ownerEmail
                }
        );
    }

    // =====================================================
    // GET ALL APPROVED PROPERTIES WITH OWNER
    // =====================================================

    public Cursor getApprovedPropertiesWithOwner() {

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT p.*, " +
                        "u.name AS owner_name " +
                        "FROM properties p " +
                        "LEFT JOIN users u " +
                        "ON p.owner_email = u.email " +
                        "WHERE p.status='Approved' " +
                        "ORDER BY p.id DESC";

        return db.rawQuery(
                query,
                null
        );
    }

    // =====================================================
    // GET APPROVED PROPERTIES
    // =====================================================

    public Cursor getApprovedProperties() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM properties " +
                        "WHERE status='Approved' " +
                        "ORDER BY id DESC",
                null
        );
    }

    // =====================================================
    // SEARCH PROPERTY BY LOCATION
    // =====================================================

    public Cursor searchPropertiesByLocation(
            String location) {

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT p.*, " +
                        "u.name AS owner_name " +
                        "FROM properties p " +
                        "LEFT JOIN users u " +
                        "ON p.owner_email = u.email " +
                        "WHERE p.status='Approved' " +
                        "AND LOWER(p.location) LIKE ? " +
                        "ORDER BY p.id DESC";

        return db.rawQuery(
                query,
                new String[]{
                        "%" +
                                location
                                        .trim()
                                        .toLowerCase() +
                                "%"
                }
        );
    }

    // =====================================================
    // GET SINGLE PROPERTY BY ID
    // =====================================================

    public Cursor getPropertyById(
            int propertyId) {

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT " +
                        "p.id, " +
                        "p.owner_email, " +
                        "p.property_name, " +
                        "p.location, " +
                        "p.rent, " +
                        "p.room_type, " +
                        "p.food, " +
                        "p.wifi, " +
                        "p.ac, " +
                        "p.description, " +
                        "p.photo1, " +
                        "p.photo2, " +
                        "p.photo3, " +
                        "p.status, " +
                        "COALESCE(u.name, 'Owner') AS owner_name " +
                        "FROM properties p " +
                        "LEFT JOIN users u " +
                        "ON p.owner_email = u.email " +
                        "WHERE p.id=? " +
                        "AND p.status='Approved'";

        return db.rawQuery(
                query,
                new String[]{
                        String.valueOf(propertyId)
                }
        );
    }

    // =====================================================
    // GET PENDING PROPERTIES
    // =====================================================

    public Cursor getPendingProperties() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.rawQuery(
                "SELECT p.*, " +
                        "u.name AS owner_name " +
                        "FROM properties p " +
                        "LEFT JOIN users u " +
                        "ON p.owner_email = u.email " +
                        "WHERE p.status='Pending' " +
                        "ORDER BY p.id DESC",
                null
        );
    }

    // =====================================================
    // APPROVE / REJECT PROPERTY
    // =====================================================

    public boolean updatePropertyStatus(
            int propertyId,
            String status) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status",
                status
        );

        int result =
                db.update(
                        "properties",
                        values,
                        "id=?",
                        new String[]{
                                String.valueOf(propertyId)
                        }
                );

        return result > 0;
    }

    // =====================================================
    // DELETE PROPERTY
    // =====================================================

    public boolean deleteProperty(
            int propertyId) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        "properties",
                        "id=?",
                        new String[]{
                                String.valueOf(propertyId)
                        }
                );

        return result > 0;
    }

    // =====================================================
    // ADD STUDENT INQUIRY
    // =====================================================

    public boolean addInquiry(
            int propertyId,
            String studentEmail,
            String question) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "property_id",
                propertyId
        );

        values.put(
                "student_email",
                studentEmail
        );

        values.put(
                "question",
                question
        );

        values.put(
                "status",
                "Pending"
        );

        long result =
                db.insert(
                        "inquiries",
                        null,
                        values
                );

        return result != -1;
    }

    // =====================================================
    // GET STUDENT INQUIRIES
    // =====================================================

    public Cursor getStudentInquiries(
            String studentEmail) {

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT i.*, " +
                        "p.property_name, " +
                        "p.location, " +
                        "p.owner_email, " +
                        "u.name AS owner_name " +
                        "FROM inquiries i " +
                        "LEFT JOIN properties p " +
                        "ON i.property_id = p.id " +
                        "LEFT JOIN users u " +
                        "ON p.owner_email = u.email " +
                        "WHERE i.student_email=? " +
                        "ORDER BY i.id DESC";

        return db.rawQuery(
                query,
                new String[]{
                        studentEmail
                }
        );
    }

    // =====================================================
    // GET OWNER INQUIRIES
    // =====================================================

    public Cursor getOwnerInquiries(
            String ownerEmail) {

        SQLiteDatabase db =
                getReadableDatabase();

        String query =
                "SELECT i.*, " +
                        "p.property_name, " +
                        "p.location, " +
                        "p.owner_email, " +
                        "u.name AS student_name " +
                        "FROM inquiries i " +
                        "LEFT JOIN properties p " +
                        "ON i.property_id = p.id " +
                        "LEFT JOIN users u " +
                        "ON i.student_email = u.email " +
                        "WHERE p.owner_email=? " +
                        "ORDER BY i.id DESC";

        return db.rawQuery(
                query,
                new String[]{
                        ownerEmail
                }
        );
    }

    // =====================================================
    // UPDATE INQUIRY STATUS
    // =====================================================

    public boolean updateInquiryStatus(
            int inquiryId,
            String status) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "status",
                status
        );

        int result =
                db.update(
                        "inquiries",
                        values,
                        "id=?",
                        new String[]{
                                String.valueOf(inquiryId)
                        }
                );

        return result > 0;
    }

    // =====================================================
    // DELETE INQUIRY
    // =====================================================

    public boolean deleteInquiry(
            int inquiryId) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        "inquiries",
                        "id=?",
                        new String[]{
                                String.valueOf(inquiryId)
                        }
                );

        return result > 0;
    }
}