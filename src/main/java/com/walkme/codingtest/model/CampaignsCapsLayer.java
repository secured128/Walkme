package com.walkme.codingtest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/*
Main business logic module to enforce campaign's validation implemented as singletone to have full control over resources and have single entry point to business logic
 */
public class CampaignsCapsLayer {

    private static class CampaignsCapsLayerHolder {
        public static final CampaignsCapsLayer instance = new CampaignsCapsLayer();
    }

    public static CampaignsCapsLayer getInstance() {
        return CampaignsCapsLayerHolder.instance;
    }

    //one time initialization
    private CampaignsCapsLayer() {
        loadData();
        initCounters();
    }

    //Data structures for storing counters in a mnimal blocking way while still assure data integrity
    Map<Integer, Map<Integer, CountersHolder>> userCounters = new HashMap<>();
    Map<Integer, AtomicInteger> globalCampaignsCountersRef = new HashMap<>();

    public List<Campaign> getCampains(int userId) {

        if (!userCounters.containsKey(userId)) {
            initUserCounters(userId, globalCampaignsCountersRef);
        }

        //synchronization on user level
        synchronized (userCounters.get(userId)) {
            List<Campaign> result = campaigns.values().parallelStream()
                .filter(c -> userCounters.get(userId).get(c.getId()).getUserCampaigsCounter() < c.getCap().getMaxCounterPerUser() && globalCampaignsCountersRef.get(c.getId()).get() < c.getCap().getMaxCount())
                .peek(c -> userCounters.get(userId).get(c.getId()).count(c.getId()))
                .collect(Collectors.toList());
            return result;
        }
    }

    private Map<Integer, Campaign> campaigns = new HashMap<Integer, Campaign>();

    private void initCounters() {
        for (Campaign campaign : campaigns.values()) {
            globalCampaignsCountersRef.put(campaign.getId(), new AtomicInteger(0));
        }
    }

    private synchronized void initUserCounters(int userId, Map<Integer, AtomicInteger> globalCampaignsCountersRef) {
        if (!userCounters.containsKey(userId)) {
            Map<Integer, CountersHolder> countersHolder = new HashMap<>();
            for (int campaignId : globalCampaignsCountersRef.keySet()) {
                countersHolder.put(campaignId, new CountersHolder(globalCampaignsCountersRef));
            }
            userCounters.put(userId, countersHolder);
        }
    }

    private class CountersHolder {

        private AtomicInteger usrCampaignsCounter = new AtomicInteger(0);
        private Map<Integer, AtomicInteger> globalCampaignsCountersRef;

        public CountersHolder(Map<Integer, AtomicInteger> globalCampaignsCountersRef) {
            this.globalCampaignsCountersRef = globalCampaignsCountersRef;
        }

        public int getUserCampaigsCounter() {
            return usrCampaignsCounter.get();
        }

        public void count(int campaignId) {
            this.usrCampaignsCounter.getAndIncrement();
            this.globalCampaignsCountersRef.get(campaignId).getAndIncrement();
        }
    }

    /*
    imitates loading from data source
    */
    private void loadData() {
        for (int i = 0; i < 10; i++) {
            campaigns.put(i, new Campaign(i, "Campaign N" + i, new Data("some data " + i), new Cap.CapBuilder().setMaxCount(10 - i).setMaxCounterPerUser(i).build()));
            campaigns.put(i + 10, new Campaign(i + 10, "Campaign N" + i + 10, new Data("some data " + i + 10), new Cap.CapBuilder().setMaxCount(i).setMaxCounterPerUser(10 - i).build()));
            campaigns.put(i + 200, new Campaign(i + 200, "Campaign N" + i + 200, new Data("some data " + i + 200), new Cap.CapBuilder().setMaxCount(10 - i).build()));
            campaigns.put(i + 300, new Campaign(i + 300, "Campaign N" + i + 300, new Data("some data " + i + 300), new Cap.CapBuilder().setMaxCounterPerUser(10 - i).build()));
            campaigns.put(i + 400, (new Campaign(i + 400, "Campaign N" + i + 400, new Data("some data " + i + 400), new Cap.CapBuilder().setMaxCount(i).build())));
            campaigns.put(i + 500, new Campaign(i + 500, "Campaign N" + i + 500, new Data("some data " + i + 500), new Cap.CapBuilder().setMaxCounterPerUser(i).build()));
        }
        campaigns.put(100, new Campaign(100, "Campaign N" + 100, new Data("some data " + 100), new Cap.CapBuilder().build()));
    }

}


