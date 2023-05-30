package com.example.multiliveapp;

public class MemberInfo {

    /******* 추가할 부분 *******/
    /* 사용자 테이블 */
    // 보류 private int user_id;
    // 보류 private int user_tag;
    private String user_email;
    private String user_pw;
    private String user_nick;
    private String user_name;
    private String user_gender; // boolean이 안되길래 String으로 함
    private String user_country; // int가 안되길래 String으로 함
    private String user_birth;
    // 보류 private int user_join; // 생성일은 Autentication에 자동으로 적혀져서 없어도 될 거 같음
    // 보류 private String user_comm;
    private String user_phone;
    private String user_photoUrl;
    private String user_stag1;
    private String user_stag2;
    private String user_stag3;
    private String user_stag4;
    private String user_stag5;
    private String user_stag6;
    private String user_stag7;
    private String user_stag8;
    private String user_stag9;


    /* 태그 테이블 */
   private String tag_id;
   private String tag_name;
   private int tag_num;

    /* 팔로우 테이블 */
    private String follow_id;
    private int follow_follower_id;
    private int follow_followed_id;
    private String follow_name;
    private String follow_date;
    private int follow_num;

    /* 라이브 테이블 */
    private String live_id;
    private String live_mem_id;
    private String live_title;
    private int live_join;
    private boolean live_public;
    private int live_tag_num;

    public MemberInfo(String user_email, String user_pw, String user_nick, String user_name, String user_gender, String user_country, String user_birth, String user_phone, String stag1, String stag2, String stag3, String stag4, String stag5, String stag6, String stag7, String stag8, String stag9) {
        this.user_email = user_email;
        this.user_pw = user_pw;
        this.user_nick = user_nick;
        this.user_name = user_name;
        this.user_gender = user_gender;
        this.user_country = user_country;
        this.user_birth = user_birth;
        this.user_phone = user_phone;
        this.user_stag1 = stag1;
        this.user_stag2 = stag2;
        this.user_stag3 = stag3;
        this.user_stag4 = stag4;
        this.user_stag5 = stag5;
        this.user_stag6 = stag6;
        this.user_stag7 = stag7;
        this.user_stag8 = stag8;
        this.user_stag9 = stag9;

    }


    public String get_user_email() { return this.user_email; }
    public void set_user_email(String user_email) {
       this.user_email = user_email;
    }

   public String get_user_pw() { return this.user_pw; }
    public void set_user_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String get_user_nick() { return this.user_nick; }
    public void set_user_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String get_user_name() {
        return this.user_name;
    }
    public void set_user_name(String user_name) {
        this.user_name = user_name;
    }

    public String get_user_gender() {
        return this.user_gender;
    }
    public void set_user_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String get_user_country() { return this.user_country; }
    public void set_user_country(String user_country) { this.user_country = user_country; }

    public String get_user_birth() {
        return this.user_birth;
    }
    public void set_user_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String get_user_phone() {
        return this.user_phone;
    }
    public void set_user_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String get_user_stag1(){
        return this.user_stag1;
    }
    public void set_user_stag1(String user_stag1){
        this.user_stag1 = user_stag1;
    }

    public String get_user_stag2(){
        return this.user_stag2;
    }
    public void set_user_stag2(String user_stag2){
        this.user_stag2 = user_stag2;
    }

    public String get_user_stag3(){
        return this.user_stag3;
    }
    public void set_user_stag3(String user_stag3){
        this.user_stag3 = user_stag3;
    }

    public String get_user_stag4(){
        return this.user_stag4;
    }
    public void set_user_stag4(String user_stag4){
        this.user_stag4 = user_stag4;
    }

    public String get_user_stag5(){
        return this.user_stag5;
    }
    public void set_user_stag5(String user_stag5){
        this.user_stag5 = user_stag5;
    }

    public String get_user_stag6(){
        return this.user_stag6;
    }
    public void set_user_stag6(String user_stag6){
        this.user_stag6 = user_stag6;
    }

    public String get_user_stag7(){
        return this.user_stag7;
    }
    public void set_user_stag7(String user_stag7){
        this.user_stag7 = user_stag7;
    }

    public String get_user_stag8(){
        return this.user_stag8;
    }
    public void set_user_stag8(String user_stag8){
        this.user_stag8 = user_stag8;
    }

    public String get_user_stag9(){
        return this.user_stag9;
    }
    public void set_user_stag9(String user_stag9){
        this.user_stag9 = user_stag9;
    }

}
