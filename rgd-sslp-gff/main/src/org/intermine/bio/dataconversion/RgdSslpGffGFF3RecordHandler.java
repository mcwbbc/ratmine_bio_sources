package org.intermine.bio.dataconversion;

/*
 * Copyright (C) 2002-2009 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.intermine.bio.io.gff3.GFF3Record;
import org.intermine.metadata.Model;
import org.intermine.xml.full.Item;

/**
 * A converter/retriever for the RgdSslpGff dataset via GFF files.
 */

public class RgdSslpGffGFF3RecordHandler extends GFF3RecordHandler
{

	private Map<String, Item> sslpMap = new HashMap<String, Item>();
    /**
     * Create a new RgdSslpGffGFF3RecordHandler for the given data model.
     * @param model the model for which items will be created
     */
    public RgdSslpGffGFF3RecordHandler (Model model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(GFF3Record record) {

		String id = record.get("primaryIdentifier");
		
		if(sslpMap.containsKey(id))
		{
				Item o_record = sslpMap.get(id);
				ArrayList<Item> locations = record.getLocations();
				ArrayList<Item> o_locations = o_record.getLocations();
				
				ArrayList<Item> all_locations = ListUtils.union(locations, o_locations);
				record.setLocations(all_locations);
		}

		sslpMap.put(id, record);
        // This method is called for every line of GFF3 file(s) being read.  Features and their
        // locations are already created but not stored so you can make changes here.  Attributes
        // are from the last column of the file are available in a map with the attribute name as
        // the key.   For example:
        //
        //     Item feature = getFeature();
        //     String symbol = record.getAttributes().get("symbol");
        //     feature.setAttrinte("symbol", symbol);
        //
        // Any new Items created can be stored by calling addItem().  For example:
        // 
        //     String geneIdentifier = record.getAttributes().get("gene");
        //     gene = createItem("Gene");
        //     gene.setAttribute("primaryIdentifier", geneIdentifier);
        //     addItem(gene);
        //
        // You should make sure that new Items you create are unique, i.e. by storing in a map by
        // some identifier. 

    }

}
