package org.apache.jetspeed.turbine.navigations;

/*
 * Copyright (c) 1997-1999 The Java Apache Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache
 *    Project for use in the Apache JServ servlet engine project
 *    <http://java.apache.org/>."
 *
 * 4. The names "Apache JServ", "Apache JServ Servlet Engine", "Turbine",
 *    "Apache Turbine", "Turbine Project", "Apache Turbine Project" and
 *    "Java Apache Project" must not be used to endorse or promote products
 *    derived from this software without prior written permission.
 *
 * 5. Products derived from this software may not be called "Apache JServ"
 *    nor may "Apache" nor "Apache JServ" appear in their names without
 *    prior written permission of the Java Apache Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache
 *    Project for use in the Apache JServ servlet engine project
 *    <http://java.apache.org/>."
 *
 * THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Java Apache Group. For more information
 * on the Java Apache Project and the Apache JServ Servlet Engine project,
 * please see <http://java.apache.org/>.
 *
 */

// Java Core Classes
import java.io.*;
import java.sql.*;
import java.util.*;

// Java Servlet Classes
import javax.servlet.*;
import javax.servlet.http.*;

// Turbine Modules
import org.apache.turbine.modules.*;

// Turbine Utility Classes
import org.apache.turbine.util.*;
import org.apache.turbine.services.resources.*;

// ECS Classes
import org.apache.ecs.*;
import org.apache.ecs.html.*;

//Jetspeed
import org.apache.jetspeed.util.*;
import org.apache.jetspeed.portal.*;
import org.apache.jetspeed.turbine.screens.portlets.*;

//necessary classes from the registrymarkup castor API that works with the Peer.
import org.apache.jetspeed.registry.peer.PortletEntry;
import org.apache.jetspeed.xml.api.registrymarkup.Parameter;
import org.apache.jetspeed.xml.api.registrymarkup.Security;
import org.apache.jetspeed.xml.api.registrymarkup.Metainfo;


public class JetspeedTopNavigation extends Navigation {

    private static String siteLogo = JetspeedResources.getInstance()
            .getString( JetspeedResources.SITE_HEADER_LOGO_KEY );

    private static String WELCOME = JetspeedResources.getInstance()
            .getString( JetspeedResources.SITE_HEADER_WELCOME_KEY );

    private static String LOGO="";


    private static String loginMessage  = TurbineResources.getString( TurbineResources.LOGIN_MESSAGE );

    private static String loginScreen   = TurbineResources.getString( "screen.login" );

    /**
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public ConcreteElement doBuild( RunData data ) throws Exception {

        return getTopicBar( data );
    }

    /**
    Generate a welcome bar for the user.

    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public ElementContainer getTopicBar( RunData data ) {

        ElementContainer elements = new ElementContainer();

        ElementContainer title = new ElementContainer();

        if (siteLogo == "" ) {
            siteLogo = TurbineResources.getString( "site.header.logo" );
        } else if (siteLogo == "" ) {
            siteLogo = JetspeedResources.getInstance()
            .getString( JetspeedResources.CONTENT_ROOT_URL_KEY ) + "/images/bg_fictive.jpg";
            //.getString( JetspeedResources.CONTENT_ROOT_URL_KEY ) + "/images/jetspeed-logo.gif";
        }

        //if the LOGO is fully specified ("/images/logo.gif") or a full URL (http://) don't touch it.
        if ( siteLogo.charAt( 0 ) != '/' ||
             siteLogo.indexOf( "http://" ) != -1 ) {

            LOGO = JetspeedResources.getInstance()
                .getString( JetspeedResources.CONTENT_ROOT_URL_KEY ) + siteLogo;

        } else {
            LOGO = siteLogo;
        }

        if (WELCOME == "" )
            WELCOME = TurbineResources.getString( "header.welcome.message" );
        if (WELCOME == "")
            WELCOME="Welcome to OpenJoda";

        //title.addElement( WELCOME + " -> " );
        title.addElement( new A( new DynamicURI( data ).toString() ).addElement( "Home" ) );

        elements.addElement( Util.getCSS() );
        Center topic = new Center();
        elements.addElement( topic );

        topic.addElement( new Comment( "BEGIN WELCOME BAR" ));

        Table table = new Table().setWidth("100%")
                                 .setCellPadding(0)
                                 .setBorder(0)
                                 .setCellSpacing(0);

        //holds the body of your current page
        ElementContainer body = new ElementContainer();


        body.addElement( title );
        body.addElement( new BR() );
        
        ConcreteElement admin = this.getPrompt( data, "Admin", "Admin" );

        if ( data.getUser() != null && data.getUser().hasLoggedIn() ) {

            PortletEntry cust = new PortletEntry();
            cust.setName( "Customizer" );

            // Only way to work if cookies are disabled
            //DynamicURI pb = PortletURIManager.getPortletMaxURI( cust, data);

            //ConcreteElement customize = this.getPrompt( pb, "Customize" );
            ConcreteElement layout = this.getPrompt(this.getLayoutPortlet( data ), "Layout" );
            /*ConcreteElement ap = this.getPrompt( this.getApplicationsPortlet( data ), "Applications" );*/
            ConcreteElement logout = this.getPrompt( data, "Home", "LogoutUser", "Logout" );
            ConcreteElement editAccount
                = this.getPrompt( data,
                                  "EditAccount",
                                  "Edit Account (" + data.getUser().getUserName() + ")" );

            Vector v = new Vector();
            //v.addElement( customize );
            v.addElement( admin );
            v.addElement( layout );
            //v.addElement( ap );
            v.addElement( logout );
            v.addElement( editAccount );

            body.addElement( this.getApplications( v ) );

        } else {

            Vector v = new Vector();

            v.addElement( this.getPrompt( data, "Login", loginMessage ) );
			v.addElement( admin );
			
            body.addElement( this.getApplications( v ) );

        }

        body.addElement( Util.getNiceDate() );
        //body.addElement( new HR() );

        table.addElement( new TR()
                            .addElement(new TD().addElement( new IMG( LOGO ) )
                            .addElement(new TD().setWidth("95%").addElement( body ) ) ) );


        topic.addElement( table );

        topic.addElement( new Comment( "END WELCOME BAR" ) );

        return elements;


    }


    /**
    Get a prompt by giving the screen, messsage
    */
    private A getPrompt( RunData data,
                         String screen,
                         String message ) {

        return getPrompt( data, screen, null, message );
    }

    /**
    Get a prompt by giving the screen, action, messsage
    */
    private A getPrompt( RunData data,
                         String screen,
                         String action,
                         String message ) {

        DynamicURI uri = new DynamicURI(data);

        if ( screen != null ) {
            uri.setScreen( screen );
        }

        if ( action != null ) {
            uri.setAction( action );
        }

        //LoginUser should redirect me here to the default action...

        return new A( uri.toString() ).addElement( message );

    }


    /**
    Get a prompt based on the DynamicURI
    */
    private A getPrompt( DynamicURI duri, String message ) {
        return new A( duri.toString() ).addElement( message );
    }

    /**
    Get a list of Applications that Jetspeed should present the user.
    */
    private ConcreteElement getApplications( Vector v ) {

        ElementContainer ec = new ElementContainer();

        ClearElement space = new ClearElement( "&nbsp;&nbsp;&nbsp;" );

        for( int i = 0; i < v.size(); ++i ) {

            ConcreteElement ce = (ConcreteElement)v.elementAt(i);

            ec.addElement( ce );
            ec.addElement( space );
        }

        ec.addElement( new BR() );

        return ec;
    }

    /**
    Given a ConcreteElement get the applications
    */
    private ConcreteElement getApplications( ConcreteElement ce ) {

        Vector v = new Vector();
        v.addElement( ce );

        return getApplications( v );
    }

    /**
    Get a DynamicURI to the Applications Portlet

    */
    private DynamicURI getApplicationsPortlet(RunData data) {
        PortletEntry entry = new PortletEntry();
        entry.setName( "Applications" );

        return PortletURIManager.getPortletMaxURI( entry, data );
    }

    private DynamicURI getLayoutPortlet(RunData data) {
        PortletEntry entry = new PortletEntry();
        entry.setName( "Layout" );

        return PortletURIManager.getPortletMaxURI( entry, data );
    }

}
