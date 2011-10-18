package com.greenenergycorp.gwttemplate.server.gwtp;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

import com.greenenergycorp.gwttemplate.server.gwtp.handler.SendTextToServerHandler;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServer;


public class ServerModule extends HandlerModule
{
    @Override
    protected void configureHandlers()
    {
        bindHandler( SendTextToServer.class, SendTextToServerHandler.class );
    }
}
