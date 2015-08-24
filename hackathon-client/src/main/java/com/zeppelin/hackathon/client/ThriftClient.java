package com.zeppelin.hackathon.client;


import com.zeppelin.hackathon.gen.DataService;

public class ThriftClient {
    public DataService.Client client;
    public String ip;

    public ThriftClient(DataService.Client client, String ip) {
        this.client = client;
        this.ip = ip;
    }
}
