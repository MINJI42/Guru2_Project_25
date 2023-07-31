package com.example.guru2_project_25;

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
                "email text NOT NULL," +
                "id text  PRIMARY KEY NOT NULL, pw text NOT NULL, userName text DEFAULT null, " +
                "ikkiName text DEFAULT null, ikkiLevel INTEGER DEFAULT 0, ikkidress INTEGER DEFAULT 0," +
                "coin INTEGER DEFAULT 0, todoCount INTEGER DEFAULT 0);")

        // 투두리스트 정보 db
        db!!.execSQL("CREATE TABLE todo (userId text, date date, todo text, checked INTEGER DEFAULT 0," +
                "FOREIGN KEY (userId) REFERENCES user (id) );")

        // 옷 정보 db
        db!!.execSQL("CREATE TABLE dress (userId text, item1 INTEGER, " +
                "item2 INTEGER, item3 INTEGER, item4 INTEGER, item5 INTEGER, " +
                "item6 INTEGER, item7 INTEGER, item8 INTEGER, item9 INTEGER, " +
                "FOREIGN KEY (userId) REFERENCES user (id));")

       // 친구 목록 정보 db
        db!!.execSQL("CREATE TABLE friend (" +
                "userId text NOT NULL," +
                "friendId text NOT NULL," +
                "friendNickname text NOT NULL," +
                "FOREIGN KEY (userId) REFERENCES user (id)," +
                "FOREIGN KEY (friendId) REFERENCES user (id))")

        // 메시지 전송
        db!!.execSQL("CREATE TABLE message (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senderId text NOT NULL," +
                "receiverId text NOT NULL," +
                "content text NOT NULL," +
                "timestamp INTEGER NOT NULL," +
                "FOREIGN KEY (senderId) REFERENCES user (id)," +
                "FOREIGN KEY (receiverId) REFERENCES user (id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}