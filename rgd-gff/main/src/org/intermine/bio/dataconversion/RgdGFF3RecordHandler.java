package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2008 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.metadata.Model;
import org.intermine.xml.full.Item;
import java.util.regex.*;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.intermine.bio.io.gff3.GFF3Record;

/**
 * Handle special cases when converting RGD GFF3 files.
 *
 * @original author Richard Smith
 * @modified by Andrew Vallejos
 */


public class RgdGFF3RecordHandler extends GFF3RecordHandler
{
    /**
     * Create a new RgdGFF3RecordHandler object.
     * @param tgtModel the target Model
     */
	private static Map<String, Integer> iDMap = new HashMap<String, Integer>();
	
    public RgdGFF3RecordHandler(Model tgtModel) {
		super(tgtModel);
    }

    /**
     * {@inheritDoc}
     */
    public void process(GFF3Record record) {
		/*
		String recId = record.getId();
		if(recId == null)
			return;
		
		if(iDMap.containsKey(recId)){
			iDMap.put(recId, iDMap.get(recId) + 1);
			record.setId(recId + ":" + iDMap.get(recId));
		}
		else{
			iDMap.put(recId, 1);
			System.out.println("Adding 1 to " + recId);
			System.out.println(record.getAttributes());
		}
		System.out.println(recId + "is now " + iDMap.get(recId));
		
		try{
			System.in.read();
		}
		catch(IOException e)
		{}
		
		/*
        Item feature = getFeature();

		// String clsName = feature.getClassName();
		if(feature.getAttribute("primaryIdentifier") == null)
			return;
			
		String ftrName = feature.getAttribute("primaryIdentifier").getValue();
		
		if(iDMap.containsKey(ftrName)){
			iDMap.put(ftrName, iDMap.get(ftrName) + 1);
			feature.setAttribute("primaryIdentifier", 
				ftrName + ":" + iDMap.get(ftrName));
		}
		else{
			iDMap.put(ftrName, 1);
			System.out.println("Adding 1 to " + ftrName);
		}
		System.out.println(ftrName + "is now" + iDMap.get(ftrName));
		System.out.println("Size of map: " + iDMap.size());
		*/
    }
    
}
