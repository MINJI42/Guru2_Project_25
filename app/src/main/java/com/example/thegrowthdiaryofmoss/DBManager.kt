package com.example.thegrowthdiaryofmoss

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {

        // 유저 정보 db
        db!!.execSQL("CREATE TABLE user (" +
                "email text PRIMARY KEY NOT NULL," +
                "id text NOT NULL, pw text NOT NULL, " +
                "userName text DEFAULT null, ikkiName text DEFAULT null," +
                "ikkiLevel INTEGER DEFAULT 0, coin INTEGER DEFAULT 0, todoCount INTEGER DEFAULT 0);")

        // 투두리스트 정보 db
        db!!.execSQL("CREATE TABLE todo (email_fk text, date date, listIndex INTEGER, list text," +
                "FOREIGN KEY (email_fk) REFERENCES user (email) );")

        // 옷 정보 db
        db!!.execSQL("CREATE TABLE dress (email_fk2 text, item1 INTEGER, " +
                "item2 INTEGER, item3 INTEGER, item4 INTEGER, item5 INTEGER, " +
                "item6 INTEGER, item7 INTEGER, item8 INTEGER, item9 INTEGER, " +
                "FOREIGN KEY (email_fk2) REFERENCES user (email));")

        // 친구 정보 db
        db!!.execSQL("CREATE TABLE friend (" +
                "userId text NOT NULL," +
                "friendId text NOT NULL," +
                "friendNickname text NOT NULL," +
                "FOREIGN KEY (userId) REFERENCES user (id)," +
                "FOREIGN KEY (friendId) REFERENCES user (id))")

        // 메시지 db
        db!!.execSQL("CREATE TABLE message (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senderId text NOT NULL," +
                "receiverId text NOT NULL," +
                "content text NOT NULL," +
                "timestamp INTEGER NOT NULL," +
                "FOREIGN KEY (senderId) REFERENCES user (id)," +
                "FOREIGN KEY (receiverId) REFERENCES user (id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) { }
}