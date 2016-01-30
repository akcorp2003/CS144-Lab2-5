/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

//test edits

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static ArrayList<User> _UserList =  new ArrayList<User>();
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile, FileWriter writer) {
    	Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element[] itemlist = getElementsByTagNameNR(doc.getDocumentElement(), "Item");
        
        //NodeList itemlist = doc.getElementsByTagName("Item");
        try {
			
			
			//build all Tables
			for(int i = 0; i < itemlist.length; i++){
				
				//build the ItemCat Table
				Element item = itemlist[i];
				String itemID = item.getAttribute("ItemID");
				Element[] categoryList = getElementsByTagNameNR(item, "Category");
				
				for(int j = 0; j < categoryList.length; j++){
					Element category = categoryList[j];
					String cat = getElementText(category );
					//writer.append();
					writer.append("&&&SCHMLEK"+itemID + ',' + cat + '\n');

				}//end for
				
				
				//build the Item Table
				String name = item.getElementsByTagName("Name").item(0).getTextContent();
				String buy_price = "";
				if(item.getElementsByTagName("Buy_Price").item(0) != null) {
					buy_price = strip(item.getElementsByTagName("Buy_Price").item(0).getTextContent());
				}
				String currently= item.getElementsByTagName("Currently").item(0).getTextContent();
				String nobids = item.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
				String first = item.getElementsByTagName("First_Bid").item(0).getTextContent();
				String location = item.getElementsByTagName("Location").item(0).getTextContent();
				String lat = "";
				if(item.getElementsByTagName("Latitude").item(0) != null){
					lat = item.getElementsByTagName("Latitude").item(0).getTextContent();
				}
				String longi = "";
				if(item.getElementsByTagName("Longitude").item(0) != null){
					longi = item.getElementsByTagName("Longitude").item(0).getTextContent();
				}
				String country = item.getElementsByTagName("Country").item(0).getTextContent();
				String started = item.getElementsByTagName("Started").item(0).getTextContent();
				String ends = item.getElementsByTagName("Ends").item(0).getTextContent();
				String desc = item.getElementsByTagName("Description").item(0).getTextContent();
				if(desc.length() > 4000){
					desc = desc.substring(0, 4000);
				}
				Element n_userid = (Element) item.getElementsByTagName("Seller").item(0);
				String userid = n_userid.getAttribute("UserID");
				
				
				writer.append("%%%SCHMLEK"+itemID + ','+name+','+buy_price+','+strip(currently)+','+nobids+','+strip(first)+','+location+
						','+lat+','+longi+','+country+','+started+','+ends+','+"\""+desc+"\""+','+userid+'\n');
				
				//build the Bids add Users Table
				//ArrayList<User> userList = new ArrayList<User>();
				
				NodeList bidlist = item.getElementsByTagName("Bid");
				for(int x = 0; x < bidlist.getLength(); x++){
					Element bid = (Element)bidlist.item(x);
					Element bidder  = (Element) bid.getElementsByTagName("Bidder").item(0);
					String bidder_id = bidder.getAttribute("UserID");
					String b_rating = bidder.getAttribute("Rating");
					String s_time = bid.getElementsByTagName("Time").item(0).getTextContent();
					Date in_date = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(s_time);
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(in_date);
					String amt = bid.getElementsByTagName("Amount").item(0).getTextContent();
					writer.append("###SCHMLEK"+itemID+','+bidder_id+','+time+','+strip(amt)+'\n');
					
					boolean flag = false;
					for(int y = 0; y < _UserList.size(); y++){
			    		if(_UserList.get(y).user_id.equals(bidder_id)){
			    			User new_user = _UserList.get(y);
							new_user.bid_rating = b_rating;
							if(bidder.getElementsByTagName("Location").item(0) != null){
								new_user.location = "\"" + bidder.getElementsByTagName("Location").item(0).getTextContent() + "\"";
							}
							if(bidder.getElementsByTagName("Country").item(0) != null){
								new_user.country = "\"" + bidder.getElementsByTagName("Country").item(0).getTextContent() + "\"";
							}
			    			_UserList.set(y, new_user);
			    			
			    			flag = true;
			    		}
			    	}
					if(!flag){ //no user was encountered, create new one and add to list
						User new_user = new User();
						new_user.user_id = bidder_id;
						new_user.bid_rating = b_rating;
						if(bidder.getElementsByTagName("Location").item(0) != null){
							new_user.location = "\"" + bidder.getElementsByTagName("Location").item(0).getTextContent() + "\"";
						}
						if(bidder.getElementsByTagName("Country").item(0) != null){
							new_user.country = "\"" + bidder.getElementsByTagName("Country").item(0).getTextContent() + "\"";
						}
						_UserList.add(new_user);
					}

					
				}
				
				
				boolean flag = false;
				//seller
				for(int y = 0; y < _UserList.size(); y++){
		    		if(_UserList.get(y).user_id.equals(userid)){
		    			User new_user = _UserList.get(y);
		    			new_user.sell_rating = n_userid.getAttribute("Rating");
		    			_UserList.set(y, new_user);
		    			flag = true;
		    		}
		    	}
				if(!flag){ //no user was encountered, create new one and add to list
					User new_user = new User();
					new_user.user_id = userid;
					new_user.sell_rating = n_userid.getAttribute("Rating");
					_UserList.add(new_user);
				}
				
				
				/*for(User user : _UserList){
					writer.append("^^^SCHMLEK"+user.user_id+','+user.bid_rating+','+user.sell_rating
						+','+user.location+','+user.country+'\n');
				}*/

				
				writer.flush();
			}//end for
			
			
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch(ParseException e){
        	e.printStackTrace();
        }
        
        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        FileWriter writer;
		try {
			writer = new FileWriter("output.csv");
			/* Process all files listed on command line. */
	        for (int i = 0; i < args.length; i++) {
	            File currentFile = new File(args[i]);
				processFile(currentFile, writer);
	            
	        }
	        //finally flush all the users out
	        for(User user : _UserList){
				writer.append("^^^SCHMLEK"+user.user_id+','+user.bid_rating+','+user.sell_rating
					+','+user.location+','+user.country+'\n');
			}
	        writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
