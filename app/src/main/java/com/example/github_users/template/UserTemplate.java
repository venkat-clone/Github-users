package com.example.github_users.template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserTemplate {


    //    public static class Root{
    public int total_count;
    public boolean incomplete_results;
    public ArrayList<Item> items = new ArrayList<Item>();

    public UserTemplate(int total_count, boolean incomplete_results, ArrayList<Item> items) {
        this.total_count = total_count;
        this.incomplete_results = incomplete_results;
        this.items = items;
    }

    public UserTemplate(JSONObject responce) {
        this.items = new ArrayList<>();
        this.total_count = 0;
        UserTemplate_add(responce);
    }

    public void UserTemplate_add(JSONObject responce) {
        try {
            this.total_count = responce.getInt("total_count");
            this.incomplete_results = responce.getBoolean("incomplete_results");
            JSONArray jsonArray = responce.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                Item item = new Item(jsonArray.getJSONObject(i));
                this.items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //    }
    public static class Item {
        public String login;
        public int id;
        public String node_id;
        public String avatar_url;
        public String gravatar_id;
        public String url;
        public String html_url;
        public String followers_url;
        public String subscriptions_url;
        public String organizations_url;
        public String repos_url;
        public String received_events_url;
        public String type;
        public int score;
        public String following_url;
        public String gists_url;
        public String starred_url;
        public String events_url;
        public boolean site_admin;

        public Item(String login, int id, String node_id, String avatar_url, String gravatar_id, String url, String html_url, String followers_url, String subscriptions_url, String organizations_url, String repos_url, String received_events_url, String type, int score, String following_url, String gists_url, String starred_url, String events_url, boolean site_admin) {
            this.login = login;
            this.id = id;
            this.node_id = node_id;
            this.avatar_url = avatar_url;
            this.gravatar_id = gravatar_id;
            this.url = url;
            this.html_url = html_url;
            this.followers_url = followers_url;
            this.subscriptions_url = subscriptions_url;
            this.organizations_url = organizations_url;
            this.repos_url = repos_url;
            this.received_events_url = received_events_url;
            this.type = type;
            this.score = score;
            this.following_url = following_url;
            this.gists_url = gists_url;
            this.starred_url = starred_url;
            this.events_url = events_url;
            this.site_admin = site_admin;
        }

        public Item(JSONObject jsonObject) throws JSONException {
            this.login = jsonObject.getString("login");
            this.id = jsonObject.getInt("id");
            this.node_id = jsonObject.getString("node_id");
            this.avatar_url = jsonObject.getString("avatar_url");
            this.gravatar_id = jsonObject.getString("gravatar_id");
            this.url = jsonObject.getString("url");
            this.html_url = jsonObject.getString("html_url");
            this.followers_url = jsonObject.getString("followers_url");
            this.subscriptions_url = jsonObject.getString("subscriptions_url");
            this.organizations_url = jsonObject.getString("organizations_url");
            this.repos_url = jsonObject.getString("repos_url");
            this.received_events_url = jsonObject.getString("received_events_url");
            this.type = jsonObject.getString("type");
            this.score = jsonObject.getInt("score");
            this.following_url = jsonObject.getString("following_url");
            this.gists_url = jsonObject.getString("gists_url");
            this.starred_url = jsonObject.getString("starred_url");
            this.events_url = jsonObject.getString("events_url");
            this.site_admin = jsonObject.getBoolean("site_admin");
        }
    }

}
