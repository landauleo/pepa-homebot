package com.landauleo.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.landauleo.PepaWebhookBot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/free-message-sender")
@Produces("*/*")
@Consumes(value = "application/json")
@ApplicationScoped
public class RealtyMessageResource {

    @Inject
    PepaWebhookBot bot;

    @POST
    public void onUpdateReceived() {
        bot.sendMessageForFree();
    }

}
