package com.diplom.services;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.model.Connection;
import com.bigchaindb.model.GenericCallback;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionService {

    @Autowired
    TransactionService transactionService;

    public void setConfig() {

        //define connections
        Map<String, Object> conn1Config = new HashMap<String, Object>(),
                conn2Config = new HashMap<String, Object>();
        Map<String, Object> conn3Config = new HashMap<String, Object>(),
                conn4Config = new HashMap<String, Object>();

        //define headers for connections
        Map<String, String> headers1 = new HashMap<String, String>();
        Map<String, String> headers2 = new HashMap<String, String>();
        Map<String, String> headers3 = new HashMap<String, String>();
        Map<String, String> headers4 = new HashMap<String, String>();

        //config header for connection 1
        headers1.put("app_id", "");
        headers1.put("app_key", "");
        headers2.put("app_id", "");
        headers2.put("app_key", "");
        headers3.put("app_id", "");
        headers3.put("app_key", "");
        headers4.put("app_id", "");
        headers4.put("app_key", "");

        //config connection 1
        conn1Config.put("baseUrl", "http://192.168.56.104:9984/");
        conn1Config.put("headers", headers1);
        Connection conn1 = new Connection(conn1Config);

        //config connection 2
        conn2Config.put("baseUrl", "http://192.168.56.105:9984/");
        conn2Config.put("headers", headers2);
        Connection conn2 = new Connection(conn2Config);

        //config connection 1
        conn3Config.put("baseUrl", "http://192.168.56.106:9984/");
        conn3Config.put("headers", headers3);
        Connection conn3 = new Connection(conn3Config);

        //config connection 2
        conn4Config.put("baseUrl", "http://192.168.56.107:9984/");
        conn4Config.put("headers", headers4);
        Connection conn4 = new Connection(conn4Config);

        //add connections
        List<Connection> connections = new ArrayList<Connection>();
        connections.add(conn1);
        connections.add(conn2);
        connections.add(conn3);
        connections.add(conn4);

        BigchainDbConfigBuilder
                .addConnections(connections)
                .setTimeout(60000) //override default timeout of 20000 milliseconds
                .setup();

        System.out.println("connection configured");
    }


    public GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                System.out.println("malformed " + response.message());
//                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                System.out.println("pushedSuccessfully");
//                onSuccess(response);
            }

            @Override
            public void otherError(Response response) {
                System.out.println("otherError" + response.message());
//                onFailure();
            }
        };

        return callback;
    }
}
