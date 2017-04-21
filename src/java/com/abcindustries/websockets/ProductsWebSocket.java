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

import com.abcindustries.controllers.ProductsController;
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
 * @author <ENTER YOUR NAME HERE>
 */
@ServerEndpoint("/productsSocket")
@ApplicationScoped
public class ProductsWebSocket {

    @Inject
    ProductsController products;

    // TODO: Inject the VendorsController as well
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String output = "";
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        if (json.containsKey("get")) {
            if (json.containsKey("id")) {
                 output = products.getByIdJson(json.getInt("id")).toString();
            }
            else if (json.getString("get").equals("products")) {
                output = products.getAllJson().toString();
            }
           else if (json.containsKey("search")) {
                 output = products.getBySearchJson(json.getString("search")).toString();
            }
           else output = products.getAllJson().toString();
            //get byid ,get by search,
           
            
        } else if (json.containsKey("post") && json.getString("post").equals("products")) {
            JsonObject productJson = json.getJsonObject("data");
            products.addJson(productJson);
            output = products.getAllJson().toString();
        }
        else if (json.containsKey("put") && json.getString("put").equals("products")) {
            JsonObject productJson = json.getJsonObject("data");
            products.editJson(productJson.getInt("productId"),productJson);
            output = products.getAllJson().toString();
        } 
            else if (json.containsKey("delete") && json.getString("delete").equals("products")) {
            int id = json.getInt("id");
            products.delete(id);
            output = products.getAllJson().toString();
        } // TODO: Capture all the other messages defined on the WebSockets API
       
        else {
            output = Json.createObjectBuilder()
                    .add("error", "Invalid Request")
                    .add("original", json)
                    .build().toString();
        }

        session.getBasicRemote().sendText(output);
    }

}
