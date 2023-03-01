package com.example.migration.core.enuns;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.EnumSet.allOf;

@Getter
@RequiredArgsConstructor
public enum Organisation {

    NUTMEG("nutmeg","gb","nutmeg"),
    FUBON("fubon","tw","fubon"),
    CHASE_GBR("chase-gbr","gb","chase"),
    CHASE_DE("chase-de","de", "chase"),
    JLP("jlp","gb","jlp");

    private final String code;
    private final String tokenMarket;
    private final String tokenOrg;

    private static final Map<String, Organisation> organisationByCodeLookup =
        allOf(Organisation.class)
            .stream()
            .collect(Collectors.toMap(Organisation::getCode, Function.identity()));

    private static final Map<String, Organisation> organisationByOrgAndMarketLookup =
        allOf(Organisation.class)
            .stream()
            .collect(Collectors.toMap(Organisation::buildOrgAndMarketKey, Function.identity()));

    private static String buildOrgAndMarketKey(Organisation organisation) {
        return buildOrgAndMarketKey(organisation.getTokenOrg(), organisation.getTokenMarket());
    }

    private static String buildOrgAndMarketKey(String org, String market) {
        return org.toLowerCase(Locale.ROOT) + "|" + market.toLowerCase(Locale.ROOT);
    }

    public static Organisation fromCode(String codeValue) {
        return organisationByCodeLookup.get(codeValue.toLowerCase(Locale.ROOT));
    }

    public static Organisation fromOrgAndMarket(String org, String market) {
        return organisationByOrgAndMarketLookup.get(buildOrgAndMarketKey(org, market));
    }

    @Override
    public String toString() {
        return this.getCode();
    }
}
