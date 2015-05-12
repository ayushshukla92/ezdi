package xmlReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.util.Arrays;
import java.util.List; 
import java.util.Iterator; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
 
public class Main {
	static Main manageDisease =new Main(); 
	static short[] shortArray = new short[15];
  private static SessionFactory factory; 
  public static void main(String argv[]) {
	 try{
	  factory = new AnnotationConfiguration().
              configure().
              //addPackage("com.xyz") //add package if used.
              addAnnotatedClass(EIndex.class).
              buildSessionFactory();
	 }
	 catch (Throwable ex) { 
		 System.err.println("Failed to create sessionFactory object." + ex);
		 throw new ExceptionInInitializerError(ex); 
	  }
	  
	  
	Main manageDisease =new Main(); 
    try {
 
	File fXmlFile = new File("/home/ubuntu/Downloads/icdXML/FY15_E-Index.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
	doc.getDocumentElement().normalize();
	NodeList nList = doc.getElementsByTagName("mainTerm");
 
	System.out.println("----------------------------");
	Boolean nec = false, nos = false;
	String code,see,seeAlso,seeCat,subCat,descriptionWithNemod,description = null;
	for (int temp = 0; temp < nList.getLength() ; temp++) 
	{
		System.out.println("0");
		nec= null;
		nos = null;
		Node nNode = nList.item(temp);
		
			
			System.out.println("1");
			Element eElement = (Element) nNode;
			descriptionWithNemod = eElement.getElementsByTagName("title").item(0).getTextContent();
			code = checkTag(eElement,nNode,"code");
			see = checkTag(eElement,nNode,"see");
			seeCat = checkTag(eElement,nNode,"seecat");
			seeAlso = checkTag(eElement,nNode,"seeAlso");
			subCat = checkTag(eElement,nNode,"subcat");
			System.out.println("*");
			if(descriptionWithNemod.contains("NEC")){
		//		System.out.println(descriptionWithNemod);
				descriptionWithNemod = descriptionWithNemod.replace("NEC", "");
				nec= true;
			}
			System.out.println("#");
			if(descriptionWithNemod.contains("NOS")){
	//			System.out.println(descriptionWithNemod);
				descriptionWithNemod = descriptionWithNemod.replace("NOS", "");
				nos= true;
			}
			byte level = 0;
			String nemod = extractNemod(descriptionWithNemod);
			System.out.println(descriptionWithNemod+descriptionWithNemod.length());
			if(descriptionWithNemod.length()>0 && descriptionWithNemod.contains(nemod)){
				description = descriptionWithNemod.replace(nemod, "");

			}
			System.out.println("2");
			short[] hierarchy = updateAndReturn((byte) 0);
			manageDisease.addDisease(nec, nos, code, description, descriptionWithNemod,hierarchy, level, see, seeAlso, seeCat, subCat, description);
			//***************************************************************************************************
			
			NodeList nList2 = eElement.getElementsByTagName("term");
			System.out.println("3");
			printTree(nList2, nNode, (byte) 1,descriptionWithNemod);
		}
	
    } 
    catch (Exception e) {
	e.printStackTrace();
    }
  }
  
private static String checkTag(Element eElement, Node nNode, String tagName)
{
	if((eElement.getElementsByTagName(tagName).getLength()>0 && eElement.getElementsByTagName(tagName).item(0).getParentNode()==nNode))
		return (eElement.getElementsByTagName(tagName).getLength()>0?eElement.getElementsByTagName(tagName).item(0).getTextContent():null);
	
	return null;
}

private static void printTree(NodeList terms,Node parentNode, byte l, String name)
{
	Boolean nec,nos;
	for (int temp = 0; temp < terms.getLength(); temp++)
	{
		
		nec = null;nos = null;
		String code,see,seeAlso,seeCat,subCat,descriptionWithNemod,description = null;
		Node nNode = terms.item(temp);	 
		Element eElement = (Element) nNode;
		String name1 = eElement.getElementsByTagName("title").item(0).getTextContent();
		if(!eElement.getParentNode().equals(parentNode))
		{
			continue;
		}
		descriptionWithNemod = name+"; " + name1;
		code = checkTag(eElement,nNode,"code");
		see = checkTag(eElement,nNode,"see");
		seeCat = checkTag(eElement,nNode,"seecat");
		seeAlso = checkTag(eElement,nNode,"seeAlso");
		subCat = checkTag(eElement,nNode,"subcat");
		byte level = l;
		
		//**********************************************
		if(descriptionWithNemod.contains("NEC")){
			descriptionWithNemod = descriptionWithNemod.replace("NEC", "");
		    nec = true;
		}
		if(descriptionWithNemod.contains("NOS")){
			descriptionWithNemod = descriptionWithNemod.replace("NOS", "");
			nos= true;
		}
		
		String nemod = extractNemod(descriptionWithNemod);
		if(descriptionWithNemod.contains(nemod)){
			description = descriptionWithNemod.replace(nemod, "");

		}
//		if(nos!=null && nos.BooleanValue())
//			System.out.println('*');
		short[] hierarchy = updateAndReturn(l);
		manageDisease.addDisease(nec, nos, code, description, descriptionWithNemod,hierarchy, level, see, seeAlso, seeCat, subCat, name1);
		//****************************************
		//System.out.println(descriptionWithNemod + " "+ code);
		NodeList nList2 = eElement.getElementsByTagName("term");
		printTree(nList2,nNode,(byte) (l+1),descriptionWithNemod);
			
	}
}

public void addDisease(Boolean nec,Boolean nos, String code,String description,String descriptionWithNemod,short[] hierarchy, byte level, String see,String seeAlso, String seeCategory, String subCategory, String title) {
    Session session = factory.openSession();
    Transaction tx = null;
    Integer diseaseID = null;
    try{
       tx = session.beginTransaction();
       EIndex disease = new EIndex();
       disease.setNEC(nec);
       disease.setNOS(nos);
       disease.setCode(code);
       disease.setDescription(description);
       disease.setDescriptionWithNemod(descriptionWithNemod);
       disease.setHierarchy(hierarchy);
       disease.setLevel(level);
       disease.setSee(see);
       disease.setSeeAlso(seeAlso);
       disease.setSeeCategory(seeCategory);
       disease.setSubCategory(subCategory);
       disease.setTitle(title);
       
       diseaseID = (Integer) session.save(disease); 
       tx.commit();
    }catch (HibernateException e) {
       if (tx!=null) tx.rollback();
       e.printStackTrace(); 
    }finally {
       session.close(); 
    }
   
 }
private static String extractNemod(String title) {
	Matcher matcher = Pattern.compile("(\\(.*\\))").matcher(title);
	if(matcher.find())
		return matcher.group(0);
	return null;
}
private static short[] updateAndReturn(byte level) {
	shortArray[level]++;
	for (int i = level+1; i < shortArray.length && shortArray[i]!=0; i++) {
		shortArray[i] = 0;
	}
	return Arrays.copyOf(shortArray,level+1);
}
 
}
