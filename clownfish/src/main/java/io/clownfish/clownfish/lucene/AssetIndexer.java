/*
 * Copyright 2019 rawdog.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.clownfish.clownfish.lucene;

import io.clownfish.clownfish.beans.AssetList;
import io.clownfish.clownfish.dbentities.CfAsset;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.lucene.index.IndexWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.document.Field;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author rawdog
 */
public class AssetIndexer implements Runnable {
    private final AssetList assetList;
    private final IndexWriter writer;
    private final String media_folder;
    private final Parser parser;
    BodyContentHandler handler;
    HashMap<String, String> metamap;

    public AssetIndexer(IndexWriter writer, AssetList assetList, String media_folder) throws IOException {
        this.assetList = assetList;
        this.writer = writer;
        this.media_folder = media_folder;
        
        parser = new AutoDetectParser();
        handler = new BodyContentHandler();
        metamap = new HashMap<>();
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(CfAsset assetcontent) throws IOException {
        Document document = new Document();
        document.add(new StoredField(LuceneConstants.ID, assetcontent.getId()));
        document.add(new StoredField(LuceneConstants.ASSET_NAME, assetcontent.getName()));
        
        Metadata metadata = new Metadata();
        metamap.clear();
         
        try (FileInputStream inputstream = new FileInputStream(media_folder + File.separator + assetcontent.getName())) {
            ParseContext context = new ParseContext();
            parser.parse(inputstream, handler, metadata, context);
            System.out.println(handler.toString());
        } catch (SAXException | TikaException ex) {
            Logger.getLogger(AssetIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        //getting the list of all meta data elements 
        String[] metadataNames = metadata.names();
        for(String name : metadataNames) {		        
            //System.out.println(name + ": " + metadata.get(name));
            metamap.put(name, metadata.get(name));
        }

        switch (metamap.get("Content-Type")) {
            case "application/pdf":
                document.add(new TextField(LuceneConstants.ASSET_TEXT, handler.toString(), Field.Store.YES));
                System.out.println("PDF");
                break;
        }
    
        return document;
    }

    private void indexAssetContent(CfAsset assetcontent) throws IOException {
        Document document = getDocument(assetcontent);
        if (null != document) {
            //System.out.println("Indexing " + attributcontent.getId());
            writer.addDocument(document);
        }
    }

    public long createIndex() throws IOException {
        for (CfAsset asset : assetList.getAssetlist()) {
            indexAssetContent(asset);
        }
        return writer.numRamDocs();
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            createIndex();
            writer.commit();
            long endTime = System.currentTimeMillis();
            System.out.println("Index Time: " + (endTime - startTime) + "ms");
        } catch (IOException ex) {
            Logger.getLogger(AssetIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}