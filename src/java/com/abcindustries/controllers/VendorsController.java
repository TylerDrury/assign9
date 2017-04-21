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
package com.abcindustries.controllers;


import com.abcindustries.entities.Vendor;
import com.abcindustries.utilities.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author Tyler Drury
 */
@ApplicationScoped
public class VendorsController {
    List<Vendor> vendorList;

    public VendorsController() {
        retrieveAllVendors();
    }

    public void retrieveAllVendors() {
        try {
            vendorList = new ArrayList<>();
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vendors");
            while (rs.next()) {
                Vendor p = new Vendor();
                p.setVendorId(rs.getInt("VendorId"));
                p.setName(rs.getString("Name"));
                p.setContactName(rs.getString("ContactName"));
                p.setPhoneNumber(rs.getString("PhoneNumber"));
                vendorList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persistToDb(Vendor p) {
        try {
            String sql = "";
            Connection conn = DBUtils.getConnection();
            if (p.getVendorId() <= 0) {
                sql = "INSERT INTO Vendors (Name, ContactName, PhoneNumber) VALUES (?, ?, ?)";
            } else {
                sql = "UPDATE Vendors SET Name = ?, ContactName = ?,PhoneNumber = ? WHERE VendorId = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getContactName());
            pstmt.setString(3, p.getPhoneNumber());
            if (p.getVendorId() > 0) {
                pstmt.setInt(4, p.getVendorId());
            }
            pstmt.executeUpdate();
            if (p.getVendorId() <= 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                p.setVendorId(id);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFromDb(Vendor p) {
       try {
            String sql = "DELETE FROM Vendors WHERE VendorId = ?";
            Connection conn = DBUtils.getConnection();        
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, p.getVendorId());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(VendorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Vendor> getAll() {
        return vendorList;
    }

    public JsonArray getAllJson() {
       JsonArrayBuilder arr = Json.createArrayBuilder();
         for (Vendor p : vendorList) {
         arr.add(p.toJson());
         }
         
        return arr.build();
    }

    public Vendor getById(int id) {
        Vendor result = null;
        for (Vendor p : vendorList) {
            if (p.getVendorId() == id) {
                result = p;
            }
        }
        return result;
    }

    public JsonObject getByIdJson(int id) {
        return getById(id).toJson();
    }

    public JsonArray getBySearchJson(String query) {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Vendor p : vendorList) {
            if (p.getName().contains(query)) {
                json.add(p.toJson());
            }
        }
        return json.build();
    }

    public JsonObject addJson(JsonObject json) {
        Vendor p = new Vendor(json);
        persistToDb(p);
        vendorList.add(p);
        return p.toJson();
    }

    public JsonObject editJson(int id, JsonObject json) {
        Vendor p = getById(id);
        p.setName(json.getString("name"));
        p.setContactName(json.getString("contactName"));
        p.setPhoneNumber(json.getString("phoneNumber"));
        persistToDb(p);
        return null;
    }

    public JsonObject delete(int id) {
         Vendor p = getById(id);
         removeFromDb(p);
         vendorList.remove(p);
        return null;
    }
}
