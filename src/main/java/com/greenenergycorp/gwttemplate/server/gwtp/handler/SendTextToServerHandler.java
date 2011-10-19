package com.greenenergycorp.gwttemplate.server.gwtp.handler;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServer;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServerResult;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SendTextToServerHandler implements ActionHandler<SendTextToServer, SendTextToServerResult>
{

    private Provider<HttpServletRequest> requestProvider;
    private ServletContext servletContext;
    private ArrayList<String> theList = Lists.newArrayList( "apple", "banana", "kiwi", "taco", "table", "kwikset", "beach", "ape" );

    @Inject
    SendTextToServerHandler( ServletContext servletContext, Provider<HttpServletRequest> requestProvider )
    {
        this.servletContext = servletContext;
        this.requestProvider = requestProvider;
    }

    @Override
    public SendTextToServerResult execute( SendTextToServer action, ExecutionContext context ) throws ActionException
    {
        ArrayList<String> list = Lists.newArrayList();
        String query = action.getTextToServer();
        for ( String entry : theList )
        {
            if ( entry.contains( query ) )
                list.add( entry );
        }
        return new SendTextToServerResult( list );
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