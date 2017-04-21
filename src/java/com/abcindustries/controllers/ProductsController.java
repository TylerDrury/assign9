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

import com.abcindustries.entities.Product;
import com.abcindustries.utilities.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author <ENTER YOUR NAME HERE>
 */
@ApplicationScoped
public class ProductsController {

    List<Product> productList;

    public ProductsController() {
        retrieveAllProducts();
    }

    public void retrieveAllProducts() {
        try {
            productList = new ArrayList<>();
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products");
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("ProductId"));
                p.setName(rs.getString("Name"));
                p.setVendorId(rs.getInt("VendorId"));
                productList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void persistToDb(Product p) {
        try {
            String sql = "";
            Connection conn = DBUtils.getConnection();
            if (p.getProductId() <= 0) {
                sql = "INSERT INTO Products (Name, VendorId) VALUES (?, ?)";
            } else {
                sql = "UPDATE Products SET Name = ?, VendorId = ? WHERE ProductId = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getVendorId());
            if (p.getProductId() > 0) {
                pstmt.setInt(3, p.getProductId());
            }
            pstmt.executeUpdate();
            if (p.getProductId() <= 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                p.setProductId(id);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFromDb(Product p) {
       try {
            String sql = "DELETE FROM Products WHERE ProductId = ?";
            Connection conn = DBUtils.getConnection();        
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, p.getProductId());
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Product> getAll() {
        return productList;
    }

    public JsonArray getAllJson() {
       JsonArrayBuilder arr = Json.createArrayBuilder();
         for (Product p : productList) {
         arr.add(p.toJson());
         }
         
        return arr.build();
    }

    public Product getById(int id) {
        Product result = null;
        for (Product p : productList) {
            if (p.getProductId() == id) {
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
        for (Product p : productList) {
            if (p.getName().contains(query)) {
                json.add(p.toJson());
            }
        }
        return json.build();
    }

    public JsonObject addJson(JsonObject json) {
        Product p = new Product(json);
        persistToDb(p);
        productList.add(p);
        return p.toJson();
    }

    public JsonObject editJson(int id, JsonObject json) {
        Product p = getById(id);
        p.setName(json.getString("name"));
        p.setVendorId(json.getInt("vendorId"));
        persistToDb(p);
        return null;
    }

    public JsonObject delete(int id) {
         Product p = getById(id);
         removeFromDb(p);
         productList.remove(p);
        return null;
    }
}
