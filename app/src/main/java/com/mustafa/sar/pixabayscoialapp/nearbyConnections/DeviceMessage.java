package com.mustafa.sar.pixabayscoialapp.nearbyConnections;

import com.google.android.gms.nearby.messages.Message;
import com.google.gson.*;

import java.nio.charset.Charset;

/**
 * Created by tatocaster on 2/24/17.
 */

public class DeviceMessage {
    private static final Gson gson = new Gson();

    private final String mUUID;
    private final String mMessageBody;

    /**
     * Builds a new {@link Message} object using a unique identifier.
     */
    public static Message newNearbyMessage(String instanceId, String messageBody) {
        DeviceMessage deviceMessage = new DeviceMessage(instanceId, messageBody);
        return new Message(gson.toJson(deviceMessage).getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Creates a {@code DeviceMessage} object from the string used to construct the payload to a
     * {@code Nearby} {@code Message}.
     */
    public static DeviceMessage fromNearbyMessage(Message message) {
        String nearbyMessageString = new String(message.getContent()).trim();
        return gson.fromJson((new String(nearbyMessageString.getBytes(Charset.forName("UTF-8")))), DeviceMessage.class);
    }

    private DeviceMessage(String uuid, String messageBody) {
        mUUID = uuid;
        mMessageBody = messageBody;
        // TODO(developer): add other fields that must be included in the Nearby Message payload.
    }

    protected String getMessageBody() {
        return mMessageBody;
    }
}