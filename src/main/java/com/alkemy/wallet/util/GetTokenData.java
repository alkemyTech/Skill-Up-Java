package com.alkemy.wallet.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class GetTokenData {

    public static Integer getUserIdFromToken(String token) throws ParseException {

        // decodifico y divido el token en header y payload.
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        JSONParser parser = new JSONParser();
        JSONObject payload_json = (JSONObject) parser.parse(payload);

        JSONObject auth_json = (JSONObject) parser.parse(payload_json.get("auth").toString());
        Integer user_id = Integer.parseInt(auth_json.get("id").toString());

        return user_id;

    }

}
