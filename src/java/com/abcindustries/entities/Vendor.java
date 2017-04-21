/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
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
package com.abcindustries.entities;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author <TylerDrury>
 */
public class Vendor {
    private int vendorId;
    private String name;
    private String contactName;
    private String phoneNumber;

     public Vendor(){
     
     }
    public Vendor(int vendorId, String name, String contactName, String phoneNumber) {
        this.vendorId = vendorId;
        this.name = name;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }
    public Vendor(JsonObject json) {
        this.vendorId = json.getInt("vendorId", 0);
        this.name = json.getString("name", "");
        this.contactName = json.getString("contactName", "");
        this.phoneNumber = json.getString("phoneNumber", "");
    }
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("vendorId", vendorId)
                .add("name", name)
                .add("contactName", contactName)
                .add("phoneNumber", phoneNumber)
                .build();
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
