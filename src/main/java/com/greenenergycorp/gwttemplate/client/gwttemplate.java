package com.greenenergycorp.gwttemplate.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwttemplate implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        RootPanel.get().add( new Label( "Hello World" ) );
    }
}
