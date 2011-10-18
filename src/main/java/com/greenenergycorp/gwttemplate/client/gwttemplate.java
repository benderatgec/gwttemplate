package com.greenenergycorp.gwttemplate.client;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

import com.greenenergycorp.gwttemplate.client.gwtp.MyGinjector;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServer;
import com.greenenergycorp.gwttemplate.shared.gwtp.SendTextToServerResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwttemplate implements EntryPoint
{
    public static final MyGinjector ginjector = GWT.create( MyGinjector.class );

    @Override
    public void onModuleLoad()
    {
        RootPanel.get().add( new Label( "Hello World" ) );
        CustomOracle oracle = new CustomOracle();
        SuggestBox box = new SuggestBox( oracle );
        RootPanel.get().add( box );
    }

    private static class CustomOracle extends SuggestOracle
    {
        @Override
        public void requestSuggestions( final Request request, final Callback callback )
        {
            ginjector.getDispatchAsync().execute( new SendTextToServer( "some text" ), new AsyncCallback<SendTextToServerResult>() {

                @Override
                public void onFailure( Throwable caught )
                {
                }

                @Override
                public void onSuccess( SendTextToServerResult result )
                {
                    List<Suggestion> suggestions = Lists.newArrayList();
                    for ( final String r : result.getResponses() )
                    {
                        suggestions.add( new Suggestion() {

                            @Override
                            public String getReplacementString()
                            {
                                return r + " replaced";
                            }

                            @Override
                            public String getDisplayString()
                            {
                                return r + " display";
                            }
                        } );
                    }
                    callback.onSuggestionsReady( request, new Response( suggestions ) );
                }
            } );

        }
    }

}
