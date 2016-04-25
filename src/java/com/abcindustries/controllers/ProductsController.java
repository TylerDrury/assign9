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
 * @author <ENTER YOUR NAME HERE>
 */
@ApplicationScoped
public class ProductsController {

    List<Product> productList;

    public ProductsController() {
        retrieveAllProducts();
    }

    public void retrieveAllProducts() {
        // TODO: Make a call that retrieves all the Products from the Database
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
        // TODO: Make a call that deletes a single Product from the database
    }

    public List<Product> getAll() {
        return productList;
    }

    public JsonArray getAllJson() {
        // TODO: Build a JsonArray object from the List
        return null;
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
        // TODO: Input the JsonObject at the specified id if it already exists
        return null;
    }

    public JsonObject delete(int id) {
        // TODO: Remember to delete from both the List and the DB
        return null;
    }
}
