package org.prebid.server.spring.config.bidder;

import org.prebid.server.bidder.BidderDeps;
import org.prebid.server.bidder.tripleliftnative.TripleliftNativeBidder;
import org.prebid.server.json.JacksonMapper;
import org.prebid.server.spring.config.bidder.model.BidderConfigurationProperties;
import org.prebid.server.spring.config.bidder.util.BidderDepsAssembler;
import org.prebid.server.spring.config.bidder.util.UsersyncerCreator;
import org.prebid.server.spring.env.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotBlank;

@Configuration
@PropertySource(value = "classpath:/bidder-config/tripleliftnative.yaml", factory = YamlPropertySourceFactory.class)
public class TripleliftNativeConfiguration {

    private static final String BIDDER_NAME = "triplelift_native";

    @Bean("tripleliftNativeConfigurationProperties")
    @ConfigurationProperties("adapters.tripleliftnative")
    BidderConfigurationProperties configurationProperties() {
        return new BidderConfigurationProperties();
    }

    @Bean
    BidderDeps tripleliftNativeBidderDeps(BidderConfigurationProperties tripleliftNativeConfigurationProperties,
                                          @NotBlank @Value("${external-url}") String externalUrl,
                                          JacksonMapper mapper) {

        return BidderDepsAssembler.forBidder(BIDDER_NAME)
                .withConfig(tripleliftNativeConfigurationProperties)
                .usersyncerCreator(UsersyncerCreator.create(externalUrl))
                .bidderCreator(config ->
                        new TripleliftNativeBidder(
                                config.getEndpoint(),
                                config.getExtraInfo(),
                                mapper))
                .assemble();
    }
}
