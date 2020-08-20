package br.com.arctica.xone.manager.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.splunk.Args;
import com.splunk.Command;
import com.splunk.HttpException;
import com.splunk.Job;
import com.splunk.ResultsReaderXml;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import br.com.arctica.xone.manager.models.AgentConfig;

@org.springframework.stereotype.Service
public class SplunkService {

    private static final int RESULTS_COUNT = 10;
    private static final int OFFSET = 0;
    private static final String OUTPUT_MODE = "xml";

    private Service connect() {
        Service service = new Service("vmi363673.contaboserver.net", 8089);
        Service.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        String credentials = "admin:as678wag";
        String basicAuthHeader = Base64.encode(credentials.getBytes());
        service.setToken("Basic " + basicAuthHeader);
        return service;
    }

    private Args getOutputArgs() {
        Args outputArgs = new Args();
        outputArgs.put("count", RESULTS_COUNT);
        outputArgs.put("offset", OFFSET);
        outputArgs.put("output_mode", OUTPUT_MODE);
        return outputArgs;
    }

    private Args getQueryArgs() {
        Args queryArgs = new Args();
        queryArgs.put("exec_mode", "blocking");
        return queryArgs;
    }

    private Args getParseArgs() {
        Args parseArgs = new Args("parse_only", true);
        return parseArgs;
    }

    private List<AgentConfig> convertResultsIntoList(InputStream stream) {
        HashMap<String, String> map;
        List<AgentConfig> agentList = new ArrayList<>();
        try {
            ResultsReaderXml resultsReader = new ResultsReaderXml(stream);
            while ((map = resultsReader.getNextEvent()) != null) {
                agentList.add(new AgentConfig(map.get("LOGINCOLABORADOR"), map.get("ENVIAR"), map.get("ATUALIZAR"),
                        map.get("COLETAR"), map.get("VERSAO")));
            }
            resultsReader.close();
        } catch (IOException e) {
            System.out.println("I/O exception: " + e);
        }
        return agentList;
    }

    public List<AgentConfig> getAllUsersConfigs() {
        Service service = connect();
        String query = "|inputlookup UserParameters.csv";

        try {
            service.parse(query, getParseArgs());
        } catch (HttpException e) {
            String detail = e.getDetail();
            Command.error("query '%s' is invalid: %s", query, detail);
        }

        Job job = service.getJobs().create(query, getQueryArgs());

        InputStream stream = null;
        stream = job.getResults(getOutputArgs());

        List<AgentConfig> agentList = convertResultsIntoList(stream);

        job.cancel();
        service.logout();

        return agentList;
    }

    public List<AgentConfig> getUserConfigByLogin(String login) {
        Service service = connect();
        String query = "|inputlookup UserParameters.csv |where LOGINCOLABORADOR=\"" + login + "\"";

        try {
            service.parse(query, getParseArgs());
        } catch (HttpException e) {
            String detail = e.getDetail();
            Command.error("query '%s' is invalid: %s", query, detail);
        }

        Job job = service.getJobs().create(query, getQueryArgs());

        InputStream stream = null;
        stream = job.getResults(getOutputArgs());

        List<AgentConfig> agentList = convertResultsIntoList(stream);

        job.cancel();
        service.logout();

        return agentList;
    }
}