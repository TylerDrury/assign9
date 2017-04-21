/*
 * Copyright 2016 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abcindustries.websockets;

import com.abcindustries.controllers.VendorsController;
import java.io.IOException;
import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Tyler Drury
 */
@ServerEndpoint("/vendorsSocket")
@ApplicationScoped
public class VendorsWebSocket {
@Inject
    VendorsController vendors;

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String output = "";
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        if (json.containsKey("get")) {
            if (json.containsKey("id")) {
                 output = vendors.getByIdJson(json.getInt("id")).toString();
            }
            else if (json.getString("get").equals("vendors")) {
                output = vendors.getAllJson().toString();
            }
           else if (json.containsKey("search")) {
                 output = vendors.getBySearchJson(json.getString("search")).toString();
            }
           else output = vendors.getAllJson().toString();
            
        } else if (json.containsKey("post") && json.getString("post").equals("vendors")) {
            JsonObject vendorJson = json.getJsonObject("data");
            vendors.addJson(vendorJson);
            output = vendors.getAllJson().toString();
        }
        else if (json.containsKey("put") && json.getString("put").equals("vendors")) {
            JsonObject vendorJson = json.getJsonObject("data");
            vendors.editJson(vendorJson.getInt("vendorId"),vendorJson);
            output = vendors.getAllJson().toString();
        } 
            else if (json.containsKey("delete") && json.getString("delete").equals("vendors")) {
            int id = json.getInt("id");
            vendors.delete(id);
            output = vendors.getAllJson().toString();
        } 
       
        else {
            output = Json.createObjectBuilder()
                    .add("error", "Invalid Request")
                    .add("original", json)
                    .build().toString();
        }

        session.getBasicRemote().sendText(output);
    }
}
