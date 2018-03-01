package com.ge.digital.controllers;


import com.ge.digital.model.DataPayload;
import com.ge.digital.model.DefaultResponse;
import com.ge.predix.timeseries.client.Client;
import com.ge.predix.timeseries.exceptions.PredixTimeSeriesException;
import com.ge.predix.timeseries.model.response.IngestionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DataIngesterControllerTest {

    @MockBean
    private DataIngesterController controller;

    @MockBean
    private Client client;

    @Captor
    private ArgumentCaptor<String> ingestionJsonCaptor;

    @Before
    public void setup() throws IOException {
        when(controller.ingesterData(any(DataPayload.class))).thenCallRealMethod();
    }

    @Test
    public void should_add_data_timeseries() throws IOException, PredixTimeSeriesException {
        //given
        DataPayload dataPayload = new DataPayload();
        dataPayload.setTagName("toto");
        dataPayload.setValue(123);
        IngestionResponse ingestionResponse = new IngestionResponse();
        ingestionResponse.setMessageId("messageId");
        ingestionResponse.setStatusCode(200);


        when(controller.getClient()).thenReturn(client);
        when(client.ingest(ingestionJsonCaptor.capture())).thenReturn(ingestionResponse);

        //when
        DefaultResponse defaultResponse = controller.ingesterData(dataPayload);

        //then
        assertThat(defaultResponse).isNotNull();
        assertThat(defaultResponse.getMessage()).isEqualTo(ingestionResponse.getMessageId() + ingestionResponse.getStatusCode());

        String ingestionJson = ingestionJsonCaptor.getValue();
        assertThat(ingestionJson).isNotNull();
        assertThat(ingestionJson).contains("\"name\":\"toto\"");
        assertThat(ingestionJson).contains("123.0");

    }

}
