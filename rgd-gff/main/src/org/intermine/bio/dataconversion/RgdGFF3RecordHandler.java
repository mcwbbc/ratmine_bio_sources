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

		String recordType = record.getType();

		if (featuresMap.get(newName) == null) {
			// new feature
			if("gene".equals(recordType)){
				try{	
					// Parse the attribute column of the GFF file for feature attributes
					// GFF Attribute Name is mapped to the Feature Attribute symbol in the RatMine Gene Model
					String symbol = record.getAttributes().get("Name").get(0);
					feature.setAttribute("symbol", symbol);
					String name = record.getAttributes().get("fullName").get(0);
					feature.setAttribute("name", name);
					String desc = record.getAttributes().get("Note").get(0);
					feature.setAttribute("description", desc);
					String geneType = record.getAttributes().get("geneType").get(0);
					feature.setAttribute("geneType", geneType);

					//pull NCBI Gene Number from xref
					for( String xref : record.getAttributes().get("Dbxref")){
						Matcher mXref = Pattern.compile("NCBIGene:(\\d+)").matcher(xref);
						while(mXref.find()){
							feature.setAttribute("ncbiGeneNumber", mXref.group(1));
						} //if
					} //for attributes
				} catch (NullPointerException e) {
					// not sure what to do with a worthless error System.out.println(e.getMessage());
				} finally {
					featuresMap.put(newName, feature);
				}
			} else if("QTL".equals(recordType)){
				try{
					String lod = record.getAttributes().get("LOD").get(0);
					if(!"null".equals(lod)){ feature.setAttribute("LOD", lod); }
					//String name = record.getAttributes().get("QTL").get(0);
					//feature.setAttribute("name", name);
					feature = getAndSetAttribute(record, "fullName", feature, "description");
				} catch (NullPointerException e){
					// TODO: figure out what to do with a worthless error message
				} finally {
					featuresMap.put(newName, feature);
				}
			
			} else if("SimpleSequenceLengthVariation".equals(recordType)){
				try{
					feature = getAndSetAttribute(record, "name", feature, "symbol");
				} catch (NullPointerException e){
					// TODO: figure out what to do with a worthless error message
				} finally {
					featuresMap.put(newName, feature);
				}
			}
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
		System.out.println("Error parsing feature type " + recordType + " with ID: " + ftrName);
	} else {
		location.removeReference("feature");
        	location.setReference("feature", feature);
	}
    }	

    private Item getAndSetAttribute(GFF3Record record, String gffAttr, Item feature, String featureAttr){
	String rAttr = record.getAttributes().get(gffAttr).get(0);
	feature.setAttribute(featureAttr, rAttr);
	return feature;
    }
    
}
