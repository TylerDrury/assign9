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
package com.abcindustries.rest;

import com.abcindustries.controllers.ProductsController;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author <ENTER YOUR NAME HERE>
 */
@Path("products")
@RequestScoped
public class ProductsREST {

    @Inject
    ProductsController products;

    @GET
    @Produces("application/json")
    public Response getAll() {
        return Response.ok(products.getAllJson()).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") int id) {
       return Response.ok(products.getByIdJson(id)).build();
        
    }
    
    // TODO: Create an @GET block on the /search/{query} path that searches by a query string
    @GET
    @Path("/search/{query}")
    @Produces("application/json")
    public Response getBySearch(@PathParam("query") String query) {
       return Response.ok(products.getBySearchJson(query)).build();      
    }
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(JsonObject json) {
        return Response.ok(products.addJson(json)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response set(@PathParam("id") int id, JsonObject json) {
         return Response.ok(products.editJson(id,json)).build();
        
    }

    @DELETE
    @Path("{id}")
    public Response set(@PathParam("id") int id) {
        return Response.ok(products.delete(id)).build();
    }
}
