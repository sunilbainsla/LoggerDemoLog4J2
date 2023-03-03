import org.json.*;

// Parse the JSON string into a JSONObject
String jsonString = "{\"eventsource\":\"topicname\",\"data\":\"{\\\"orgid\\\":\\\"test\\\",\\\"data\\\":{\\\"relationship\\\":{\\\"payment\\\":{\\\"data\\\":[{\\\"id\\\":\\\"123\\\"}]}}}}\"}";
		JSONObject json = new JSONObject(jsonString);

		// Get the "data" object
		JSONObject data = json.getJSONObject("data");

		// Get the "data" object inside the "relationship" object
		JSONObject relationshipData = data.getJSONObject("data").getJSONObject("relationship").getJSONObject("payment").getJSONArray("data").getJSONObject(0);

		// Get the value of the "id" field
		String id = relationshipData.getString("id");

		System.out.println(id); // Output: 123
