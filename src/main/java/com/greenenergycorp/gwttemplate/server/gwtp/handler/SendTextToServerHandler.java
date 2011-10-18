package com.greenenergycorp.gwttemplate.server.gwtp.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import com.greenenergycorp.gwttemplate.shared.gwtp.FieldVerifier;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServer;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServerResult;

public class SendTextToServerHandler implements ActionHandler<SendTextToServer, SendTextToServerResult>
{

    private Provider<HttpServletRequest> requestProvider;
    private ServletContext servletContext;

    @Inject
    SendTextToServerHandler( ServletContext servletContext, Provider<HttpServletRequest> requestProvider )
    {
        this.servletContext = servletContext;
        this.requestProvider = requestProvider;
    }

    @Override
    public SendTextToServerResult execute( SendTextToServer action, ExecutionContext context ) throws ActionException
    {

        String input = action.getTextToServer();

        // Verify that the input is valid.
        if ( !FieldVerifier.isValidName( input ) )
        {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new ActionException( "Name must be at least 4 characters long" );
        }

        String serverInfo = servletContext.getServerInfo();
        String userAgent = requestProvider.get().getHeader( "User-Agent" );
        return new SendTextToServerResult( "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
            + userAgent );
    }

    @Override
    public Class<SendTextToServer> getActionType()
    {
        return SendTextToServer.class;
    }

    @Override
    public void undo( SendTextToServer action, SendTextToServerResult result, ExecutionContext context ) throws ActionException
    {
        // Not undoable
    }

}