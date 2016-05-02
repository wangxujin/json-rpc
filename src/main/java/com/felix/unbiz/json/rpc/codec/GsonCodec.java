package com.felix.unbiz.json.rpc.codec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.felix.unbiz.json.rpc.exception.rpc.CodecException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * ClassName: GsonCodec <br>
 * Function: Json编码接口
 *
 * @author wangxujin
 */
public class GsonCodec implements Codec {

    private static final JsonParser PARSER = new JsonParser();
    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping()
            .serializeSpecialFloatingPointValues().create();

    @Override
    public JsonElement decode(String encoding, byte[] req) throws CodecException {
        String jsonString;
        try {
            jsonString = new String(req, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new CodecException("json decode exception", e);
        }
        return PARSER.parse(jsonString);
    }

    @Override
    public byte[] encode(String encoding, JsonElement res) throws CodecException {
        try {
            String data = GSON.toJson(res);
            return data.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new CodecException("json encode exception", e);
        } catch (IOException e) {
            throw new CodecException("json encode exception", e);
        }
    }
}
