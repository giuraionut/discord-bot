package com.discordbot.babybot.http;

import com.discordbot.babybot.database.entities.guild_member.GuildMember;
import com.discordbot.babybot.database.entities.my_guild.MyGuild;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class SelfHttpReq {
    private final String url = "http://localhost:8080/";
    private static final Logger log = LoggerFactory.getLogger(SelfHttpReq.class);
    private static final Gson gson = new Gson();

    public void post(String path, Object payload) {
        HttpPost httpPost = new HttpPost(url + path);
        StringEntity stringEntity = new StringEntity(gson.toJson(payload), ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        CloseableHttpClient client = HttpClients.createDefault();
        httpPost.setHeader("Content-type", "application/json");
        try {
            client.execute(httpPost).close();
        } catch (IOException ex) {
            log.error("Failed to execute POST request on " + url + path, ex);
        }
    }

    public Object get(String path, String id) {
        HttpGet httpGet = new HttpGet(url + path + id);
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpResponse response = client.execute(httpGet);
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity entity = response.getEntity();
            if (path.equals("user/")) {
                MyUser myUser = objectMapper.readValue(entity.getContent(), MyUser.class);
                client.close();
                return myUser;
            }
            if (path.equals("guild/")) {
                MyGuild myGuild = objectMapper.readValue(entity.getContent(), MyGuild.class);
                client.close();
                return myGuild;
            }
            if (path.equals("guildMember/")) {
                GuildMember guildMember = objectMapper.readValue(entity.getContent(), GuildMember.class);
                client.close();
                return guildMember;
            }
        } catch (IOException ex) {
            log.error("Failed to execute GET request on " + url + path, ex);
        }
        return null;
    }
}
