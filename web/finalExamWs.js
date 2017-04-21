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

/*=================================================================
 * Warning. This file is fine. You should not need to change this *
 * file at all to complete this project. If you think you do, ask *
 * the instructor about what you're planning to do.               *
 =================================================================*/

$(document).ready(function () {
    var productsWsUri = "ws://localhost:8080/CSD4464-Assign9/productsSocket";
    var pws = new WebSocket(productsWsUri);
    var vendorsWsUri = "ws://localhost:8080/CSD4464-Assign9/vendorsSocket";
    var vws = new WebSocket(vendorsWsUri);

    pws.onmessage = function (evt) {
        if (typeof evt.data !== 'undefined') {
            var data = $.parseJSON(evt.data);
            if (Array.isArray(data)) {
                var result = '<tbody>';
                result += "<tr><th>Product ID</th><th>Name</th><th>Vendor ID</th></tr>";
                for (var i = 0; i < data.length; i++)
                    result += '<tr><td>' + data[i].productId + '</td><td>' + data[i].name + '</td><td>' + data[i].vendorId + '</td></tr>';
                result += '</tbody>';
                $('#products').html(result);
            } else {                
                $('#productName').val(data.name);
                $('#productVendor').val(data.vendorId);            
            }
        }
    };
    vws.onmessage = function (evt) {
        if (typeof evt.data !== 'undefined') {
            var data = $.parseJSON(evt.data);
            if (Array.isArray(data)) {
                var result = '<tbody>';
                result += "<tr><th>Vendor ID</th><th>Name</th><th>Contact Name</th><th>Phone Number</th></tr>";
                for (var i = 0; i < data.length; i++)
                    result += '<tr><td>' + data[i].vendorId + '</td><td>' +
                            data[i].name + '</td><td>' + data[i].contactName +
                            '</td><td>' + data[i].phoneNumber + '</td></tr>';
                result += '</tbody>';
                $('#vendors').html(result);
            }
            else {                
                $('#vendorName').val(data.name);
                $('#vendorContact').val(data.contactName);
                $('#vendorPhone').val(data.phoneNumber);            
            }
        }
    };
    pws.onopen = function (evt) {
        pws.send(JSON.stringify({"get": "products"}));
    }

    vws.onopen = function (evt) {
        vws.send(JSON.stringify({"get": "vendors"}));
    }
    $('#findProductById').click(function () {
        pws.send(JSON.stringify({"get": "products", "id": parseInt($('#productId').val())}));
    });
    $('#findProductByQuery').click(function () {
        pws.send(JSON.stringify({"get": "products", "search": $('#productQuery').val()}));
    });
    $('#deleteProductById').click(function () {
        pws.send(JSON.stringify({"delete": "products", "id": parseInt($('#productId').val())}));
    });
    $('#addProduct').click(function () {
        pws.send(JSON.stringify({post: "products",
            data: {
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }}));
    });
    $('#setProduct').click(function () {
        pws.send(JSON.stringify({put: "products",
            data: {
                productId: parseInt($('#productId').val()),
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }}));
    });
    $('#findVendorById').click(function () {
        vws.send(JSON.stringify({"get": "vendors", "id": parseInt($('#vendorId').val())}));
    });
    $('#findVendorByQuery').click(function () {
        vws.send(JSON.stringify({"get": "vendors", "search": $('#vendorQuery').val()}));
    });
    $('#deleteVendorById').click(function () {
        vws.send(JSON.stringify({"delete": "vendors", "id": parseInt($('#vendorId').val())}));
    });
    $('#addVendor').click(function () {
        vws.send(JSON.stringify({post: "vendors",
            data: {
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }}));
    });
    $('#setVendor').click(function () {
        vws.send(JSON.stringify({put: "vendors",
            data: {
                vendorId: parseInt($('#vendorId').val()),
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }}));
    });
});