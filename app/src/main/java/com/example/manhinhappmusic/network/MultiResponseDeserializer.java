package com.example.manhinhappmusic.network;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.PlaylistResponse;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.dto.UserResponse;
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
        JsonElement dataElement = jsonObject.get("data");
        Object data;
        switch (type) {
            case "song":
                data = context.deserialize(dataElement, SongResponse.class);
                break;
            case "playlist":
                data = context.deserialize(dataElement, PlaylistResponse.class);
                break;
            case "artist":
                data = context.deserialize(dataElement, UserResponse.class);
                break;
            default:
                data = null;
                break;

        }
        MultiResponse response = new MultiResponse();
        response.setType(type);
        response.setData(data);

        return response;
    }
}
