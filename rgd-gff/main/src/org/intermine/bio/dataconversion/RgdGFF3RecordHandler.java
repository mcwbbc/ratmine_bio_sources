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
	
    public RgdGFF3RecordHandler(Model tgtModel) {
		super(tgtModel);
		 // create a map of classname to reference name for parent references
        refsAndCollections = new HashMap<String, String>();
        refsAndCollections.put("Exon", "gene");
        refsAndCollections.put("CDS", "transcript");
		refsAndCollections.put("MRNA", "gene");
		
		//Map<String, Item> featuresMap = new HashMap<String, Item>();
    }

	Map<String, Item> featuresMap = new HashMap<String, Item>();

    /**
     * {@inheritDoc}
     */

     public void process(GFF3Record record) {

		Item feature = getFeature();

		if(feature.getAttribute("primaryIdentifier") == null) {
			return;
		}
		String ftrName = feature.getAttribute("primaryIdentifier").getValue();
		Matcher matcher = Pattern.compile("RGD").matcher(ftrName);
		String newName = matcher.replaceAll("");

		if (featuresMap.get(newName) == null) {
		    // new feature
			featuresMap.put(newName, feature);
		} else {
		    // we've already seen this feature

            	    // remove the current duplicate feature from items so it doesn't get stored
            	    removeFeature();

		    // reset the feature we're referring to to be the original feature item
            	    feature = featuresMap.get(newName);
		}

        // location refers to feature object - either new feature or original one retrieved from map
        Item location = getLocation();
        if(location == null)
	{
		System.out.println("Error parsing feature with ID: " + ftrName);
	} else {
		location.removeReference("feature");
        	location.setReference("feature", feature);
	}
    }	

    /*
    public void process(GFF3Record record) {
	
		Item feature = getFeature();
		
		if(feature.getAttribute("primaryIdentifier") == null)
			return;

		String ftrName = feature.getAttribute("primaryIdentifier").getValue();

		Matcher matcher = Pattern.compile("RGD").matcher(ftrName);

		String newName = matcher.replaceAll("");

		feature.setAttribute("primaryIdentifier", newName);
        System.out.println(newName);

		if(featuresMap.get(newName) == null)
		{
			featuresMap.put(newName, feature);
		}
        removeFeature();

        //System.out.println(featuresMap.get(newName));
        Item location = getLocation();
        location.removeReference("feature");
        location.setReference("feature", featuresMap.get(newName));
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

		// String clsName = feature.getClassName();
					
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

    }
    */
    
}
