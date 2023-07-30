package com.example.guru2_project_25;

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    // 다른 코틀린 파일에서 DB매니저 생성 시 이름은 appDB로 설정해주세요!
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
        db!!.execSQL("CREATE TABLE todo (email_todo text, date INTEGER, todo text," +
                "FOREIGN KEY (email_todo) REFERENCES user (email) );")

        // 옷 정보 db
        db!!.execSQL("CREATE TABLE dress (email_dress text, item1 INTEGER, " +
                "item2 INTEGER, item3 INTEGER, item4 INTEGER, item5 INTEGER, " +
                "item6 INTEGER, item7 INTEGER, item8 INTEGER, item9 INTEGER, " +
                "FOREIGN KEY (email_dress) REFERENCES user (email));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}