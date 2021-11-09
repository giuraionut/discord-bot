package com.discordbot.babybot.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface RedditGallery {

    default HashMap<Integer, String> getGalleryFromNode(JsonNode node) {
        HashMap<Integer, String> links = new HashMap<>();
        final JsonNode data = node.get("data");
        JsonNode galleryData = data.get("gallery_data");

        List<String> mediaIds = new ArrayList<>();

        galleryData.forEach(gd ->
                gd.forEach(x -> mediaIds.add(
                        x.get("media_id").toString().replace("\"", "")
                )));

        List<JsonNode> mediaNodes = new ArrayList<>();

        mediaIds.forEach(id -> mediaNodes.add(data.get("media_metadata").get(id)));

        int i = 0;
        for (JsonNode n : mediaNodes) {
            final int o = n.findValues("o").size();
            final int p = n.findValues("p").size();
            if (o > 0) {
                links.put(i,
                        n.get("o").get(0).get("u")
                                .toString().split("\\?")[0]
                                .replace("/preview.", "/i.").replace("\"", "")
                );
            }
            if (p > 0) {
                links.put(i,
                        n.get("p").get(0).get("u")
                                .toString().split("\\?")[0]
                                .replace("/preview.", "/i.").replace("\"", "")
                );
            }
            i++;
        }

        return links;
    }

}
