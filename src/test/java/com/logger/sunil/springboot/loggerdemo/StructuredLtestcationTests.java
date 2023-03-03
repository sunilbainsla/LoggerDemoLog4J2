import net.minidev.json.*;

// Parse the JSON string into a JSONObject
String jsonString = "{\"eventsource\":\"topicname\",\"data\":\"{\\\"orgid\\\":\\\"test\\\",\\\"data\\\":{\\\"relationship\\\":{\\\"payment\\\":{\\\"data\\\":[{\\\"id\\\":\\\"123\\\"}]}}}}\"}";
		JSONObject json = (JSONObject) JSONValue.parse(jsonString);

		// Get the "data" object
		JSONObject data = (JSONObject) json.get("data");

		// Get the "data" object inside the "relationship" object
		JSONObject relationshipData = (JSONObject) data.get("data");
		relationshipData = (JSONObject) relationshipData.get("relationships");
		relationshipData = (JSONObject) relationshipData.get("payment");
		relationshipData = ((JSONArray) relationshipData.get("data")).getJSONObject(0);

		// Get the value of the "id" field
		String id = (String) relationshipData.get("id");

		System.out.println(id); // Output: 123
