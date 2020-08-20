/*
 * Copyright © 2018 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.util.env.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonPropertyOrder({"chainId", "active", "defaultPeers", "wellKnownPeers", "blacklistedPeers", "name", "description", "symbol",
    "prefix", "project", "genesisLocation", "featuresHeightRequirement", "blockchainProperties", "decimals"})
public class Chain {
    private UUID chainId;
    private boolean active;
    private List<String> defaultPeers;
    private List<String> wellKnownPeers;
    private List<String> blacklistedPeers;
    private String name;
    private String description;
    private String symbol;
    private String prefix;
    private String project;
    private String genesisLocation;
    private FeaturesHeightRequirement featuresHeightRequirement;
    private Map<Integer, BlockchainProperties> blockchainProperties;
    private int decimals;
    private long oneAPL;

    @JsonCreator
    public Chain(@JsonProperty("chainId") UUID chainId,
                 @JsonProperty("wellKnownPeers") List<String> wellKnownPeers,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("symbol") String symbol,
                 @JsonProperty("prefix") String prefix,
                 @JsonProperty("project") String project,
                 @JsonProperty("genesisLocation") String genesisLocation,
                 @JsonProperty("blockchainProperties") List<BlockchainProperties> blockchainProperties,
                 @JsonProperty("decimals") int decimals
                 ) {
        this(chainId, false, Collections.emptyList(), wellKnownPeers, Collections.emptyList(), name, description, symbol, prefix, project, genesisLocation,
            blockchainProperties, null, decimals);
    }

    /**
     *
     * @param chainId
     * @param active
     * @param defaultPeers
     * @param wellKnownPeers
     * @param blacklistedPeers
     * @param name
     * @param description
     * @param symbol
     * @param prefix
     * @param project
     * @param genesisLocation
     * @param blockchainProperties
     * @param featuresHeightRequirement
     * @param decimals
     */
    public Chain(UUID chainId,
                 boolean active,
                 List<String> defaultPeers,
                 List<String> wellKnownPeers,
                 List<String> blacklistedPeers,
                 String name,
                 String description,
                 String symbol,
                 String prefix,
                 String project,
                 String genesisLocation,
                 List<BlockchainProperties> blockchainProperties,
                 FeaturesHeightRequirement featuresHeightRequirement,
                 int decimals
    ) {
        this.chainId = chainId;
        this.active = active;
        this.defaultPeers = defaultPeers;
        this.wellKnownPeers = wellKnownPeers;
        this.blacklistedPeers = blacklistedPeers;
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.prefix = prefix;
        this.project = project;
        this.genesisLocation = genesisLocation;
        this.blockchainProperties =
            blockchainProperties
                .stream()
                .sorted(Comparator.comparingInt(BlockchainProperties::getHeight))
                .collect(
                    Collectors.toMap(BlockchainProperties::getHeight, bp -> bp,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        this.featuresHeightRequirement = featuresHeightRequirement;
        this.decimals = decimals;
        this.oneAPL = 10^decimals;
    }

    /**
     *
     * @param chainId
     * @param active
     * @param defaultPeers
     * @param wellKnownPeers
     * @param blacklistedPeers
     * @param name
     * @param description
     * @param symbol
     * @param prefix
     * @param project
     * @param genesisLocation
     * @param blockchainProperties
     * @param featuresHeightRequirement
     */
    public Chain(UUID chainId,
                 boolean active,
                 List<String> defaultPeers,
                 List<String> wellKnownPeers,
                 List<String> blacklistedPeers,
                 String name,
                 String description,
                 String symbol,
                 String prefix,
                 String project,
                 String genesisLocation,
                 List<BlockchainProperties> blockchainProperties,
                 FeaturesHeightRequirement featuresHeightRequirement)
    {
        this(chainId, active, defaultPeers, wellKnownPeers, blacklistedPeers, name, description, symbol, prefix, project, genesisLocation, blockchainProperties,
                 featuresHeightRequirement, 8);
    }
    
    /**
     *
     * @param chainId
     * @param active
     * @param defaultPeers
     * @param wellKnownPeers
     * @param blacklistedPeers
     * @param name
     * @param description
     * @param symbol
     * @param prefix
     * @param project
     * @param genesisLocation
     * @param blockchainProperties
     */
    public Chain(UUID chainId,
                 boolean active,
                 List<String> defaultPeers,
                 List<String> wellKnownPeers,
                 List<String> blacklistedPeers,
                 String name,
                 String description,
                 String symbol,
                 String prefix,
                 String project,
                 String genesisLocation,
                 List<BlockchainProperties> blockchainProperties)
    {
        this(chainId, active, defaultPeers, wellKnownPeers, blacklistedPeers, name, description, symbol, prefix, project, genesisLocation, blockchainProperties,
                 new FeaturesHeightRequirement(), 8);
    }
    
    /**
     *
     * @param chainId
     * @param defaultPeers
     * @param wellKnownPeers
     * @param blacklistedPeers
     * @param name
     * @param description
     * @param symbol
     * @param prefix
     * @param project
     * @param genesisLocation
     * @param blockchainProperties
     */
    public Chain(UUID chainId,
                 List<String> defaultPeers,
                 String name,
                 String description,
                 String symbol,
                 String prefix,
                 String project,
                 String genesisLocation,
                 List<BlockchainProperties> blockchainProperties)
    {
        this(chainId, true, defaultPeers, new ArrayList<>(), new ArrayList<>(), name, description, symbol, prefix, project, genesisLocation, blockchainProperties,
                 new FeaturesHeightRequirement(), 8);
    }
    
    public Chain() {
    }

    public FeaturesHeightRequirement getFeaturesHeightRequirement() {
        return featuresHeightRequirement;
    }

    public void setFeaturesHeightRequirement(FeaturesHeightRequirement featuresHeightRequirement) {
        this.featuresHeightRequirement = featuresHeightRequirement;
    }

    public UUID getChainId() {
        return chainId;
    }

    public void setChainId(UUID chainId) {
        this.chainId = chainId;
    }

    public List<String> getDefaultPeers() {
        return defaultPeers;
    }

    public void setDefaultPeers(List<String> defaultPeers) {
        this.defaultPeers = defaultPeers;
    }

    public List<String> getWellKnownPeers() {
        return wellKnownPeers;
    }

    public void setWellKnownPeers(List<String> wellKnownPeers) {
        this.wellKnownPeers = wellKnownPeers;
    }

    public List<String> getBlacklistedPeers() {
        return blacklistedPeers;
    }

    public void setBlacklistedPeers(List<String> blacklistedPeers) {
        this.blacklistedPeers = blacklistedPeers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getGenesisLocation() {
        return genesisLocation;
    }

    public void setGenesisLocation(String genesisLocation) {
        this.genesisLocation = genesisLocation;
    }
    
    public int getDecimals()
    {
        return this.decimals;
    }
    
    public void setDecimals(int decimals)
    {
        this.decimals = decimals;
        this.oneAPL = 10^decimals;
    }
    public long getOneAPL()
    {
        return this.oneAPL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chain)) return false;
        Chain chain = (Chain) o;
        return active == chain.active /*&&
            Objects.equals(chainId, chain.chainId) &&
            Objects.equals(defaultPeers, chain.defaultPeers) &&
            Objects.equals(wellKnownPeers, chain.wellKnownPeers) &&
            Objects.equals(blacklistedPeers, chain.blacklistedPeers) &&
            Objects.equals(name, chain.name) &&
            Objects.equals(description, chain.description) &&
            Objects.equals(symbol, chain.symbol) &&
            Objects.equals(prefix, chain.prefix) &&
            Objects.equals(project, chain.project) &&
            Objects.equals(genesisLocation, chain.genesisLocation) &&
            Objects.equals(blockchainProperties, chain.blockchainProperties) &&
            Objects.equals(featuresHeightRequirement, chain.featuresHeightRequirement)*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainId, active, defaultPeers, wellKnownPeers, blacklistedPeers, name, description, symbol, prefix, project, genesisLocation, blockchainProperties, featuresHeightRequirement);
    }

    public Chain copy() {
        List<String> defaultPeersCopy = new ArrayList<>(defaultPeers);
        List<String> wellKnownPeersCopy = new ArrayList<>(wellKnownPeers);
        List<String> blacklistedPeersCopy = new ArrayList<>(blacklistedPeers);
        List<BlockchainProperties> blockchainPropertiesCopy = blockchainProperties.values().stream().map(BlockchainProperties::copy).collect(Collectors.toList());
        return new Chain(chainId, active, defaultPeersCopy, wellKnownPeersCopy, blacklistedPeersCopy, name, description, symbol, prefix, project,
            genesisLocation, blockchainPropertiesCopy, featuresHeightRequirement != null ? featuresHeightRequirement.copy() : null, decimals);
    }

    @Override
    public String toString() {
        return "Chain{" +
            "chainId=" + chainId +
            ", active=" + active +
            ", defaultPeers=" + defaultPeers +
            ", wellKnownPeers=" + wellKnownPeers +
            ", blacklistedPeers=" + blacklistedPeers +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", symbol='" + symbol + '\'' +
            ", prefix='" + prefix + '\'' +
            ", project='" + project + '\'' +
            ", genesisLocation='" + genesisLocation + '\'' +
            ", featuresHeightRequirement='" + featuresHeightRequirement + '\'' +
            ", blockchainProperties=" + blockchainProperties +
            ", decimals='" + decimals
            + '}';
    }

    @JsonGetter("blockchainProperties")
    public List<BlockchainProperties> getBlockchainPropertiesList() {
        return new ArrayList<>(blockchainProperties.values());
    }

    public Map<Integer, BlockchainProperties> getBlockchainProperties() {
        return blockchainProperties;
    }

    public void setBlockchainProperties(Map<Integer, BlockchainProperties> blockchainProperties) {
        this.blockchainProperties = blockchainProperties;
    }
}
