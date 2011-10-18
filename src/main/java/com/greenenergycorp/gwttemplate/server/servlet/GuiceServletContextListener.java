package com.greenenergycorp.gwttemplate.server.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.greenenergycorp.gwttemplate.server.gwtp.DispatchServletModule;
import com.greenenergycorp.gwttemplate.server.gwtp.ServerModule;


public class GuiceServletContextListener extends com.google.inject.servlet.GuiceServletContextListener
{

    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( new ServerModule(), new DispatchServletModule() );
    }
}