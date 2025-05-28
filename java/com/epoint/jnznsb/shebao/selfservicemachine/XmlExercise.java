package com.epoint.jnznsb.shebao.selfservicemachine;

import org.json.XML;
import org.json.JSONObject;
import org.json.JSONArray;

public class XmlExercise
{

    public static JSONArray getSJSONArray(String xmlresult) {
        JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
        JSONObject p = xmlJSONObj.getJSONObject("p");
        JSONArray Sarralist = p.getJSONArray("s");

        return Sarralist;
    }

    public static JSONArray getKJSONArray(String xmlresult) {
        JSONObject xmlJSONObj = XML.toJSONObject(xmlresult);
        JSONObject p = xmlJSONObj.getJSONObject("p");

        JSONArray Darralist = p.getJSONArray("d");
        return Darralist;
    }

}
