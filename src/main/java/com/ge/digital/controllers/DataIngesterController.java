package com.ge.digital.controllers;

import com.ge.digital.model.DataPayload;
import com.ge.digital.model.DefaultResponse;
import com.ge.predix.timeseries.client.Client;
import com.ge.predix.timeseries.client.ClientFactory;
import com.ge.predix.timeseries.client.TenantContext;
import com.ge.predix.timeseries.client.TenantContextFactory;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.builder.IngestionRequestBuilder;
import com.ge.predix.timeseries.model.builder.IngestionTag;
import com.ge.predix.timeseries.model.datapoints.DataPoint;
import com.ge.predix.timeseries.model.datapoints.Quality;
import com.ge.predix.timeseries.model.response.IngestionResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


@RestController
public class DataIngesterController extends RestApiController{

    @Autowired
    private TenantContext tenantContext;

    @ApiOperation(value = "Ingest Data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token authorization retrieved from predix uaa.", required = true, dataType = "string", paramType = "header", example = "Bearer 68e34344e5-d485-466a-b55f-0324fe345rg3445")})
    @RequestMapping(value = "/ingest", method = RequestMethod.POST)
    public DefaultResponse ingesterData(@RequestBody DataPayload data) throws IOException {
        IngestionRequestBuilder ingestionRequestBuilder = IngestionRequestBuilder.createIngestionRequest().withMessageId(UUID.randomUUID().toString())
                .addIngestionTag(IngestionTag.Builder.createIngestionTag().withTagName(data.getTagName())
                        .addDataPoints(Collections.singletonList(new DataPoint(new Date().getTime(), data.getValue(), Quality.GOOD))).build());

        try {
            String json = ingestionRequestBuilder.build().get(0);
            IngestionResponse response = getClient().ingest(json);
            String responseString = response.getMessageId() + response.getStatusCode();
            return new DefaultResponse(responseString);

        } catch (PredixTimeSeriesException e) {
            e.printStackTrace();
            return new DefaultResponse(Arrays.toString(e.getStackTrace()));
        }
    }

    public Client getClient() throws PredixTimeSeriesException {
        return ClientFactory.ingestionClientForTenant(tenantContext);
    }

    @Bean
    public TenantContext tenantContextFactory() throws IOException, PredixTimeSeriesException, URISyntaxException {
        return TenantContextFactory.createTenantContextFromPropertiesFile("/predix-timeseries.properties");
    }

}
