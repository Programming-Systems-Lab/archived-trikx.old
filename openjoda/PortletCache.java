/*
 *
 * Copyright (c) 1998 The Java Apache Project.  All rights reserved.
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
 * 3. Every modification must be notified to the Java Apache Project
 *    and redistribution of the modified code without prior notification
 *    is not permitted in any form.
 *
 * 4. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache Project
 *    (http://java.apache.org/)."
 *
 * 5. The names "Jetspeed", "Apache Jetspeed" and "Apache Jetspeed
 *    Project" must not be used to endorse or promote products
 *    derived from this software without prior written permission.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache Project
 *    (http://java.apache.org/)."
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
 * License version 1.0
 *
 */

package org.apache.jetspeed.cache.memory;

//java stuff
import java.io.*;
import java.util.*;

//jetspeed stuff
import org.apache.jetspeed.portal.*;
import org.apache.jetspeed.util.*;
import org.apache.jetspeed.registry.peer.*;

//turbine stuff
import org.apache.turbine.util.*;


/**
Stores PortletControls in memory so performance is optimized.

@author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
@version $Id$
*/
public class PortletCache extends BaseSingleton {

    public static final String SINGLETON_HANDLE = "jetspeed.portletcache";
    

    /**
    Singleton instance
    */
    private static PortletCache instance = null;
    
    /**
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public static PortletCache getInstance() {

        if ( instance == null ) {
            instance = (PortletCache)SingletonHolder.get( SINGLETON_HANDLE );
        }
        
        return instance;

    }

    /**
    Perform any initialization needed by the PortletCache.
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public void init() { 

        this.setSingletonHandle( SINGLETON_HANDLE );
        
        SingletonHolder.put( this );    

        this.setInitialized( true );
    }
    
    /**
    Add a Cacheable to the cache.
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public void addCacheable( Cacheable item ) {

        String handle = item.getHandle();

        if ( handle.length() == 0 ) {
            throw new RuntimeException("You must specify a handle for the item you want to cache.");
        }
        
        if ( item.isCacheable() ) {
            //MemoryStore.getInstance().hold( handle, item);
        }

    }
    
    /**
    Get a Cacheable object from the cache.
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public Cacheable getCacheable( String handle ) {

        Cacheable item = (Cacheable)MemoryStore.getInstance().get( handle );

        if ( item == null ) {
            Log.note( "Jetspeed: cache miss: " + handle );
        } else {
            Log.note( "Jetspeed: cache hit: " + handle );                
        }
        
        return item;

    }

    /**
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public void swap( Cacheable original, Cacheable replacement ) {

        MemoryStore.getInstance().remove( original.getHandle() );

        MemoryStore.getInstance().hold( replacement.getHandle(), replacement );
        
    }
    
    /**
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public String getStatus() {
        return MemoryStore.getInstance().getStatus();
    }

    /**
    Get the Portlets that have been stored in memory.  
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public Portlet[] getPortlets() {

        Enumeration enum = MemoryStore.getInstance().list();
        
        Vector v = new Vector();
        
        while ( enum.hasMoreElements() ) {
            
            String key = (String)enum.nextElement();

            Object obj = MemoryStore.getInstance().get( key );
            
            if ( obj instanceof Portlet ) {
                v.addElement( obj );
            }
            
        }
        
        Portlet[] portlets = new Portlet[ v.size() ];
        v.copyInto( portlets );
        return portlets;
        
    }

    /**
    Return true if the given Entry is instantiated and in the memory cache.
    
    @author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
    @version $Id$
    */
    public boolean isCached( PortletEntry entry ) {
        
        String handle = CacheHandleManager.getHandle( entry );
        
        //return this.getCacheable( handle ) != null;
	  return false;
        
    }
    
}

