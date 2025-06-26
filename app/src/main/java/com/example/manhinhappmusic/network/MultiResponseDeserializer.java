package com.example.manhinhappmusic.network;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.model.Playlist;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MultiResponseDeserializer implements JsonDeserializer<MultiResponse> {


    @Override
    public MultiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "song":
                return context.deserialize(jsonObject, SongResponse.class);
            case "playlist":
                return context.deserialize(jsonObject, Playlist.class);
            default:
                // fallback
                return context.deserialize(jsonObject, MultiResponseImp.class);
        }
    }
}
