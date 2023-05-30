package com.example.multiliveapp.followDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    //DBHelper를 생성자로 관리할 db 이름과 버전 정보를 받음.
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //db를 새로 생성할때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 followTB 이고
        myNick 문자열 컬럼 (현재 사용자) , followUser 문자열 컬럼 (팔로우 한 유저닉넴) */
        db.execSQL("CREATE TABLE followTB (myNick TEXT, followUser TEXT);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String nick, String user) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO followTB (myNick, followUser) VALUES (?,?)", new String[]{nick, user});
        db.close();
    }


    public void delete(String followUser) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM followTB WHERE followUser = (?) ", new String[]{followUser});
        db.close();
    }

    public void deleteUSER(String myNick) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM followTB WHERE myNick = (?) ", new String[]{myNick});
        db.close();
    }

    public List getFollowUserList() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        List<String> result = new ArrayList<String>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM followTB", null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(1));
        }

        return result;

    }


}
