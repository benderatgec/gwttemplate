package com.greenenergycorp.gwttemplate.client;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;

import com.greenenergycorp.gwttemplate.client.gwtp.MyGinjector;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwttemplate implements EntryPoint
{
    public static final MyGinjector ginjector = GWT.create( MyGinjector.class );
    private static final Logger LOGGER = Logger.getLogger( gwttemplate.class.getName() );
    private static Label endlabel = new Label();
    private static Label debuglabel = new Label();

    @Override
    public void onModuleLoad()
    {
        RootPanel.get().add( new Label( "Hello World" ) );
        CustomOracle oracle = new CustomOracle();
        SuggestBox box = new SuggestBox( oracle );
        RootPanel.get().add( box );
        RootPanel.get().add( endlabel );
        RootPanel.get().add( debuglabel );
    }

    private static List<String> fields = Lists.newArrayList( "user:", "type:" );
    private static List<String> users = Lists.newArrayList( "adam", "adrian", "chuck", "charles", "dan", "dennis" );
    private static List<String> types = Lists.newArrayList( "open", "operational", "closed", "canceled", "error" );

    private class CustomOracle extends MultiWordSuggestOracle
    {
        @Override
        public void requestSuggestions( final Request request, final Callback callback )
        {
            // LOGGER.info( request.getQuery() );
            Result r = AwesomeSuggestionStateMachine.computeState( request.getQuery() );
            clear();
            if ( r.finalState == AwesomeSuggestionStateMachine.State.FIELD || r.finalState == AwesomeSuggestionStateMachine.State.NEXT_TOKEN )
            {
                addAll( fields );
                request.setQuery( r.tokens.get( r.tokens.size() - 1 ).field );
                super.requestSuggestions( request, callback );
            }
            else if ( r.tokens.get( r.tokens.size() - 1 ).field.endsWith( "user" ) )
            {
                addAll( users );
                request.setQuery( r.tokens.get( r.tokens.size() - 1 ).value );
                super.requestSuggestions( request, callback );
            }
            else
            {
                addAll( types );
                request.setQuery( r.tokens.get( r.tokens.size() - 1 ).value );
                super.requestSuggestions( request, callback );
            }
        }
    }

    private static class AwesomeSuggestionStateMachine
    {
        enum State
        {
            NEXT_TOKEN
            {
                @Override
                public State nextState( char c )
                {
                    switch ( c )
                    {
                        case ' ':
                            return NEXT_TOKEN;
                        case ':':
                            return ERROR;
                        default:
                            return FIELD;
                    }
                }
            },
            FIELD
            {
                @Override
                public State nextState( char c )
                {
                    switch ( c )
                    {
                        case ':':
                            return VALUE;
                        case ' ':
                            return ERROR;
                        default:
                            return FIELD;
                    }
                }
            },
            VALUE
            {
                @Override
                public State nextState( char c )
                {
                    switch ( c )
                    {
                        case ' ':
                            return NEXT_TOKEN;
                        case ':':
                            return ERROR;
                        default:
                            return VALUE;
                    }
                }
            },
            ERROR
            {
                @Override
                public State nextState( char c )
                {
                    return ERROR;
                }
            };

            public abstract State nextState( char c );
        }

        public static Result computeState( String input )
        {
            State currentState = State.NEXT_TOKEN;
            String progression = currentState.name();
            int lastTokenStart = 0;
            int currentIndex = 0;
            List<Token> tokens = Lists.newArrayList();
            for ( char c : input.toCharArray() )
            {
                currentState = currentState.nextState( c );
                progression += ", " + currentState.name();
                if ( currentState == State.ERROR )
                {
                    break;
                }
                else if ( currentState == State.NEXT_TOKEN )
                {
                    tokens.add( parseToken( input.substring( lastTokenStart, currentIndex ).trim() ) );
                    lastTokenStart = currentIndex;
                }
                currentIndex++;
            }
            tokens.add( parseToken( input.substring( lastTokenStart, currentIndex ).trim() ) );
            debuglabel.setText( "State Progression: " + progression );
            return new Result( currentState, tokens );
        }
        // based on token input go to next state, state determines available suggestsions.
    }

    private static Token parseToken( String unparsed )
    {
        String[] splitToken = unparsed.split( ":" );
        String field = splitToken[0];
        String value = splitToken.length == 2 ? splitToken[1] : "";
        return new Token( field, value );
    }

    private static class Result
    {
        private AwesomeSuggestionStateMachine.State finalState;
        private final List<Token> tokens;

        public Result( AwesomeSuggestionStateMachine.State currentState, List<Token> tokens )
        {
            finalState = currentState;
            this.tokens = tokens;
        }

        @Override
        public String toString()
        {
            return "state: " + finalState.name() + " tokens: " + tokens;
        }
    }

    private static class Token
    {
        private String field;
        private String value;

        public Token( String field, String value )
        {
            this.field = field;
            this.value = value;
        }

    }
    // two phase
    // build suggestion list
    // ) using trie for prefix search, state machine to determine next set of suggestions
    // parse query into reef query
    // ) state machines to parse query into tokens useful in querying reef


}
