package com.ecity.android.httpforandroid.http;

import java.util.ArrayList;
import java.util.Hashtable;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
public class HttpArrayListService extends HttpBaseService<ArrayList<Hashtable<String, String>>> {

	ArrayList<Hashtable<String, String>> plans;

	public HttpArrayListService(HttpBaseServiceParameters paramTaskParameters) {
		super(paramTaskParameters);
	}

	public HttpArrayListService(HttpBaseServiceParameters paramTaskParameters,
			HttpBaseServiceListener<ArrayList<Hashtable<String, String>>> paramTaskListener) {
		super(paramTaskParameters, paramTaskListener);
	}

	@Override
	public ArrayList<Hashtable<String, String>> execute() {
		try {
			plans = new ArrayList<Hashtable<String, String>>();

			JsonParser jsonParser = HttpRequestJsonParse
					.executeFromMap(this.actionInput.getUrl(), this.actionInput.generateRequestParams());

			if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
				return null;
			}

			String name = "", text = "";
			JsonToken token;

			Hashtable<String, String> current = null;

			while (jsonParser.nextToken() != null) {

				token = jsonParser.getCurrentToken();
				name = jsonParser.getCurrentName();
				text = jsonParser.getText();

				switch (token) {
				case START_OBJECT:
					current = new Hashtable<String, String>();

					break;
				case VALUE_NUMBER_INT:
				case VALUE_STRING:
					current.put(name, text);

					break;
				case END_OBJECT:
					plans.add(current);
					current = null;

					break;
				default:
					break;
				}
			}

		} catch (Exception e) {
		}
		return plans;
	}
}
