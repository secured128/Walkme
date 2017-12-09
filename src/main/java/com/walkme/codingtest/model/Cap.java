package com.walkme.codingtest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
Cap implemented as Builder Design pattern to enable easy initialisation for different number of optional parameters
 */
public class Cap {

    private int maxCounterPerUser;
    private int maxCount;

    private Cap(CapBuilder capBuilder) {
        this.maxCount = capBuilder.maxCount;
        this.maxCounterPerUser = capBuilder.maxCounterPerUser;
    }

    @JsonProperty("max_counter_per_user")
    public int getMaxCounterPerUser() {
        return maxCounterPerUser;
    }

    @JsonProperty("max_counter")
    public int getMaxCount() {
        return maxCount;
    }

    public static class CapBuilder {

        private Integer maxCounterPerUser = Integer.MAX_VALUE;
        private Integer maxCount = Integer.MAX_VALUE;

        public CapBuilder setMaxCounterPerUser(Integer maxCounterPerUser) {
            this.maxCounterPerUser = maxCounterPerUser;
            return this;
        }

        public CapBuilder setMaxCount(Integer maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Cap build() {
            return new Cap(this);
        }
    }
}
