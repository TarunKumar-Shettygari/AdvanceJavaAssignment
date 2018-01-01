package com.accolite.au;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


public class XMLParsing {
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		HashMap<String,HashMap> license=new HashMap<String,HashMap>();
		HashSet<String> validlicense=new HashSet<String>();
		FileWriter fw_license=null;
		FileWriter fw_invalidLicense=null;
		FileWriter fw_invalidLicenseLOA=null;
		BufferedWriter bw_license=null;
		BufferedWriter bw_invalidLicense=null;
		BufferedWriter bw_invalidLicenseLOA=null;
		//Initializing file writers and buffered writers to write into files
		try {
			fw_license=new FileWriter("Merged_License.txt");
			fw_invalidLicense=new FileWriter("Invalid_License.txt");
			fw_invalidLicenseLOA=new FileWriter("Invalid_LicenseLOA.txt");
			bw_license=new BufferedWriter(fw_license);
			bw_invalidLicense=new BufferedWriter(fw_invalidLicense);
			bw_invalidLicenseLOA=new BufferedWriter(fw_invalidLicenseLOA);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//parsing license.xml file to get all licenses into hashmap license
	    try {
	         File inputFile = new File("License.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("CSR_Producer");
	         
	         for (int i = 0; i < nList.getLength(); i++) {
	            Node nNode = nList.item(i);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               String NIPR_Number=eElement.getAttribute("NIPR_Number");
	               //System.out.println("NIPR_Number : " + eElement.getAttribute("NIPR_Number"));
	       				
	               NodeList License = eElement.getElementsByTagName("License");
	               for(int j=0;j<License.getLength();j++) {
	            	   HashMap<String,String> attributeValues=new HashMap<String,String>();
	            	   Element ele = (Element) License.item(j);
	            	   NamedNodeMap nodeMap = ele.getAttributes();
	                   for (int k=0 ; k<nodeMap.getLength() ; k++) {
	                       Attr attribute = (Attr) nodeMap.item(k);
	                       String name = attribute.getName();
	                       String value = attribute.getValue().trim();
	                       attributeValues.put(name, value);
	                       //System.out.println(name+":"+value);
	                   }
	                   String key=Integer.parseInt(NIPR_Number)+attributeValues.get("State_Code")+Integer.parseInt(attributeValues.get("License_Number"))+attributeValues.get("Date_Status_Effective");
	 	               //System.out.println(key);
	                   attributeValues.put("NIPR_Number",NIPR_Number);
	 	               license.put(key, attributeValues);
	               }
	            }
	         }

	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	    //parsing license lines file licenseLoa.xml file, merging both the files into Merged_License.txt file and identifying invalid license lines which are written to Invalid_LicenseLOA.txt file 
	    try {
	         File inputFile = new File("License_loa.xml");
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("CSR_Producer");
	         
	         for (int i = 0; i < nList.getLength(); i++) {
		            Node nNode = nList.item(i);
		            
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		               Element eElement = (Element) nNode;
		               String NIPR_Number=eElement.getAttribute("NIPR_Number");
		               //System.out.println("NIPR_Number : " + eElement.getAttribute("NIPR_Number"));
		       				
		               NodeList License = eElement.getElementsByTagName("License");
		               for(int j=0;j<License.getLength();j++) {
		            	   HashMap<String,String> attributeValues=new HashMap<String,String>();
		            	   Element ele = (Element) License.item(j);
		            	   NamedNodeMap nodeMap = ele.getAttributes();
		                   for (int k=0 ; k<nodeMap.getLength() ; k++) {
		                       Attr attribute = (Attr) nodeMap.item(k);
		                       String name = attribute.getName();
		                       String value = attribute.getValue().trim();
		                       attributeValues.put(name, value);
		                       //System.out.println(name+":"+value);
		                   }
		                   NodeList loas = ele.getElementsByTagName("LOA");
				           for(int x=0;x<loas.getLength();x++) {
				        	   HashMap<String,String> loaValues=new HashMap<String,String>();
			            	   Element ele1 = (Element) loas.item(x);
			            	   NamedNodeMap nodeMap1 = ele1.getAttributes();
			                   for (int y=0 ; y<nodeMap1.getLength() ; y++) {
			                       Attr attribute = (Attr) nodeMap1.item(y);
			                       String name = attribute.getName();
			                       String value = attribute.getValue().trim();
			                       loaValues.put(name, value);
			                       //System.out.println(name+":"+value);
			                   }
			                   String key=Integer.parseInt(NIPR_Number)+attributeValues.get("State_Code")+Integer.parseInt(attributeValues.get("License_Number"))+attributeValues.get("Date_Status_Effective");
			                   String loaExpDate;
			                   if(loaValues.get("LOA_Expiration_Date")==null){
			                	   String issuedate=loaValues.get("LOA_Issue_Date");
			                	   StringTokenizer st=new StringTokenizer(issuedate,"/");
			                	   String mm=st.nextToken();
			                	   String dd=st.nextToken();
			                	   String yyyy=st.nextToken();
			                	   loaExpDate=mm+"/"+dd+"/"+(Integer.parseInt(yyyy)+2);
			                	   
			                   }
			                   else {
			                	   loaExpDate=loaValues.get("LOA_Expiration_Date");
			                   }
			                   String licenseExpDate;
			                   if(attributeValues.get("License_Expiration_Date")==null){
			                	   String effectivedate=loaValues.get("Date_Status_Effective");
			                	   StringTokenizer st=new StringTokenizer(effectivedate,"/");
			                	   String mm=st.nextToken();
			                	   String dd=st.nextToken();
			                	   String yyyy=st.nextToken();
			                	   licenseExpDate=mm+"/"+dd+"/"+(Integer.parseInt(yyyy)+2);
			                	   
			                   }
			                   else {
			                	   licenseExpDate=attributeValues.get("License_Expiration_Date");
			                   }
					           if(license.containsKey(key)) {
					        	   String LICENSE_HEADER_ROW ="Nipr:"+NIPR_Number+", License ID:"+attributeValues.get("License_Number")+", Jurisdiction:"+attributeValues.get("State_Code")+", Resident:"+attributeValues.get("Resident_Indicator")+", License Class:"+attributeValues.get("License_Class")+", License Effective Date:"+attributeValues.get("Date_Status_Effective")+", License Expiry Date:"+licenseExpDate+", License Status:"+attributeValues.get("License_Status")+", License Line:"+loaValues.get("LOA_Name")+", License Line Effective Date:"+loaValues.get("LOA_Issue_Date")+", License Line Expiry Date:"+loaExpDate+", License Line Status:"+loaValues.get("LOA_Status")+"\n";
					        	   validlicense.add(key);
					        	   bw_license.write(LICENSE_HEADER_ROW);
					        	   bw_license.flush();
					            	  
					           }
					           else {
					        	   String LICENSE_HEADER_ROW ="Nipr:"+NIPR_Number+", License ID:"+attributeValues.get("License_Number")+", Jurisdiction:"+attributeValues.get("State_Code")+", Resident:"+attributeValues.get("Resident_Indicator")+", License Class:"+attributeValues.get("License_Class")+", License Effective Date:"+attributeValues.get("Date_Status_Effective")+", License Expiry Date:"+licenseExpDate+", License Status:"+attributeValues.get("License_Status")+", License Line:"+loaValues.get("LOA_Name")+", License Line Effective Date:"+loaValues.get("LOA_Issue_Date")+", License Line Expiry Date:"+loaExpDate+", License Line Status:"+loaValues.get("LOA_Status")+"\n";
					        	   bw_invalidLicenseLOA.write(LICENSE_HEADER_ROW);
					        	   bw_invalidLicenseLOA.flush();
					           }
			              }
		               }
		         }
	         }
	    } catch (Exception e) {
	         e.printStackTrace();
	      }
	    //identify invalid licenses and writing them to Invalid_License.txt file
	    for(String str:license.keySet())
	    {
	    	if(!validlicense.contains(str)) {
	    		@SuppressWarnings("unchecked")
				HashMap<String,String> temp=license.get(str);
	    		String licenseExpDate;
                if(temp.get("License_Expiration_Date")==null){
             	   String effectivedate=temp.get("Date_Status_Effective");
             	   StringTokenizer st=new StringTokenizer(effectivedate,"/");
             	   String mm=st.nextToken();
             	   String dd=st.nextToken();
             	   String yyyy=st.nextToken();
             	   licenseExpDate=mm+"/"+dd+"/"+(Integer.parseInt(yyyy)+2);
             	   
                }
                else {
             	   licenseExpDate=temp.get("License_Expiration_Date");
                }
	        	String LICENSE_HEADER_ROW ="Nipr:"+temp.get("NIPR_Number")+", License ID:"+temp.get("License_Number")+", Jurisdiction:"+temp.get("State_Code")+", Resident:"+temp.get("Resident_Indicator")+", License Class:"+temp.get("License_Class")+", License Effective Date:"+temp.get("Date_Status_Effective")+", License Expiry Date:"+licenseExpDate+", License Status:"+temp.get("License_Status")+"\n";
	    		try {
					bw_invalidLicense.write(LICENSE_HEADER_ROW);
					bw_invalidLicense.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
	}
	
}



