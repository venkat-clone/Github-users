package com.example.github_users.template;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;



public class UserTemp {

        public String name="";
        public String description="";
        public String url="";
        public String html_url="";
        public String updated_at="";
        public String created_at ="";
        public String clone_url="";

        public UserTemp(String name, String description, String url, String html_url, String updated_at,String created_at, String clone_url) {
                this.name = name;
                this.description = description;
                this.url = url;
                this.html_url = html_url;
                this.updated_at = updated_at;
                this.created_at = created_at;
                this.clone_url = clone_url;
        }
        public String date_at(String date) {
                String[] a  = date.split("T")[0].split("-");

                long year_ = Integer.parseInt(a[0]);
                long month_ = Integer.parseInt(a[1]);
                long day_ = Integer.parseInt(a[2]);
                Calendar today = Calendar.getInstance();
                long year_t = today.get(Calendar.YEAR);
                long month_t = today.get(Calendar.MONTH);
                long day_t = today.get(Calendar.DAY_OF_MONTH);
                year_t = year_t - year_;
                long d1 = month_*28+day_;
                long d2 = year_t*365+month_t*28 +day_t;
                long m2 = year_t*12 +month_t;



                if(d2-d1 == 1) return "today";
                if(d2-d1 < 28 ) return String.valueOf(d2-d1)+" days ago";
                if(m2-month_==1 ) return "one month ago";
                if(m2-month_<12 ) return String.valueOf(m2-month_)+" months ago";
                return "on "+date.split("T")[0];
        }
        public UserTemp(JSONObject object) throws JSONException {
                this.name = object.getString("full_name");

                this.description = object.getString("description");
                if (this.description.length()>50) this.description = this.description.substring(0,50).concat(" ...");


                this.url = object.getString("url");
                this.html_url = object.getString("html_url");

                this.clone_url = object.getString("clone_url");

                this.created_at = "created "+date_at(object.getString("pushed_at"));
                this.updated_at = "updated "+date_at(object.getString("updated_at"));




        }

}


