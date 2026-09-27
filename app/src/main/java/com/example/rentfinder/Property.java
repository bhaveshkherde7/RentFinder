package com.example.rentfinder;

public class Property {

    int id;

    String ownerName;
    String ownerEmail;

    String name;
    String location;
    String rent;
    String roomType;
    String food;
    String wifi;
    String ac;
    String description;

    String photo1;
    String photo2;
    String photo3;

    public Property(
            int id,
            String ownerName,
            String ownerEmail,
            String name,
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

        this.id = id;

        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;

        this.name = name;
        this.location = location;
        this.rent = rent;
        this.roomType = roomType;
        this.food = food;
        this.wifi = wifi;
        this.ac = ac;
        this.description = description;

        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
    }
}