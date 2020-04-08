package com.sunnyfeng.rugraduating.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerTypeAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(final JsonWriter out, final Integer value) throws IOException {
        out.beginObject()
                .name("$numberInt")
                .value(value.toString())
                .endObject();
    }

    @Override
    public Integer read(final JsonReader in) throws IOException {
        in.beginObject();
        assert "$numberInt".equals(in.nextName());
        in.nextName();
        String integer = in.nextString();
        in.endObject();
        return new Integer(integer);
    }
}